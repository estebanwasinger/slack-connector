package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class LeaveAndJoinChannelTest extends AbstractSlackTestCase {

    @Test
    public void testLeaveAndJoinChannel() {
        Boolean founded = false;
        getConnector().leaveChannel(CHANNEL_ID);
        for (String member : getConnector().getChannelInfo(CHANNEL_ID).getMembers()) {
            assertFalse(member.equals(USER_ID));
        }
        getConnector().joinChannel(CHANNEL_NAME);
        for (String member : getConnector().getChannelInfo(CHANNEL_ID).getMembers()) {
            if (member.equals(USER_ID)) {
                founded = true;
            }
        }
        assertTrue(founded);
    }

}
