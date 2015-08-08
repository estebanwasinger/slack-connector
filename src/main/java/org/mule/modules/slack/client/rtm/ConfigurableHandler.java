package org.mule.modules.slack.client.rtm;

import com.google.gson.Gson;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.slack.client.SlackClient;
import org.mule.modules.slack.client.rtm.filter.EventObserver;
import org.mule.modules.slack.client.rtm.filter.MessagesObserver;
import org.mule.modules.slack.client.rtm.filter.OnlyTypeObserver;
import org.mule.modules.slack.client.rtm.filter.SelfEventsFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableHandler implements EventHandler {

    public static final String USER_TYPING_EVENT = "user_typing";
    public static final String USER_FIELD = "user";
    SourceCallback sourceCallback;
    Boolean ignoreSelfEvents;
    SlackClient slackClient;
    Gson gson;
    Class<? extends Map> stringStringMap = HashMap.class;
    private final List<EventObserver> observerList;


    public ConfigurableHandler(SourceCallback sourceCallback, SlackClient slackClient, Boolean acceptMessages, Boolean onlyDMMessages, Boolean ignoreSelfEvents, Boolean userTyping, Boolean onlyNewMessages) {
        this.slackClient = slackClient;
        this.sourceCallback = sourceCallback;
        gson = new Gson();
        observerList = new ArrayList<EventObserver>();

        if (acceptMessages) {
            observerList.add(new MessagesObserver(sourceCallback, onlyDMMessages, onlyNewMessages));
        }
        if (userTyping) {
            observerList.add(new OnlyTypeObserver(sourceCallback, USER_TYPING_EVENT));
        }

    }

    public void onMessage(String message) {

        Map messageMap = gson.fromJson(message, stringStringMap);
        if (SelfEventsFilter.filter((String) messageMap.get(USER_FIELD), slackClient.getSelfId(), ignoreSelfEvents)) {
            for (EventObserver eventObserver : observerList) {
                try {
                    eventObserver.notify(messageMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
