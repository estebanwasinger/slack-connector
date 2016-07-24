package org.mule.modules.slack.client.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;

import javax.ws.rs.client.WebTarget;
import java.lang.reflect.Type;
import java.util.List;

public class Users {

    private final Type usersListType = new TypeToken<List<org.mule.modules.slack.client.model.User>>(){}.getType();
    private final SlackRequester slackRequester;
    private final Gson gson;

    public Users(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    public org.mule.modules.slack.client.model.User getUserInfo(String id) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USER_INFO)
                .queryParam("user", id);

        String output = SlackRequester.sendRequest(webTarget);

        JSONObject slackResponse = (JSONObject) new JSONObject(output).get("user");
        return gson.fromJson(slackResponse.toString(), org.mule.modules.slack.client.model.User.class);
    }

    public List<org.mule.modules.slack.client.model.User> getUserList() {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.USER_LIST);

        String output = SlackRequester.sendRequest(webTarget);

        JSONArray slackResponse = (JSONArray) new JSONObject(output).get("members");
        return gson.fromJson(slackResponse.toString(), usersListType);
    }

    public org.mule.modules.slack.client.model.User getUserInfoByName(String username) throws UserNotFoundException {
        List<org.mule.modules.slack.client.model.User> list = getUserList();
        for (org.mule.modules.slack.client.model.User user : list) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        throw new UserNotFoundException("The user: " + username + " does not exist, please check the name!");
    }

}
