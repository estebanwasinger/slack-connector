package org.mule.modules.slack.client.rtm.filter;

public class MessagesFilter {
    public static Boolean filter(String type, Boolean isNeeded){
        return isNeeded && type.toLowerCase().equals("message");
    }
}
