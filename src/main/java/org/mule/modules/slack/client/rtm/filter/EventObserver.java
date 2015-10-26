package org.mule.modules.slack.client.rtm.filter;

import java.util.Map;

public interface EventObserver {

    boolean shouldSend(Map<String, Object> message);
}
