package org.mule.modules.slack.client.rtm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.slack.client.SlackClient;
import org.mule.modules.slack.client.rtm.filter.DirectMessageFilter;
import org.mule.modules.slack.client.rtm.filter.MessagesFilter;
import org.mule.modules.slack.client.rtm.filter.SelfEventsFilter;

import java.lang.reflect.Type;
import java.util.Map;

public class ConfigurableHandler implements EventHandler {

    SourceCallback sourceCallback;
    Boolean onlyDMMessages;
    Boolean ignoreSelfEvents;
    Boolean messages;
    SlackClient slackClient;
    Gson gson;
    Type stringStringMap;


    public ConfigurableHandler(SourceCallback sourceCallback, SlackClient slackClient, Boolean messages, Boolean onlyDMMessages, Boolean ignoreSelfEvents) {
        this.slackClient = slackClient;
        this.messages = messages;
        this.onlyDMMessages = onlyDMMessages;
        this.ignoreSelfEvents = ignoreSelfEvents;
        this.sourceCallback = sourceCallback;
        gson = new Gson();
        stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
    }

    public void onMessage(String message) {
        JSONObject jsonObject = new JSONObject(message);
        String channel = jsonObject.getString("channel");
        String type = jsonObject.getString("type");
        String user = jsonObject.getString("user");

        if (DirectMessageFilter.filter(channel,onlyDMMessages) && MessagesFilter.filter(type, messages) && SelfEventsFilter.filter(user, slackClient.getSelfId(), ignoreSelfEvents)) {
            try {
                sourceCallback.process(gson.fromJson(message,stringStringMap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
