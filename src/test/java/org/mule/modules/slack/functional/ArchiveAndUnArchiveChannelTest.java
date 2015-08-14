package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class ArchiveAndUnArchiveChannelTest extends AbstractSlackTestCase {

    @Test
    public void testArchiveAndUnarchiveGroup() {
        if(!getConnector().getGroupInfo(GROUP_ID).getIsArchived()){
            getConnector().archiveGroup(GROUP_ID);
            assertEquals(true,getConnector().getGroupInfo(GROUP_ID).getIsArchived());
        }
        getConnector().unarchiveGroup(GROUP_ID);
        assertEquals(false,getConnector().getGroupInfo(GROUP_ID).getIsArchived());
    }

}
