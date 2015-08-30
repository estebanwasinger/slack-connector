package org.mule.modules.slack.functional;

import org.junit.Assert;
import org.junit.Test;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import java.util.List;

public class ListChannelsTestCases extends AbstractSlackTestCase {

    @Test
    public void testListChannel() {

        List<Channel> result = getConnector().getChannelList();
        Assert.assertEquals(result.isEmpty(), false);
    }

}
