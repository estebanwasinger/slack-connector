package org.mule.modules.slack.client.rtm.filter;

import java.util.Map;

public interface EventFilter {

    boolean shouldAccept(Map<String, Object> message);

}
