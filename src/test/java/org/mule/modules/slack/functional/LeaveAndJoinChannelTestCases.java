/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class LeaveAndJoinChannelTestCases extends AbstractSlackTestCase {

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
