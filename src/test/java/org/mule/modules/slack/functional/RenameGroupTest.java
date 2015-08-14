package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class RenameGroupTest extends AbstractSlackTestCase {

    @Test
    public void testRenameGroup() throws UserNotFoundException {
        String actualDate = getDateString();
        getConnector().renameGroup(GROUP_ID, actualDate);
        assertEquals(actualDate,getConnector().getGroupInfo(GROUP_ID).getName());
    }

}
