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
import org.mule.modules.slack.client.SlackClient;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.modules.slack.client.model.chat.attachment.Field;
import org.mule.modules.slack.client.model.file.FileUploadResponse;
import org.mule.modules.slack.client.model.group.Group;
import org.mule.modules.slack.client.model.im.DirectMessageChannel;
import org.mule.modules.slack.client.model.im.DirectMessageChannelCreationResponse;
import org.mule.modules.slack.metadata.AllChannelCategory;
import org.mule.modules.slack.metadata.ChannelCategory;
import org.mule.modules.slack.metadata.GroupCategory;
import org.mule.modules.slack.metadata.UserCategory;
import org.mule.modules.slack.retrievers.ChannelMessageRetriever;
import org.mule.modules.slack.retrievers.DirectMessageRetriever;
import org.mule.modules.slack.retrievers.GroupMessageRetriever;
import org.mule.modules.slack.retrievers.MessageRetriever;
import org.mule.modules.slack.strategy.OAuth2ConnectionStrategy;
import org.mule.modules.slack.strategy.SlackConnectionStrategy;

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
    @Processor(friendlyName = "User - Info")
    @Summary("This processor returns information about a team member.")
    @MetaDataScope(UserCategory.class)
    public User getUserInfo(@MetaDataKeyParam @Summary("User ID to get info on") @FriendlyName("User ID") String id) throws UserNotFoundException {
        return slack().getUserInfo(id);
    }

    @OAuthProtected
    @Processor(friendlyName = "User - Info by name")
    @Summary("This processor returns information about a team member.")
    @MetaDataScope(UserCategory.class)
    public User getUserInfoByName(@MetaDataKeyParam @Summary("User name to get info on") @FriendlyName("Username") String id) throws UserNotFoundException {
        return slack().getUserInfoByName(id);
    }

    @OAuthProtected
    @Summary("This processor returns a list of all users in the team. This includes deleted/deactivated users.")
    @Processor(friendlyName = "User - List")
    public List<User> getUserList() {
        return slack().getUserList();
    }

    //***********
    // Channels methods
    //***********

    @OAuthProtected
    @Summary("This processor returns a list of all channels in the team. This includes channels the caller is in, channels they are not currently in, and archived channels. The number of (non-deactivated) members in each channel is also returned.")
    @Processor(friendlyName = "Channel - List")
    public List<Channel> getChannelList() {
        return slack().getChannelList();
    }

    @OAuthProtected
    @Summary("This processor returns a portion of messages/events from the specified channel.")
    @Processor(friendlyName = "Channel - History")
    @MetaDataScope(ChannelCategory.class)
    public List<Message> getChannelHistory(@FriendlyName("Channel ID") @Summary("Channel to fetch history for") @MetaDataKeyParam String channelId, @Optional @Summary("End of time range of messages to include in results. Leave it blank to select current time.") String latestTimestamp, @Optional @Summary("Start of time range of messages to include in results. Leave it blank for timestamp 0") String oldestTimestamp, @Default("100") @Summary("Number of messages to return, between 1 and 1000.") String mountOfMessages) {
        return slack().getChannelHistory(channelId, latestTimestamp, oldestTimestamp, mountOfMessages);
    }

    @OAuthProtected
    @Summary("This processor returns information about a team channel specifyng the ID.")
    @Processor(friendlyName = "Channel - Info")
    @MetaDataScope(ChannelCategory.class)
    public Channel getChannelInfo(@Summary("Channel to get info on") @FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().getChannelById(channelId);
    }

    @OAuthProtected
    @Summary("This processor is used to create a channel.")
    @Processor(friendlyName = "Channel - Create")
    public Channel createChannel(@Summary("Name of channel to create") String channelName) {
        return slack().createChannel(channelName);
    }

    @OAuthProtected
    @Summary("This method renames a team channel. The only people who can rename a channel are team admins, or the person that originally created the channel. Others will recieve a \"not_authorized\" error.")
    @Processor(friendlyName = "Channel - Rename")
    @MetaDataScope(ChannelCategory.class)
    public Channel renameChannel(@Summary("Channel to rename") @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, @Summary("New name for channel") String channelName) {
        return slack().renameChannel(channelId, channelName);
    }

    @OAuthProtected
    @Processor(friendlyName = "Channel - Join")
    @MetaDataScope(ChannelCategory.class)
    public Channel joinChannel(@MetaDataKeyParam String channelName) {
        return slack().joinChannel(channelName);
    }

    @OAuthProtected
    @Processor(friendlyName = "Channel - Leave")
    @MetaDataScope(ChannelCategory.class)
    public Boolean leaveChannel(@FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().leaveChannel(channelId);
    }

    @OAuthProtected
    @Processor(friendlyName = "Channel - Info by Name")
    @MetaDataScope(ChannelCategory.class)
    public Channel getChannelByName(@MetaDataKeyParam String channelName) {
        return slack().getChannelByName(channelName);
    }

    @OAuthProtected
    @Processor(friendlyName = "Channel - Archive")
    @MetaDataScope(ChannelCategory.class)
    public Boolean archiveChannel(@MetaDataKeyParam String channelID) {
        return slack().archiveChannel(channelID);
    }

    @OAuthProtected
    @Processor(friendlyName = "Channel - Unarchive")
    @MetaDataScope(ChannelCategory.class)
    public Boolean unarchiveChannel(@MetaDataKeyParam String channelID) {
        return slack().unarchiveChannel(channelID);
    }

    @OAuthProtected
    @Processor(friendlyName = "Channel - Set topic")
    @MetaDataScope(ChannelCategory.class)
    public Boolean setChannelTopic(@MetaDataKeyParam String channelID, String topic) {
        return slack().setChannelTopic(channelID, topic);
    }

    @OAuthProtected
    @Processor(friendlyName = "Channel - Set purpose")
    @MetaDataScope(ChannelCategory.class)
    public Boolean setChannelPurpose(@MetaDataKeyParam String channelID, String purpose) {
        return slack().setChannelPurpose(channelID, purpose);
    }

    //***********
    // Chat methods
    //***********

    @OAuthProtected
    @Processor(friendlyName = "Chat - Post message")
    @MetaDataScope(AllChannelCategory.class)
    public MessageResponse postMessage(String message, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, @Optional @FriendlyName("Name to show") String username, @Optional @FriendlyName("Icon URL") String iconURL, @Optional Boolean asUser) {
        return slack().sendMessage(message, channelId, username, iconURL, asUser);
    }

    @OAuthProtected
    @Processor(friendlyName = "Chat - Post message with attachment")
    @MetaDataScope(AllChannelCategory.class)
    public MessageResponse postMessageWithAttachment(@Optional String message, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, @Optional @FriendlyName("Name to show") String username, @Optional @FriendlyName("Icon URL") String iconURL, @Default("#[payload]") ChatAttachment chatAttachment, @Optional Field field, @Optional Boolean asUser) {
        List<Field> fields = new ArrayList<Field>();
        fields.add(field);
        chatAttachment.setFields(fields);
        return slack().sendMessageWithAttachment(message, channelId, username, iconURL, chatAttachment, asUser);
    }

    @OAuthProtected
    @Processor(friendlyName = "Chat - Delete message")
    @MetaDataScope(AllChannelCategory.class)
    public Boolean deleteMessage(@FriendlyName("Message TimeStamp") String timeStamp, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().deleteMessage(timeStamp, channelId);
    }

    @OAuthProtected
    @Processor(friendlyName = "Chat - Update message")
    @MetaDataScope(AllChannelCategory.class)
    public Boolean updateMessage(@FriendlyName("Message TimeStamp") String timeStamp, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, String message) {
        return slack().updateMessage(timeStamp, channelId, message);
    }

    //***********
    // IM methods
    //***********

    @OAuthProtected
    @Processor(friendlyName = "IM - Open DM channel")
    @MetaDataScope(UserCategory.class)
    public DirectMessageChannelCreationResponse openDirectMessageChannel(@FriendlyName("User ID") @MetaDataKeyParam String userId) {
        return slack().openDirectMessageChannel(userId);
    }

    @OAuthProtected
    @Processor(friendlyName = "IM - Close DM channel")
   // @MetaDataScope(UserCategory.class) TODO
    public Boolean closeDirectMessageChannel(@FriendlyName("DM Channel ID") String channelId) {
        return slack().closeDirectMessageChannel(channelId);
    }

    @OAuthProtected
    @Processor(friendlyName = "IM - List DM channels")
    public List<DirectMessageChannel> listDirectMessageChannels() {
        return slack().getDirectMessageChannelsList();
    }

    @OAuthProtected
    @Summary("This processor returns a portion of messages/events from the specified DM.")
    @Processor(friendlyName = "IM - History")
    @MetaDataScope(UserCategory.class)
    public List<Message> getDMHistory(@FriendlyName("Channel ID") @Summary("Channel to fetch history for") @MetaDataKeyParam String channelID, @Optional @Summary("End of time range of messages to include in results. Leave it blank to select current time.") String latestTimestamp, @Optional @Summary("Start of time range of messages to include in results. Leave it blank for timestamp 0") String oldestTimestamp, @Default("100") @Summary("Number of messages to return, between 1 and 1000.") String mountOfMessages) {
        return slack().getDirectChannelHistory(channelID, latestTimestamp, oldestTimestamp, mountOfMessages);
    }

    //***********
    // Groups methods
    //***********

    @OAuthProtected
    @Processor(friendlyName = "Group - List")
    public List<Group> getGroupList() {
        return slack().getGroupList();
    }

    @OAuthProtected
    @Summary("This processor returns a portion of messages/events from the specified group.")
    @Processor(friendlyName = "Group - History")
    @MetaDataScope(GroupCategory.class)
    public List<Message> getGroupHistory(@FriendlyName("Channel ID") @Summary("Group to fetch history for") @MetaDataKeyParam String groupID, @Optional @Summary("End of time range of messages to include in results. Leave it blank to select current time.") String latestTimestamp, @Optional @Summary("Start of time range of messages to include in results. Leave it blank for timestamp 0") String oldestTimestamp, @Default("100") @Summary("Number of messages to return, between 1 and 1000.") String mountOfMessages) {
        return slack().getGroupHistory(groupID, latestTimestamp, oldestTimestamp, mountOfMessages);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Set topic")
    @MetaDataScope(GroupCategory.class)
    public Boolean setGroupTopic(@MetaDataKeyParam String channelID, String topic) {
        return slack().setGroupTopic(channelID, topic);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Set purpose")
    @MetaDataScope(GroupCategory.class)
    public Boolean setGroupPurpose(@MetaDataKeyParam String channelID, String purpose) {
        return slack().setGroupPurpose(channelID, purpose);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Create")
    public Group createGroup(String groupName) {
        return slack().createGroup(groupName);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Close")
    @MetaDataScope(GroupCategory.class)
    public Boolean closeGroup(@MetaDataKeyParam String channelID) {
        return slack().closeGroup(channelID);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Open")
    @MetaDataScope(GroupCategory.class)
    public Boolean openGroup(@MetaDataKeyParam String channelID) {
        return slack().openGroup(channelID);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Archive")
    @MetaDataScope(GroupCategory.class)
    public Boolean archiveGroup(@MetaDataKeyParam String channelID) {
        return slack().archiveGroup(channelID);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Unarchive")
    @MetaDataScope(GroupCategory.class)
    public Boolean unarchiveGroup(@MetaDataKeyParam String channelID) {
        return slack().unarchiveGroup(channelID);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Rename")
    @MetaDataScope(GroupCategory.class)
    public Channel renameGroup(@Summary("Group to rename") @FriendlyName("Group ID") @MetaDataKeyParam String groupId, @Summary("New name for group") String groupName) {
        return slack().renameGroup(groupId, groupName);
    }

    @OAuthProtected
    @Summary("This processor returns information about a private group.")
    @Processor(friendlyName = "Group - Info")
    @MetaDataScope(GroupCategory.class)
    public Group getGroupInfo(@Summary("Group to get info on") @FriendlyName("Group ID") @MetaDataKeyParam String channelId) {
        return slack().getGroupInfo(channelId);
    }

    @OAuthProtected
    @Processor(friendlyName = "Group - Leave")
    @MetaDataScope(GroupCategory.class)
    public Boolean leaveGroup(@FriendlyName("Group ID") @MetaDataKeyParam String channelId) {
        return slack().leaveGroup(channelId);
    }

    //***********
    // Files methods
    //***********

    @OAuthProtected
    @Processor(friendlyName = "File - Upload")
    @MetaDataScope(AllChannelCategory.class)
    public FileUploadResponse uploadFile(@FriendlyName("Channel ID") @MetaDataKeyParam String channelID, @Optional String fileName, @Optional String fileType, @Optional String title, @Optional String initialComment, @Summary("File path of the file to upload") @Path String filePath) throws IOException {
        return slack().sendFile(channelID, fileName, fileType, title, initialComment, filePath);
    }


    @OAuthProtected
    @Processor(friendlyName = "File - Upload as Input Stream")
    @MetaDataScope(AllChannelCategory.class)
    public FileUploadResponse uploadFileAsInputStreams(@Summary("Channel ID to send the message") @FriendlyName("Channel ID") @MetaDataKeyParam String channelID, @Summary("File name to show in the Slack message") @Optional String fileName,
                                                       @Optional String fileType, @Summary("Message title") @Optional String title, @Optional String initialComment, @Summary("Input Stream Reference of where to look the file to upload") @Default("#[payload]") InputStream inputStream) throws IOException {
        return slack().sendFile(channelID, fileName, fileType, title, initialComment, inputStream);
    }

    //************
    // Source methods
    //************


    @Source(friendlyName = "Retrieve messages")
    @MetaDataScope(AllChannelCategory.class)
    public Message retrieveMessages(SourceCallback source, Integer messageRetrieverInterval, @Summary("This source stream messages/events from the specified channel, group or direct message channel") @MetaDataKeyParam @FriendlyName("Channel ID") String channelID) throws Exception {
        String oldestTimeStamp;
        MessageRetriever messageRetriever = getMessageVerifierForChannel(channelID);

        if (getConnectionStrategy().getClass().equals(OAuth2ConnectionStrategy.class)) {
            while (true) {
                logger.error("Retrieve Messages source doesn't work with OAuth 2 configuration, please use Connection Management");
                Thread.sleep(5000);
            }
        }

        while (!getConnectionStrategy().isAuthorized()) {
            Thread.sleep(1000);
            logger.debug("Waiting authorization!");
        }

        //logger.info("Started retrieving messages of channel: "+slack().getChannelById(channelID).getName() +"!");

        oldestTimeStamp = messageRetriever.retrieve(slack(), channelID, null, null, "1").get(0).getTs();

        while (true) {
            Thread.sleep(messageRetrieverInterval);
            //System.out.println("Retrieving messages!");
            List<Message> messages = messageRetriever.retrieve(slack(), channelID, null, oldestTimeStamp, "1000");
            //System.out.println("Oldest TS:" + oldestTimeStamp);
            if (messages.isEmpty()) {
                //  System.out.println("No Updates!");
            } else {
                oldestTimeStamp = messages.get(0).getTs();
            }
            Integer i = messages.size();

            while (i > 0) {
                source.process(messages.get(i - 1));
                i--;
            }
        }
    }

    private MessageRetriever getMessageVerifierForChannel(String channelID) throws Exception {
        if (channelID.toLowerCase().toLowerCase().startsWith("g")) {
            return new GroupMessageRetriever();
        }

        if (channelID.toLowerCase().toLowerCase().startsWith("c")) {
            return new ChannelMessageRetriever();
        }

        if (channelID.toLowerCase().toLowerCase().startsWith("d")) {
            return new DirectMessageRetriever();
        }

        throw new Exception("Incorrect name for channel");
    }

    public SlackClient slack() {
        return connectionStrategy.getSlackClient();
    }

    public SlackConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(SlackConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

}