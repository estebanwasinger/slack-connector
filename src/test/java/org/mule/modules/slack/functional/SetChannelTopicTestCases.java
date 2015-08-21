package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class SetChannelTopicTestCases extends AbstractSlackTestCase {

    @Test
    public void testSetChannelTopic() throws UserNotFoundException {
        String date = getDateString();
        getConnector().setChannelTopic(CHANNEL_ID, date);
        String topic = getConnector().getChannelInfo(CHANNEL_ID).getTopic().getValue();
        assertEquals(topic,date);
    }

}
