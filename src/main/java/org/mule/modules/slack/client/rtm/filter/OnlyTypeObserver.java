package org.mule.modules.slack.client.rtm.filter;

import org.mule.api.callback.SourceCallback;

import java.util.Map;

public class OnlyTypeObserver implements EventObserver {

    private final String eventType;
    private final SourceCallback sourceCallback;

    public OnlyTypeObserver(SourceCallback sourceCallback, String eventType) {
        this.eventType = eventType;
        this.sourceCallback = sourceCallback;
    }

    @Override
    public boolean shouldSend(Map<String, Object> message) {
        if (message.get("type").equals(eventType)) {
            return true;
        }
        return false;
    }
}
