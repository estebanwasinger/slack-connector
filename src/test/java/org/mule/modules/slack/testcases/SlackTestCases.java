package org.mule.modules.slack.testcases;

/**
 * Created by estebanwasinger on 4/6/15.
 */
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.slack.RegressionSuite;
import org.mule.modules.slack.TestParent;
import org.mule.modules.slack.client.exceptions.SlackException;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.client.model.channel.Purpose;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.tools.devkit.ctf.exceptions.MethodExecutionFailedException;

import java.util.List;


public class SlackTestCases extends TestParent{

    public static final String TEST_MESSAGE = "test message";
    public static final String USER_ID = "U03NE28RL";
    public static final String CHANNEL_ID = "C03NE28RY";
    public static final String CHANNEL_NAME = "random";
    public static final String ESTEBANWASINGER = "estebanwasinger";

    @Category({ RegressionSuite.class })
    @Test
    public void testListChannel(){

        List<Channel> result = getConnector().getChannelList();
        Assert.assertNotEquals(result.isEmpty(),true);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testGetUserList(){
        List<User> userList = getConnector().getUserList();
        Assert.assertNotEquals(userList.isEmpty(),true);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testGetChannelInfo(){
        Channel channel = getConnector().getChannelInfo(CHANNEL_ID);
        Assert.assertEquals(channel.getName(),CHANNEL_NAME);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testGetChannelInfoByName(){
        Channel channel = getConnector().getChannelByName(CHANNEL_NAME);
        Assert.assertEquals(channel.getId(),CHANNEL_ID);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testPostMessage(){
        MessageResponse response = getConnector().postMessage(TEST_MESSAGE, CHANNEL_ID, null, null, null);
        assertEquals(response.getMessage().getText(), TEST_MESSAGE);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testGetUserInfo() throws UserNotFoundException {
        User response = getConnector().getUserInfo(USER_ID);
        assertEquals(response.getName(), ESTEBANWASINGER);
    }

    @Category({ RegressionSuite.class })
    @Test(expected = MethodExecutionFailedException.class)
    public void testGetBadChannelInfo(){
        getConnector().getChannelInfo("321321");
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testSetChannelPurpose() throws UserNotFoundException {
        String newPurpose = "newPurpose";
        getConnector().setChannelPurpose(CHANNEL_ID,"oldPurpose");
        getConnector().setChannelPurpose(CHANNEL_ID, newPurpose);
        String purpose = getConnector().getChannelInfo(CHANNEL_ID).getPurpose().getValue();
        assertEquals(purpose,newPurpose);
    }

    @Category({ RegressionSuite.class })
    @Test(expected = MethodExecutionFailedException.class)
    public void testRetrieveUserInfoFailure() throws UserNotFoundException {
        getConnector().getUserInfo("sdadsa");
    }

}