package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class SetChannelPurposeTest extends AbstractSlackTestCase {

    @Test
    public void testSetChannelPurpose() throws UserNotFoundException {
        String date = getDateString();
        getConnector().setChannelPurpose(CHANNEL_ID,date);
        String purpose = getConnector().getChannelInfo(CHANNEL_ID).getPurpose().getValue();
        assertEquals(purpose, date);
    }

}
