package org.mule.modules.slack.client.rtm;

/**
 * Created by estebanwasinger on 8/1/15.
 */
public interface EventHandler {
    void onMessage(String message);
}
