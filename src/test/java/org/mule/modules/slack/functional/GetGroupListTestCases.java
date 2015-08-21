package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.group.Group;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

public class GetGroupListTestCases extends AbstractSlackTestCase {

    @Test
    public void testGetGroupList() {
        Boolean found = false;
        for (Group group : getConnector().getGroupList()) {
            if(group.getName().equals("othergroup")) found = true;
        }
        assertEquals(true,found);
    }

}
