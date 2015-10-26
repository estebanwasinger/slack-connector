/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client;

/**
 * Created by estebanwasinger on 12/4/14.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.glassfish.jersey.uri.UriComponent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.modules.slack.client.exceptions.ChannelNotFoundException;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.modules.slack.client.model.file.FileUploadResponse;
import org.mule.modules.slack.client.model.group.Group;
import org.mule.modules.slack.client.model.im.DirectMessageChannel;
import org.mule.modules.slack.client.model.im.DirectMessageChannelCreationResponse;
import org.mule.modules.slack.client.rtm.EventHandler;
import org.mule.modules.slack.client.rtm.SlackMessageHandler;

import javax.websocket.DeploymentException;
import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SlackClient {

    private String token;
    private Gson mapper;
    private SlackRequester slackRequester;
    private SlackMessageHandler slackMessageHandler;
    private static final Logger logger = Logger.getLogger(SlackClient.class);

//    public String getSelfId() {
//        return selfId;
//    }

    private String selfId;

    public SlackClient(String token) {
        slackRequester = new SlackRequester(token);
        this.token = token;
        mapper = new Gson();
    }

    //******************
    // Auth methods
    //******************

    public String testAuth() {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.AUTH_TEST);

        return SlackRequester.sendRequest(webTarget);
    }

    public String getSelfId() {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.AUTH_TEST);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = new JSONObject(output);
        return slackResponse.getString("user_id");
    }

    public Boolean isConnected() {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.AUTH_TEST);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = new JSONObject(output);
        return slackResponse.getBoolean("ok");
    }

    //******************
    // Channel methods
    //******************

    public List<Channel> getChannelList() {

        List<Channel> list = new ArrayList<Channel>();
        WebTarget webTarget = slackRequester.getWebTarget().path(Operations.CHANNELS_LIST);
        String output = slackRequester.sendRequest(webTarget);
        JSONArray channels = (JSONArray) new JSONObject(output).get("channels");

        for (int i = 0; i < channels.length(); i++) {
            JSONObject channel = (JSONObject) channels.get(i);
            Channel newChannel = mapper.fromJson(channel.toString(), Channel.class);
            list.add(newChannel);
        }
        return list;
    }

    public Boolean leaveChannel(String channelId) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_LEAVE)
                .queryParam("channel", channelId);
        String output = slackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Channel getChannelById(String id) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_INFO)
                .queryParam("channel", id);
        String output = slackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return mapper.fromJson(slackResponse.toString(), Channel.class);
    }

    public List<Message> getChannelHistory(String channelId, String latest, String oldest, String count) {
        return getMessages(channelId, latest, oldest, count, Operations.CHANNELS_HISTORY);
    }

    public Channel createChannel(String channelName) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_CREATE)
                .queryParam("name", channelName);
        String output = slackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return mapper.fromJson(slackResponse.toString(), Channel.class);
    }

    public Channel renameChannel(String channelId, String newName) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_RENAME)
                .queryParam("name", newName)
                .queryParam("channel", channelId);
        String output = slackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return mapper.fromJson(slackResponse.toString(), Channel.class);
    }

    public Channel joinChannel(String channelName) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_JOIN)
                .queryParam("name", channelName);
        String output = slackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return mapper.fromJson(slackResponse.toString(), Channel.class);
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
        String output = slackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean setChannelTopic(String channelID, String topic) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_SETTOPIC)
                .queryParam("channel", channelID)
                .queryParam("topic", topic);
        String output = slackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean markViewChannel(String channelID, String timeStamp) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_MARK)
                .queryParam("channel", channelID)
                .queryParam("ts", timeStamp);
        String output = slackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean kickUserFromChannel(String channelID, String user) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_KICK)
                .queryParam("channel", channelID)
                .queryParam("user", user);
        String output = slackRequester.sendRequest(webTarget);

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

        String output = slackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean archiveChannel(String channelID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHANNELS_ARCHIVE)
                .queryParam("channel", channelID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    //******************
    // User methods
    //******************

    public User getUserInfo(String id) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USER_INFO)
                .queryParam("user", id);

        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("user");
        return mapper.fromJson(slackResponse.toString(), User.class);
    }

    public List<User> getUserList() {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USER_LIST);

        String output = SlackRequester.sendRequest(webTarget);

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("members");
        Type listType = new TypeToken<ArrayList<User>>() {
        }.getType();
        return mapper.fromJson(slackResponse.toString(), listType);
    }

    public User getUserInfoByName(String username) throws UserNotFoundException {
        List<User> list = getUserList();
        for (User user : list) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        throw new UserNotFoundException("The user: " + username + " does not exist, please check the name!");
    }

    //******************
    // Chat methods
    //******************

    public MessageResponse sendMessage(String message, String channelId, String username, String iconUrl, Boolean asUser) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHAT_POSTMESSAGE)
                .queryParam("channel", channelId)
                .queryParam("text", UriComponent.encode(message, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
                .queryParam("username", username)
                .queryParam("icon_url", iconUrl)
                .queryParam("as_user", String.valueOf(asUser));

        String output = SlackRequester.sendRequest(webTarget);

        return mapper.fromJson(output, MessageResponse.class);
    }

    public MessageResponse sendMessageWithAttachment(String message, String channelId, String username, String iconUrl, List<ChatAttachment> chatAttachmentArrayList, Boolean asUser) {

        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.CHAT_POSTMESSAGE)
                .queryParam("channel", channelId)
                .queryParam("text", message)
                .queryParam("username", username)
                .queryParam("icon_url", iconUrl)
                .queryParam("as_user", String.valueOf(asUser));

        webTarget = webTarget.queryParam("attachments", UriComponent.encode(mapper.toJson(chatAttachmentArrayList), UriComponent.Type.QUERY_PARAM_SPACE_ENCODED));

        String output = SlackRequester.sendRequest(webTarget);

        return mapper.fromJson(output, MessageResponse.class);
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

    //******************
    // IM methods
    //******************

    public DirectMessageChannelCreationResponse openDirectMessageChannel(String userId) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.IM_OPEN)
                .queryParam("user", userId);

        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return mapper.fromJson(slackResponse.toString(), DirectMessageChannelCreationResponse.class);
    }

    public List<DirectMessageChannel> getDirectMessageChannelsList() {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.IM_LIST);

        String output = SlackRequester.sendRequest(webTarget);

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("ims");
        Type listType = new TypeToken<ArrayList<DirectMessageChannel>>() {
        }.getType();
        return mapper.fromJson(slackResponse.toString(), listType);
    }

    public List<Message> getDirectChannelHistory(String channelId, String latest, String oldest, String count) {
        return getMessages(channelId, latest, oldest, count, Operations.IM_HISTORY);
    }

    public Boolean markViewDirectMessageChannel(String channelID, String timeStamp) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.IM_MARK)
                .queryParam("channel", channelID)
                .queryParam("ts", timeStamp);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean closeDirectMessageChannel(String channelID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.IM_CLOSE)
                .queryParam("channel", channelID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }


    //******************
    // Group methods
    //******************


    public List<Message> getGroupHistory(String channelId, String latest, String oldest, String count) {
        return getMessages(channelId, latest, oldest, count, Operations.GROUPS_HISTORY);
    }

    public List<Group> getGroupList() {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_LIST);

        String output = SlackRequester.sendRequest(webTarget);

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("groups");
        Type listType = new TypeToken<ArrayList<Group>>() {
        }.getType();
        return mapper.fromJson(slackResponse.toString(), listType);
    }

    public Group createGroup(String name) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_CREATE)
                .queryParam("name", name);

        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("group");
        return mapper.fromJson(slackResponse.toString(), Group.class);
    }

    public Boolean openGroup(String channelID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_OPEN)
                .queryParam("channel", channelID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean leaveGroup(String channelID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_LEAVE)
                .queryParam("channel", channelID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean archiveGroup(String channelID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_ARCHIVE)
                .queryParam("channel", channelID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean setGroupPurpose(String channelID, String purpose) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_SETPORPUSE)
                .queryParam("channel", channelID)
                .queryParam("purpose", purpose);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean setGroupTopic(String channelID, String topic) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_SETTOPIC)
                .queryParam("channel", channelID)
                .queryParam("topic", topic);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean closeGroup(String channelID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_CLOSE)
                .queryParam("channel", channelID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean markViewGroup(String channelID, String timeStamp) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_MARK)
                .queryParam("channel", channelID)
                .queryParam("ts", timeStamp);

        String output = SlackRequester.sendRequest(webTarget);
        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean kickUserFromGroup(String channelID, String user) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_KICK)
                .queryParam("channel", channelID)
                .queryParam("user", user);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean inviteUserToGroup(String channelID, String user) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_INVITE)
                .queryParam("channel", channelID)
                .queryParam("user", user);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Boolean unarchiveGroup(String groupID) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_UNARCHIVE)
                .queryParam("channel", groupID);

        String output = SlackRequester.sendRequest(webTarget);

        return new JSONObject(output).getBoolean("ok");
    }

    public Group renameGroup(String channelId, String newName) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_RENAME)
                .queryParam("channel", channelId)
                .queryParam("name", newName);

        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("channel");
        return mapper.fromJson(slackResponse.toString(), Group.class);
    }

    public Group getGroupInfo(String groupId) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_INFO)
                .queryParam("channel", groupId);

        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("group");
        return mapper.fromJson(slackResponse.toString(), Group.class);
    }


    //******************
    // File methods
    //******************
    //TODO -- Delete duplicated code
    public FileUploadResponse sendFile(String channelId, String fileName, String fileType, String title, String initialComment, InputStream file) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.FILES_UPLOAD)
                .queryParam("channels", channelId)
                .queryParam("filename", fileName)
                .queryParam("filetype", fileType)
                .queryParam("title", title)
                .queryParam("initial_comment", initialComment);

        String stringResponse = SlackRequester.sendRequestWithFile(webTarget, file);

        return mapper.fromJson(new JSONObject(stringResponse).getJSONObject("file").toString(), FileUploadResponse.class);
    }

    public FileUploadResponse sendFile(String channelId, String fileName, String fileType, String title, String initialComment, String filePath) throws IOException {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.FILES_UPLOAD)
                .queryParam("channels", channelId)
                .queryParam("filename", fileName)
                .queryParam("filetype", fileType)
                .queryParam("title", title)
                .queryParam("initial_comment", initialComment);

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File " + file.getAbsolutePath() + " does not exist!");
        }

        String stringResponse = SlackRequester.sendAttachmentRequest(webTarget, file);

        return mapper.fromJson(new JSONObject(stringResponse).getJSONObject("file").toString(), FileUploadResponse.class);
    }

    //******************
    // Util methods
    //******************

//    public void searchMessages(String query, Sort sort){
//
//    }

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

    //******************
    // Util methods
    //******************

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
        return mapper.fromJson(slackResponse.toString(), listType);
    }

    private String getURL(String operation) {
        return "https://slack.com/api/" + operation + "?token=" + token;
    }

}
