package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class CloseAndOpenGroupTestCases extends AbstractSlackTestCase {

    @Test
    public void testCloseAndOpenGroup() {
        if (!getConnector().getGroupInfo(GROUP_ID).getIsOpen()) {
            getConnector().closeGroup(GROUP_ID);
            assertEquals(false, getConnector().getGroupInfo(GROUP_ID).getIsOpen());
        }
        getConnector().openGroup(GROUP_ID);
        assertEquals(true, getConnector().getGroupInfo(GROUP_ID).getIsOpen());
    }

}
