package org.mule.modules.slack.client.rtm.filter;

/**
 * Created by estebanwasinger on 8/6/15.
 */
public class SelfEventsFilter {
    public static Boolean filter(String user, String myId, Boolean isNeeded){
        return !isNeeded || !user.toLowerCase().equals(myId.toLowerCase());
    }
}
