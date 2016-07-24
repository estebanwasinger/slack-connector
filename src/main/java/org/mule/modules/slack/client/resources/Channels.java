package org.mule.modules.slack.client.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.exceptions.ChannelNotFoundException;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.client.model.chat.Message;

import javax.ws.rs.client.WebTarget;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Channels {

    private final SlackRequester slackRequester;
    private final Gson gson;

    public Channels(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public List<Channel> getChannelList() {

        List<Channel> list = new ArrayList<Channel>();
        WebTarget webTarget = slackRequester.getWebTarget().path(Operations.CHANNELS_LIST);
        String output = SlackRequester.sendRequest(webTarget);
        JSONArray channels = (JSONArray) new JSONObject(output).get("channels");

        for (int i = 0; i < channels.length(); i++) {
            JSONObject channel = (JSONObject) channels.get(i);
            Channel newChannel = gson.fromJson(channel.toString(), Channel.class);
            list.add(newChannel);
        }
        return list;
    }

    public Boolean leaveChannel(String channelId) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_LEAVE)
                .queryParam("channel", channelId);
        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Channel getChannelById(String id) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_INFO)
                .queryParam("channel", id);
        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return gson.fromJson(slackResponse.toString(), Channel.class);
    }

    public List<Message> getChannelHistory(String channelId, String latest, String oldest, String count) {
        return getMessages(channelId, latest, oldest, count, Operations.CHANNELS_HISTORY);
    }

    public Channel createChannel(String channelName) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_CREATE)
                .queryParam("name", channelName);
        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return gson.fromJson(slackResponse.toString(), Channel.class);
    }

    public Channel renameChannel(String channelId, String newName) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_RENAME)
                .queryParam("name", newName)
                .queryParam("channel", channelId);
        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return gson.fromJson(slackResponse.toString(), Channel.class);
    }

    public Channel joinChannel(String channelName) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_JOIN)
                .queryParam("name", channelName);
        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return gson.fromJson(slackResponse.toString(), Channel.class);
    }

    public Channel getChannelByName(String name) {
        List<Channel> list = getChannelList();
        for (Channel channel : list) {
            if (channel.getName().equals(name)) {
                return channel;
            }
        }
        throw new ChannelNotFoundException("Channel: " + name + " does not exist.");
    }

    public Boolean setChannelPurpose(String channelID, String purpose) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_SETPURPOSE)
                .queryParam("channel", channelID)
                .queryParam("purpose", purpose);
        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean setChannelTopic(String channelID, String topic) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_SETTOPIC)
                .queryParam("channel", channelID)
                .queryParam("topic", topic);
        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean markViewChannel(String channelID, String timeStamp) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_MARK)
                .queryParam("channel", channelID)
                .queryParam("ts", timeStamp);
        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean kickUserFromChannel(String channelID, String user) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_KICK)
                .queryParam("channel", channelID)
                .queryParam("user", user);
        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean inviteUserToChannel(String channelID, String user) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_INVITE)
                .queryParam("channel", channelID)
                .queryParam("user", user);
        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean unarchiveChannel(String channelID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_UNARCHIVE)
                .queryParam("channel", channelID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean archiveChannel(String channelID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_ARCHIVE)
                .queryParam("channel", channelID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public List<Message> getMessages(String channelId, String latest, String oldest, String count, String operation) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(operation)
                .queryParam("channel", channelId)
                .queryParam("latest", latest)
                .queryParam("oldest", oldest)
                .queryParam("count", count);

        String output = SlackRequester.sendRequest(webTarget);

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("messages");
        Type listType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        return gson.fromJson(slackResponse.toString(), listType);
    }
}
