/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class SetGroupPurposeTestCases extends AbstractSlackTestCase {

    @Test
    public void testSetGroupPurpose() throws UserNotFoundException {
        String date = getDateString();
        getConnector().setGroupPurpose(GROUP_ID, date);
        String purpose = getConnector().getGroupInfo(GROUP_ID).getPurpose().getValue();
        assertEquals(purpose,date);
    }

}
