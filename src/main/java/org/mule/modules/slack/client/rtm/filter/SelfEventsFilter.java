/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
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
