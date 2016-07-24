/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.exceptions.SlackException;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class GetUserInfoTestCases extends AbstractSlackTestCase {

    @Test
    public void testGetUserInfo() throws UserNotFoundException {
        User response = getConnector().getUserInfo(USER_ID);
        assertEquals(response.getName(), ESTEBANWASINGER);
    }

    @Test(expected = SlackException.class)
    public void testGetNoExistingUserInfo() {
        getConnector().getUserInfo("sdadsa");
    }

}
