package org.mule.modules.slack.client.rtm.filter;

import java.util.Map;

public interface EventObserver {

    void notify(Map<String, Object> message) throws Exception;
}
