package org.mule.modules.slack.client.resources;

import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;

import javax.ws.rs.client.WebTarget;

public class Auth {

    private final SlackRequester slackRequester;

    public Auth(SlackRequester slackRequester) {

        this.slackRequester = slackRequester;
    }

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

}
