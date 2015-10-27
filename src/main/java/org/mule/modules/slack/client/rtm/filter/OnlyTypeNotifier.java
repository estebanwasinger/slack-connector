package org.mule.modules.slack.client.rtm.filter;

import java.util.Map;

public class OnlyTypeNotifier implements EventNotifier {

    private final String eventType;

    public OnlyTypeNotifier(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean shouldSend(Map<String, Object> message) {
        return message.get("type").equals(eventType);
    }
}
