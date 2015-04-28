package org.mule.modules.slack.testcases;

/**
 * Created by estebanwasinger on 4/6/15.
 */
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.slack.RegressionSuite;
import org.mule.modules.slack.TestParent;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.client.model.chat.MessageResponse;

import java.util.List;


public class SlackTestCases extends TestParent{

    public static final String TEST_MESSAGE = "test message";
    public static final String USER_ID = "U03NE28RL";
    public static final String CHANNEL_ID = "C03NE28RY";
    public static final String ESTEBANWASINGER = "estebanwasinger";

    @Category({ RegressionSuite.class })
    @Test
    public void testListChannel(){

        List<Channel> result = getConnector().getChannelList();
        for(Channel channel : result){
            System.out.println(channel.getName());
        }
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testPostMessage(){
        MessageResponse response = getConnector().postMessage(TEST_MESSAGE, CHANNEL_ID, null, null, null);
        assertEquals(response.getMessage().getText(), TEST_MESSAGE);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testRetrieveUserInfo() throws UserNotFoundException {
        User response = getConnector().getUserInfo(USER_ID);
        assertEquals(response.getName(), ESTEBANWASINGER);
    }

//    @Category({ RegressionSuite.class })
//    @Test(expected = SlackException.class)
//    public void testRetrieveUserInfoFailure() throws UserNotFoundException {
//        getConnector().getUserInfo("sdadsa");
//    }

}