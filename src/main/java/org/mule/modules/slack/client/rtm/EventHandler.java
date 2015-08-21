package org.mule.modules.slack.client.rtm;

public interface EventHandler {
    void onMessage(String message);
}
