package org.mule.modules.slack.client.rtm.filter;

import java.util.Map;

public class SelfEventsFilter implements EventFilter {

    private String selfUserId;
    public static final String USER_FIELD = "user";

    public SelfEventsFilter(String selfUserId) {
        this.selfUserId = selfUserId.toLowerCase();
    }

    public static Boolean filter(String user, String myId, Boolean isNeeded) {
        return !isNeeded || !user.toLowerCase().equals(myId.toLowerCase());
    }

    @Override
    public boolean shouldAccept(Map<String, Object> message) {
        return !((String) message.get(USER_FIELD)).toLowerCase().equals(selfUserId);
    }
}
