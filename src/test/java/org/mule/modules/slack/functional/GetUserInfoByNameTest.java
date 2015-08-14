package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class GetUserInfoByNameTest extends AbstractSlackTestCase {

    @Test
    public void testGetUserInfoByName() throws UserNotFoundException {
        User user = getConnector().getUserInfoByName(ESTEBANWASINGER);
        assertEquals(ESTEBANWASINGER, user.getName());
    }

}
