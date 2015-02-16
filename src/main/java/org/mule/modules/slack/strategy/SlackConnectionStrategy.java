package org.mule.modules.slack.strategy;

import org.stevew.SlackClient;

/**
 * Created by estebanwasinger on 1/30/15.
 */
public interface SlackConnectionStrategy {

    public SlackClient getSlackClient();

    public Boolean isAuthorized();

}
