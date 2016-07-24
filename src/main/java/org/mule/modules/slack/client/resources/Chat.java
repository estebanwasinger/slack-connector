package org.mule.modules.slack.client.resources;

import com.google.gson.Gson;
import org.glassfish.jersey.uri.UriComponent;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;

import javax.ws.rs.client.WebTarget;
import java.util.List;

public class Chat {

    private final SlackRequester slackRequester;
    private final Gson gson;

    public Chat(SlackRequester slackRequester, Gson gson) {
        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public MessageResponse sendMessage(String message, String channelId, String username, String iconUrl, Boolean asUser) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHAT_POSTMESSAGE)
                .queryParam("channel", channelId)
                .queryParam("text", UriComponent.encode(message, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
                .queryParam("username", username)
                .queryParam("icon_url", iconUrl)
                .queryParam("as_user", String.valueOf(asUser));

        String output = SlackRequester.sendRequest(webTarget);

        return gson.fromJson(output, MessageResponse.class);
    }

    public MessageResponse sendMessageWithAttachment(String message, String channelId, String username, String iconUrl, List<ChatAttachment> chatAttachmentArrayList, Boolean asUser) {

        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHAT_POSTMESSAGE)
                .queryParam("channel", channelId)
                .queryParam("text", message)
                .queryParam("username", username)
                .queryParam("icon_url", iconUrl)
                .queryParam("as_user", String.valueOf(asUser));

        webTarget = webTarget.queryParam("attachments", UriComponent.encode(gson.toJson(chatAttachmentArrayList), UriComponent.Type.QUERY_PARAM_SPACE_ENCODED));

        String output = SlackRequester.sendRequest(webTarget);

        return gson.fromJson(output, MessageResponse.class);
    }

    public Boolean deleteMessage(String timeStamp, String channelId) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHAT_DELETE)
                .queryParam("channel", channelId)
                .queryParam("ts", timeStamp);

        String output = SlackRequester.sendRequest(webTarget);
        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean updateMessage(String timeStamp, String channelId, String message) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHAT_UPDATE)
                .queryParam("channel", channelId)
                .queryParam("text", message)
                .queryParam("ts", timeStamp);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }
}
