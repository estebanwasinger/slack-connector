/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
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
