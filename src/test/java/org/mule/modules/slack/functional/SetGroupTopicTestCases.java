package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class SetGroupTopicTestCases extends AbstractSlackTestCase {

    @Test
    public void testSetGroupTopic() throws UserNotFoundException {
        String date = getDateString();
        getConnector().setGroupTopic(GROUP_ID, date);
        String topic = getConnector().getGroupInfo(GROUP_ID).getTopic().getValue();
        assertEquals(topic,date);
    }

}
