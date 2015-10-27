package org.mule.modules.slack.client.rtm.filter;

import java.util.Map;

public interface EventNotifier {

    boolean shouldSend(Map<String, Object> message);
}
