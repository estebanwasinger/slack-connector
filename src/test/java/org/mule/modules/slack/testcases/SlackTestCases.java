package org.mule.modules.slack.testcases;

/**
 * Created by estebanwasinger on 4/6/15.
 */

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.slack.RegressionSuite;
import org.mule.modules.slack.TestParent;
import org.mule.modules.slack.client.SlackClient;
import org.mule.modules.slack.client.exceptions.SlackException;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.client.model.channel.Purpose;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.client.model.file.FileUploadResponse;
import org.mule.modules.slack.client.model.group.Group;
import org.mule.modules.slack.client.model.im.DirectMessageChannel;
import org.mule.modules.slack.client.model.im.DirectMessageChannelCreationResponse;
import org.mule.tools.devkit.ctf.exceptions.MethodExecutionFailedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;


public class SlackTestCases extends TestParent{

    public static final String TEST_MESSAGE = "test message";
    public static final String USER_ID = "U03NE28RL";
    public static final String GROUP_ID = "G03R7DHNY";
    public static final String CHANNEL_ID = "C03NE28RY";
    public static final String CHANNEL_RENAMING ="C04KG7XAM";
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
        assertEquals(TEST_MESSAGE, response.getMessage().getText());
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
        String date = getDateString();
        getConnector().setChannelPurpose(CHANNEL_ID,date);
        String purpose = getConnector().getChannelInfo(CHANNEL_ID).getPurpose().getValue();
        assertEquals(purpose,date);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testSetChannelTopic() throws UserNotFoundException {
        String date = getDateString();
        getConnector().setChannelTopic(CHANNEL_ID, date);
        String topic = getConnector().getChannelInfo(CHANNEL_ID).getTopic().getValue();
        assertEquals(topic,date);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testGetUserInfoByName() throws UserNotFoundException {
        User user = getConnector().getUserInfoByName(ESTEBANWASINGER);
        assertEquals(ESTEBANWASINGER, user.getName());
    }

    @Category({ RegressionSuite.class })
    @Test(expected = MethodExecutionFailedException.class)
    public void testRetrieveUserInfoFailure() throws UserNotFoundException {
        getConnector().getUserInfo("sdadsa");
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testRenameChannel() throws UserNotFoundException {
        String actualDate = getDateString();
        getConnector().renameChannel(CHANNEL_RENAMING, actualDate);
        assertEquals(actualDate,getConnector().getChannelInfo(CHANNEL_RENAMING).getName());
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testRenameGroup() throws UserNotFoundException {
        String actualDate = getDateString();
        getConnector().renameGroup(GROUP_ID, actualDate);
        assertEquals(actualDate,getConnector().getGroupInfo(GROUP_ID).getName());
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testLeaveAndJoinChannel() {
        Boolean founded = false;
        getConnector().leaveChannel(CHANNEL_ID);
        for (String member : getConnector().getChannelInfo(CHANNEL_ID).getMembers()) {
            assertFalse(member.equals(USER_ID));
        }
        getConnector().joinChannel(CHANNEL_NAME);
        for (String member : getConnector().getChannelInfo(CHANNEL_ID).getMembers()) {
            if(member.equals(USER_ID)){
                founded = true;
            }
        }
        assertTrue(founded);
    }


    @Category({ RegressionSuite.class })
    @Test
    public void testArchiveAndUnarchiveChannel() {
        if(!getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived()){
            getConnector().archiveChannel(CHANNEL_RENAMING);
            assertEquals(true,getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived());
        }
        getConnector().unarchiveChannel(CHANNEL_RENAMING);
        assertEquals(false,getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived());
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testArchiveAndUnarchiveGroup() {
        if(!getConnector().getGroupInfo(GROUP_ID).getIsArchived()){
            getConnector().archiveGroup(GROUP_ID);
            assertEquals(true,getConnector().getGroupInfo(GROUP_ID).getIsArchived());
        }
        getConnector().unarchiveGroup(GROUP_ID);
        assertEquals(false,getConnector().getGroupInfo(GROUP_ID).getIsArchived());
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testUpdateAndDeleteMessage() {
        MessageResponse response = getConnector().postMessage(TEST_MESSAGE, CHANNEL_ID, null, null, null);
        getConnector().updateMessage(response.getTs(),CHANNEL_ID,"updated message");
        getConnector().deleteMessage(response.getTs(),CHANNEL_ID);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testGetDirectMessageChannelList() {
        Boolean found = false;
        for (DirectMessageChannel directMessageChannel : getConnector().listDirectMessageChannels()) {
            if(directMessageChannel.getUser().equals("USLACKBOT")) found = true;
        }

        assertEquals(true,found);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testGetGroupList() {
        Boolean found = false;
        for (Group group : getConnector().getGroupList()) {
            if(group.getName().equals("othergroup")) found = true;
        }
        assertEquals(true,found);
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testGetChannelHistory() {
        String newMessage = "My new message";
        getConnector().postMessage(newMessage,CHANNEL_ID,null,null,true);
        Message message = getConnector().getChannelHistory(CHANNEL_ID, null, null, "1").get(0);
        assertEquals(newMessage,message.getText());
    }

    // TODO Not supported by the Connector Testing Framework
    /* @Category({ RegressionSuite.class })
    @Test
    public void testUploadFileAsInputStream() throws IOException {
        String text = "Text as inputStream";
        InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        FileUploadResponse fileUploadResponse = getConnector().uploadFileAsInputStreams(CHANNEL_ID, null, null, null, null, stream);
        assertEquals(text,fileUploadResponse.getPreview());
    }*/

    @Category({ RegressionSuite.class })
    @Test
    public void testUploadFileAsInputStream() throws IOException {
        Properties prop = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream("slack-automation-credentials.properties");
        prop.load(in);
        String slackToken = (String) prop.get("config-type.accessToken");
        SlackClient slackClient = new SlackClient(slackToken);
        String text = "Text as inputStream";
        InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        FileUploadResponse fileUploadResponse = slackClient.sendFile(GROUP_ID, null, null, null, null, stream);
        assertEquals(text,fileUploadResponse.getPreview());
    }

    @Category({ RegressionSuite.class })
    @Test
    public void testOpenDMChannel() throws IOException {
        DirectMessageChannelCreationResponse directMessageChannelCreationResponse = getConnector().openDirectMessageChannel(USER_ID);
    }

    private String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("ddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
