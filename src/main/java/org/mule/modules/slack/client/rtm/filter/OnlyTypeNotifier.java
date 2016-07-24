/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.client.rtm.filter;

import java.util.Map;

public class OnlyTypeNotifier implements EventNotifier {

    private final String eventType;

    public OnlyTypeNotifier(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean shouldSend(Map<String, Object> message) {
        return message.get("type").equals(eventType);
    }
}
