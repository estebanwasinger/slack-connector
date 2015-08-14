package org.mule.modules.slack.functional;

import org.junit.Assert;
import org.junit.Test;
import org.mule.modules.slack.client.exceptions.SlackException;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class GetChannelInfoTest extends AbstractSlackTestCase {

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
