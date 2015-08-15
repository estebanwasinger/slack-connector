package org.mule.modules.slack.functional;

import org.junit.Assert;
import org.junit.Test;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class GetChannelByNameTestCases extends AbstractSlackTestCase {

    @Test
    public void testGetChannelInfoByName(){
        Channel channel = getConnector().getChannelByName(CHANNEL_NAME);
        Assert.assertEquals(channel.getId(), CHANNEL_ID);
    }

}
