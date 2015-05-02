/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.strategy;


import org.mule.modules.slack.client.SlackClient;

/**
 * Created by estebanwasinger on 1/30/15.
 */
public interface SlackConnectionStrategy {

    public SlackClient getSlackClient();

    public Boolean isAuthorized();

}
