package org.mule.modules.slack.client.rtm;

import com.google.gson.Gson;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.slack.client.rtm.filter.EventNotifier;
import org.mule.modules.slack.client.rtm.filter.EventFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableHandler implements EventHandler {

    SourceCallback sourceCallback;
    Gson gson;
    Class<? extends Map> stringStringMap = HashMap.class;
    private List<EventNotifier> observerList;
    private List<EventFilter> eventFilterList;


    public ConfigurableHandler(SourceCallback sourceCallback, List<EventNotifier> eventNotifierList, List<EventFilter> eventFilterList) {
        this.sourceCallback = sourceCallback;
        gson = new Gson();
        this.observerList = eventNotifierList;
        this.eventFilterList = eventFilterList;
    }


    public void onMessage(String message) {
        Map messageMap = gson.fromJson(message, stringStringMap);

        if (shouldBeAccepted(messageMap, eventFilterList)) {
            if (shouldBeSent(messageMap, observerList)) {
                try {
                    sourceCallback.process(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean shouldBeAccepted(Map<String, Object> message, List<EventFilter> filterList) {
        for (EventFilter eventFilter : filterList) {
            if (!eventFilter.shouldAccept(message)) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldBeSent(Map<String, Object> message, List<EventNotifier> observerList) {
        for (EventNotifier eventNotifier : observerList) {
            if (eventNotifier.shouldSend(message)) {
                return true;
            }
        }
        return false;
    }

}
