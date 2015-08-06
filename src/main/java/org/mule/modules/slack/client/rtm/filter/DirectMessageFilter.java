package org.mule.modules.slack.client.rtm.filter;

public class DirectMessageFilter {

    public static Boolean filter(String user, Boolean isNeeded){
        return !isNeeded || user.toLowerCase().startsWith("d");
    }
}
