package org.mule.modules.slack.client.rtm;

import com.google.gson.Gson;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.slack.client.rtm.filter.EventObserver;
import org.mule.modules.slack.client.rtm.filter.SlackEventFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableHandler implements EventHandler {

    SourceCallback sourceCallback;
    Gson gson;
    Class<? extends Map> stringStringMap = HashMap.class;
    private List<EventObserver> observerList;
    private List<SlackEventFilter> slackEventFilterList;


    public ConfigurableHandler(SourceCallback sourceCallback, List<EventObserver> eventObserverList, List<SlackEventFilter> slackEventFilterList) {
        this.sourceCallback = sourceCallback;
        gson = new Gson();
        this.observerList = eventObserverList;
        this.slackEventFilterList = slackEventFilterList;
    }


    public void onMessage(String message) {
        Map messageMap = gson.fromJson(message, stringStringMap);

        if (shouldBeAccepted(messageMap, slackEventFilterList)) {
            if (shouldBeSent(messageMap, observerList)) {
                try {
                    sourceCallback.process(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean shouldBeAccepted(Map<String, Object> message, List<SlackEventFilter> filterList) {
        for (SlackEventFilter slackEventFilter : filterList) {
            if (!slackEventFilter.shouldAccept(message)) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldBeSent(Map<String, Object> message, List<EventObserver> observerList) {
        for (EventObserver eventObserver : observerList) {
            if (eventObserver.shouldSend(message)) {
                return true;
            }
        }
        return false;
    }

}
