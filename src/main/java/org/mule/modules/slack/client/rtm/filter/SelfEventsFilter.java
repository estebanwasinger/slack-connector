package org.mule.modules.slack.client.rtm.filter;

public class SelfEventsFilter {

    public static Boolean filter(String user, String myId, Boolean isNeeded) {
        return !isNeeded || !user.toLowerCase().equals(myId.toLowerCase());
    }
}
