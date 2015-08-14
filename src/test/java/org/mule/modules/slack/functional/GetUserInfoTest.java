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
public class GetUserInfoTest extends AbstractSlackTestCase {

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
