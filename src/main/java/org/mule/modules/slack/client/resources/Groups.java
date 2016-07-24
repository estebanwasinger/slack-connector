package org.mule.modules.slack.client.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.client.model.group.Group;

import javax.ws.rs.client.WebTarget;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Groups {

    private final Type groupsListType = new TypeToken<List<Group>>() {}.getType();
    private final SlackRequester slackRequester;
    private final Gson gson;

    public Groups(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public List<Message> getGroupHistory(String channelId, String latest, String oldest, String count) {
        return getMessages(channelId, latest, oldest, count, Operations.GROUPS_HISTORY);
    }

    public List<Group> getGroupList() {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_LIST);

        String output = SlackRequester.sendRequest(webTarget);

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("groups");
        return gson.fromJson(slackResponse.toString(), groupsListType);
    }

    public Group createGroup(String name) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_CREATE)
                .queryParam("name", name);

        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("group");
        return gson.fromJson(slackResponse.toString(), Group.class);
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
        return gson.fromJson(slackResponse.toString(), Group.class);
    }

    public Group getGroupInfo(String groupId) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.GROUPS_INFO)
                .queryParam("channel", groupId);

        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("group");
        return gson.fromJson(slackResponse.toString(), Group.class);
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
