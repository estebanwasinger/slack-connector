/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack;

import org.apache.log4j.Logger;
import org.mule.api.annotations.*;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Path;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.Optional;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.slack.strategy.OAuth2ConnectionStrategy;
import org.mule.modules.slack.strategy.SlackConnectionStrategy;
import org.stevew.SlackClient;
import org.stevew.exceptions.SlackException;
import org.stevew.exceptions.UserNotFoundException;
import org.stevew.model.User;
import org.stevew.model.channel.Channel;
import org.stevew.model.chat.Message;
import org.stevew.model.chat.MessageResponse;
import org.stevew.model.chat.attachment.ChatAttachment;
import org.stevew.model.chat.attachment.Field;
import org.stevew.model.file.FileUploadResponse;
import org.stevew.model.group.Group;
import org.stevew.model.im.DirectMessageChannel;
import org.stevew.model.im.DirectMessageChannelCreationResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Slack Anypoint Connector
 *
 * @author Esteban Wasinger
 */
@Connector(name = "slack", friendlyName = "Slack")
public class SlackConnector {

    private static final Logger logger = Logger.getLogger(SlackConnector.class);

    @ConnectionStrategy
    SlackConnectionStrategy connectionStrategy;

    //***********
    // Users methods
    //***********

    @OAuthProtected
    @Processor
    @MetaDataScope(UserCategory.class)
    public User getUserInfo(@MetaDataKeyParam String id) throws UserNotFoundException {
        return slack().getUserInfo(id);
    }

    @OAuthProtected
    @Processor
    public List<User> getUserList() {
        return slack().getUserList();
    }

    //***********
    // Channels methods
    //***********

    @OAuthProtected
    @Processor
    public List<Channel> getChannelList() {
        return slack().getChannelList();
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(ChannelCategory.class)
    public List<Message> getChannelHistory(@FriendlyName("Channel ID") @MetaDataKeyParam String channelId, String latestTimestamp, String oldestTimestamp, String mountOfMessages) {
        return slack().getChannelHistory(channelId, latestTimestamp, oldestTimestamp, mountOfMessages);
    }

    @OAuthProtected
    @Processor(friendlyName = "Get Channel By ID")
    @MetaDataScope(ChannelCategory.class)
    public Channel getChannel(@FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().getChannelById(channelId);
    }

    @OAuthProtected
    @Processor
    public Channel createChannel(String channelName) {
        return slack().createChannel(channelName);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(ChannelCategory.class)
    public Channel renameChannel(@FriendlyName("Channel ID") @MetaDataKeyParam String channelId, String channelName) {
        return slack().renameChannel(channelId, channelName);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(ChannelCategory.class)
    public Channel joinChannel(@MetaDataKeyParam String channelName) {
        return slack().joinChannel(channelName);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(ChannelCategory.class)
    public Boolean leaveChannel(@FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().leaveChannel(channelId);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(ChannelCategory.class)
    public Channel getChannelByName(@MetaDataKeyParam String channelName) {
        return slack().getChannelByName(channelName);
    }

    //***********
    // Chat methods
    //***********

    @OAuthProtected
    @Processor
    @MetaDataScope(AllChannelCategory.class)
    public MessageResponse postMessage(String message, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, @FriendlyName("Name to show") String username, @FriendlyName("Icon URL") String iconURL) {
        return slack().sendMessage(message, channelId, username, iconURL);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(AllChannelCategory.class)
    public MessageResponse postMessageWithAttachment(@Optional String message, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, @Optional @FriendlyName("Name to show") String username, @Optional @FriendlyName("Icon URL") String iconURL, @Default("#[payload]") ChatAttachment chatAttachment, Field field) {
        List<Field> fields = new ArrayList<Field>();
        fields.add(field);
        chatAttachment.setFields(fields);
        return slack().sendMessageWithAttachment(message, channelId, username, iconURL, chatAttachment);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(AllChannelCategory.class)
    public Boolean deleteMessage(@FriendlyName("Message TimeStamp") String timeStamp, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().deleteMessage(timeStamp, channelId);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(AllChannelCategory.class)
    public Boolean updateMessage(@FriendlyName("Message TimeStamp") String timeStamp, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, String message) {
        return slack().updateMessage(timeStamp, channelId, message);
    }

    //***********
    // IM methods
    //***********

    @OAuthProtected
    @Processor
    @MetaDataScope(UserCategory.class)
    public DirectMessageChannelCreationResponse openDirectMessageChannel(@FriendlyName("User ID") @MetaDataKeyParam String userId) {
        return slack().openDirectMessageChannel(userId);
    }

    @OAuthProtected
    @Processor
    public List<DirectMessageChannel> listDirectMessageChannels() {
        return slack().getDirectMessageChannelsList();
    }

    //***********
    // Groups methods
    //***********
    
    @OAuthProtected
    @Processor
    public List<Group> getGroupList(){
        return slack().getGroupList();
    }
    
    @OAuthProtected
    @Processor
    @MetaDataScope(GroupCategory.class)
    public Boolean setGroupPurpose(@MetaDataKeyParam String channelID, String purpose){
        return slack().setGroupPurpose(channelID,purpose);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(GroupCategory.class)
    public Boolean setGroupTopic(@MetaDataKeyParam String channelID, String topic){
        return slack().setGroupTopic(channelID, topic);
    }
    
    @OAuthProtected
    @Processor
    public Group createGroup(String groupName){
        return slack().createGroup(groupName);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(GroupCategory.class)
    public Boolean closeGroup(@MetaDataKeyParam String channelID){
        return slack().closeGroup(channelID);
    }
    
    @OAuthProtected
    @Processor
    @MetaDataScope(GroupCategory.class)
    public Boolean archiveGroup(@MetaDataKeyParam String channelID){
        return slack().archiveGroup(channelID);
    }

    //***********
    // Files methods
    //***********

    @OAuthProtected
    @Processor
    @MetaDataScope(AllChannelCategory.class)
    public FileUploadResponse uploadFile(@FriendlyName("Channel ID") @MetaDataKeyParam String channelID, @Optional String fileName, @Optional String fileType, @Optional String title, @Optional String initialComment, @Summary("File path of the file to upload") @Path String filePath) throws IOException {
        return slack().sendFile(channelID, fileName, fileType, title, initialComment, filePath);
    }


    @OAuthProtected
    @Processor
    @MetaDataScope(AllChannelCategory.class)
    public FileUploadResponse uploadFileAsInputStreams(@Summary("Channel ID to send the message") @FriendlyName("Channel ID") @MetaDataKeyParam String channelID, @Summary("File name to show in the Slack message") @Optional String fileName,
                                                       @Optional String fileType, @Summary("Message title")@Optional String title, @Optional String initialComment, @Summary("Input Stream Reference of where to look the file to upload") @Default("#[payload]") InputStream inputStream) throws IOException {
        return slack().sendFile(channelID, fileName, fileType, title, initialComment, inputStream);
    }

    //************
    // Source methods
    //************


    @Source
    @MetaDataScope(AllChannelCategory.class)
    public Message retrieveMessages(SourceCallback source, Integer messageRetreiveInterval, @MetaDataKeyParam @FriendlyName("Channel ID") String channelID) throws Exception{
        String oldestTimeStamp = String.valueOf(new java.util.Date().getTime());

        if(getConnectionStrategy().getClass().equals(OAuth2ConnectionStrategy.class)){
            while (true){
                logger.error("Retrieve Messages source doesn't work with OAuth 2 configuration, please use Connection Management");
                Thread.sleep(5000);
            }
        }

        while(!getConnectionStrategy().isAuthorized()){
            Thread.sleep(1000);
            logger.debug("Waiting authorization!");
        }

        logger.info("Started retrieving messages of channel: "+slack().getChannelById(channelID).getName() +"!");

        oldestTimeStamp = slack().getChannelHistory(channelID,null,null,"1").get(0).getTs();

    	while (true)
        {	
            Thread.sleep(messageRetreiveInterval);
            //System.out.println("Retrieving messages!");
            List<Message> messages = slack().getChannelHistory(channelID, null, oldestTimeStamp, "1000");
            //System.out.println("Oldest TS:" + oldestTimeStamp);
            if(messages.size()==0){
              //  System.out.println("No Updates!");
            }else {
                oldestTimeStamp = messages.get(0).getTs();
            }
            Integer i = messages.size();

            while(i>0){
                source.process(messages.get(i-1));
                i--;
            }
        }
    }

    protected SlackClient slack() {
        return connectionStrategy.getSlackClient();
    }

    public SlackConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(SlackConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

}