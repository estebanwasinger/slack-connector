package org.mule.modules.slack.client.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.usergroups.Usergroup;

import javax.ws.rs.client.WebTarget;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserGroups {

    private static final String USERGROUP = "usergroup";
    private static final String INCLUDE_DISABLED = "include_disabled";
    private static final String INCLUDE_COUNT = "include_count";
    private static final String INCLUDE_USERS = "include_users";
    private static final String NAME = "name";
    private static final String HANDLE = "handle";
    private static final String DESCRIPTION = "description";
    private static final String CHANNELS = "channels";
    private static final String USERS = "users";
    private final SlackRequester slackRequester;
    private final Gson gson;
    private final Type userGroupsListType = new TypeToken<ArrayList<Usergroup>>() {}.getType();
    private final Type stringListType = new TypeToken<List<String>>(){}.getType();

    public UserGroups(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public List<Usergroup> listUserGroups(boolean includeDisabled, boolean includeCount, boolean includeUsers){
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USERGROUPS_LIST)
                .queryParam(INCLUDE_DISABLED, (includeDisabled) ? 1 : 0)
                .queryParam(INCLUDE_COUNT, (includeCount) ? 1 : 0)
                .queryParam(INCLUDE_USERS, (includeUsers) ? 1 : 0);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("usergroups");
        return gson.fromJson(slackResponse.toString(), userGroupsListType);
    }

    public Usergroup enableUserGroup(String usergroupId, boolean includeCount){
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USERGROUPS_ENABLE)
                .queryParam(USERGROUP, usergroupId)
                .queryParam(INCLUDE_COUNT, (includeCount) ? 1 : 0);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }

    public Usergroup disableUserGroup(String usergroupId, boolean includeCount){
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USERGROUPS_DISABLE)
                .queryParam(USERGROUP, usergroupId)
                .queryParam(INCLUDE_COUNT, (includeCount) ? 1 : 0);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }

    public Usergroup createUserGroup(String name, String handle, String description, List<String> channels, boolean include_count){
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USERGROUPS_CREATE)
                .queryParam(NAME, name)
                .queryParam(HANDLE, handle)
                .queryParam(DESCRIPTION, description)
                .queryParam(CHANNELS, StringUtils.join(channels, ','))
                .queryParam(INCLUDE_COUNT, (include_count) ? 1 : 0);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }

    public Usergroup updateUserGroup(String usergroupId, String name, String handle, String description, List<String> channels, boolean include_count){
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USERGROUPS_UPDATE)
                .queryParam(USERGROUP, usergroupId)
                .queryParam(NAME, name)
                .queryParam(HANDLE, handle)
                .queryParam(DESCRIPTION, description)
                .queryParam(CHANNELS, StringUtils.join(channels, ','))
                .queryParam(INCLUDE_COUNT, (include_count) ? 1 : 0);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }

    public List<String> listUsersFromUserGroup(String usergroupId, boolean includeDisabled){
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USERGROUPS_USERS_LIST)
                .queryParam(USERGROUP, usergroupId)
                .queryParam(INCLUDE_DISABLED, (includeDisabled) ? 1 : 0);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERS);
        return gson.fromJson(slackResponse.toString(), stringListType);
    }
    public Usergroup updateUsersFromUserGroup(String usergroupId, List<String> users, boolean includeCount){
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USERGROUPS_USERS_UPDATE)
                .queryParam(USERGROUP, usergroupId)
                .queryParam(USERS, StringUtils.join(users, ','))
                .queryParam(INCLUDE_COUNT, (includeCount) ? 1 : 0);

        String output = SlackRequester.sendRequest(webTarget);
        JSONObject slackResponse = (JSONObject) new JSONObject(output).get(USERGROUP);
        return gson.fromJson(slackResponse.toString(), Usergroup.class);
    }
}
