package org.mule.modules.slack.functional;

import org.junit.Assert;
import org.junit.Test;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import java.util.List;

public class GetUserListTestCases extends AbstractSlackTestCase {

    @Test
    public void testGetUserList(){
        List<User> userList = getConnector().getUserList();
        Assert.assertNotEquals(userList.isEmpty(), true);
    }
}
