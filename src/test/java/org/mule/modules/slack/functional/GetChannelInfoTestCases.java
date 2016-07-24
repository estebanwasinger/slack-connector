/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.functional;

import org.junit.Assert;
import org.junit.Test;
import org.mule.modules.slack.client.exceptions.SlackException;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class GetChannelInfoTestCases extends AbstractSlackTestCase {

    @Test
    public void testGetChannelInfo(){
        Channel channel = getConnector().getChannelInfo(CHANNEL_ID);
        Assert.assertEquals(channel.getName(), CHANNEL_NAME);
    }

    @Test(expected = SlackException.class)
    public void testGetBadChannelInfo(){
        getConnector().getChannelInfo("321321");
    }

}
