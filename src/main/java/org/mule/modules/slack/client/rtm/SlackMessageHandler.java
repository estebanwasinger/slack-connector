/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.client.rtm;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.mule.modules.slack.client.exceptions.SlackException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class SlackMessageHandler implements MessageHandler.Whole<String> {

    private String webSocketUrl;
    private Session websocketSession;
    private long lastPingSent = 0;
    private volatile long lastPingAck = 0;
    private boolean reconnectOnDisconnection = true;
    private long messageId = 0;
    public EventHandler messageHandler;

    public SlackMessageHandler(String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
    }

    public void connect() throws IOException, DeploymentException, InterruptedException {
        ClientManager client = ClientManager.createClient();
        client.getProperties().put(ClientProperties.LOG_HTTP_UPGRADE, true);
        final MessageHandler handler = this;
        websocketSession = client.connectToServer(new Endpoint() {

            @Override
            public void onOpen(Session session, EndpointConfig config) {
                session.addMessageHandler(handler);
            }

        }, URI.create(webSocketUrl));
        while (true) {
            try {
                if (lastPingSent != lastPingAck) {
                    // disconnection happened
                    websocketSession.close();
                    lastPingSent = 0;
                    lastPingAck = 0;
                    connect();
                    continue;
                } else {
                    lastPingSent = getNextMessageId();
                    websocketSession.getBasicRemote().sendText("{\"type\":\"ping\",\"id\":" + lastPingSent + "}");
                }
                Thread.sleep(20000);
            } catch (Exception e) {
                websocketSession.close();
                throw new SlackException("Error in RTM Connection", e);
            }
        }
    }

    public void onMessage(String message) {
        if (message.contains("{\"type\":\"pong\",\"reply_to\"")) {
            int rightBracketIdx = message.indexOf('}');
            String toParse = message.substring(26, rightBracketIdx);
            lastPingAck = Integer.parseInt(toParse);
            return;
        }
        if (!message.contains("{\"type\":\"hello\"})")) {
            messageHandler.onMessage(message);
        }
    }

    private synchronized long getNextMessageId() {
        return messageId++;
    }

}
