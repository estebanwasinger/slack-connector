/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
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
