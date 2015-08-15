package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class ArchiveAndUnArchiveGroupTestCases extends AbstractSlackTestCase {

    @Test
    public void testArchiveAndUnarchiveChannel() {
        if (!getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived()) {
            getConnector().archiveChannel(CHANNEL_RENAMING);
            assertEquals(true, getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived());
        }
        getConnector().unarchiveChannel(CHANNEL_RENAMING);
        assertEquals(false, getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived());
    }

}
