/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.mule.modules.slack.client.resources.*;
import org.mule.modules.slack.client.rtm.EventHandler;
import org.mule.modules.slack.client.rtm.SlackMessageHandler;

import javax.websocket.DeploymentException;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;

public class SlackClient {

    private static final Logger logger = Logger.getLogger(SlackClient.class);
    private SlackRequester slackRequester;
    private SlackMessageHandler slackMessageHandler;
    private String selfId;
    private String token;
    private Gson gson;
    public final Chat chat;
    public final Users users;
    public final UserGroups usergroups;
    public final Channels channels;
    public final Groups groups;
    public final Files files;
    public final Auth auth;
    public final IM im;

    public SlackClient(String token) {
        slackRequester = new SlackRequester(token);
        this.token = token;
        gson = new Gson();
        usergroups = new UserGroups(slackRequester, gson);
        channels = new Channels(slackRequester, gson);
        users = new Users(slackRequester, gson);
        groups = new Groups(slackRequester, gson);
        chat = new Chat(slackRequester, gson);
        auth = new Auth(slackRequester);
        im = new IM(slackRequester, gson);
        files = new Files(slackRequester, gson);
    }

    //******************
    // RTM
    //******************

    public String getWebSockerURI() {
        WebTarget webTarget = slackRequester.getWebTarget().path(Operations.RTM_START);
        String s = SlackRequester.sendRequest(webTarget);
        selfId = new JSONObject(s).getJSONObject("self").getString("id");
        return new JSONObject(s).getString("url");
    }

    public void startRealTimeCommunication(EventHandler messageHandler) throws DeploymentException, InterruptedException, IOException {
        slackMessageHandler = new SlackMessageHandler(this.getWebSockerURI());
        slackMessageHandler.messageHandler = messageHandler;
        while (true) {
            try {
                slackMessageHandler.connect();
            } catch (Exception e) {
                logger.error("Error Cause: ", e);
                logger.warn("Retrying RTM Communication in 20 Seconds");
                Thread.sleep(20000);
                logger.warn("Starting RTM Communication");
                slackMessageHandler = new SlackMessageHandler(this.getWebSockerURI());
                slackMessageHandler.messageHandler = messageHandler;
            }
        }
    }

    public SlackMessageHandler getSlackMessageHandler() {
        return slackMessageHandler;
    }


    private String getURL(String operation) {
        return "https://slack.com/api/" + operation + "?token=" + token;
    }

}
