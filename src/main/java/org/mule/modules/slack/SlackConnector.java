/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.NestedProcessor;
import org.mule.api.annotations.*;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Path;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.RefOnly;
import org.mule.api.callback.SourceCallback;
import org.mule.api.store.ObjectStore;
import org.mule.modules.slack.client.SlackClient;
import org.mule.modules.slack.client.exceptions.SlackException;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.client.model.User;
import org.mule.modules.slack.client.model.channel.Channel;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.modules.slack.client.model.file.FileUploadResponse;
import org.mule.modules.slack.client.model.group.Group;
import org.mule.modules.slack.client.model.im.DirectMessageChannel;
import org.mule.modules.slack.client.model.im.DirectMessageChannelCreationResponse;
import org.mule.modules.slack.client.model.usergroups.Usergroup;
import org.mule.modules.slack.client.rtm.ConfigurableHandler;
import org.mule.modules.slack.client.rtm.filter.*;
import org.mule.modules.slack.client.utils.Tuple;
import org.mule.modules.slack.config.BasicSlackConfig;
import org.mule.modules.slack.config.SlackOAuth2Config;
import org.mule.modules.slack.metadata.AllChannelCategory;
import org.mule.modules.slack.metadata.ChannelCategory;
import org.mule.modules.slack.metadata.GroupCategory;
import org.mule.modules.slack.metadata.UserCategory;
import org.mule.modules.slack.retrievers.ChannelMessageRetriever;
import org.mule.modules.slack.retrievers.DirectMessageRetriever;
import org.mule.modules.slack.retrievers.GroupMessageRetriever;
import org.mule.modules.slack.retrievers.MessageRetriever;
import org.mule.modules.slack.storage.ObjectStoreStorage;
import org.mule.modules.slack.storage.SlackStorage;

import javax.inject.Inject;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Slack Anypoint Connector
 *
 * @author Esteban Wasinger
 */

@Connector(name = "slack", friendlyName = "Slack") public class SlackConnector {

    private static final Logger logger = Logger.getLogger(SlackConnector.class);
    private static final String NUMBER_OF_MESSAGES = "Number of messages to return, the value should be between 1 and 1000.";
    private static final String USER_TYPING_EVENT = "user_typing";
    private static final String IM_CREATED = "im_created";
    private static final String FILE_CREATED = "file_created";
    private static final String FILE_SHARED = "file_shared";
    private static final String FILE_PUBLIC = "file_public";

    @Inject MuleContext muleContext;

    @Config BasicSlackConfig slackConfig;

    //***********
    // Users methods
    //***********

    /**
     * This processor returns information about a team member.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-user-info}
     *
     * @param id ID of the desired User
     * @return The desired User
     * @throws org.mule.modules.slack.client.exceptions.UserNotFoundException When the user doesn't exist
     */
    @OAuthProtected
    @Processor(friendlyName = "User - Info")
    @Summary("This processor returns information about a team member.")
    @MetaDataScope(UserCategory.class)
    public User getUserInfo(@MetaDataKeyParam @Summary("User ID to get info on") @FriendlyName("User ID") String id) {
        return slack().users.getUserInfo(id);
    }

    /**
     * This processor returns information about a team member.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-user-info-by-name}
     *
     * @param username Username of the desired User
     * @return The desired User
     * @throws org.mule.modules.slack.client.exceptions.UserNotFoundException When the user doesn't exist
     */

    @OAuthProtected
    @Processor(friendlyName = "User - Info by name")
    @Summary("This processor returns information about a team member.")
    public User getUserInfoByName(@Summary("User name to get info on") @FriendlyName("Username") String username) throws UserNotFoundException {
        return slack().users.getUserInfoByName(username);
    }

    /**
     * This processor returns a list of all user in the team. This includes deleted/deactivated user.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-user-list}
     *
     * @return List of Slack Users
     */

    @OAuthProtected
    @Summary("This processor returns a list of all user in the team. This includes deleted/deactivated user.")
    @Processor(friendlyName = "User - List")
    public List<User> getUserList() {
        return slack().users.getUserList();
    }

    //***********
    // Channels methods
    //***********

    /**
     * This processor returns a list of all channels in the team. This includes channels the caller is in, channels they are not currently in, and archived channels. The number of (non-deactivated) members in each channel is also returned.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-channel-list}
     *
     * @return List of Slack Channels
     */

    @OAuthProtected
    @Summary("This processor returns a list of all channels in the team. This includes channels the caller is in, channels they are not currently in, and archived channels. The number of (non-deactivated) members in each channel is also returned.")
    @Processor(friendlyName = "Channel - List")
    public List<Channel> getChannelList() {
        return slack().channels.getChannelList();
    }

    /**
     * This processor returns a portion of messages/events from the specified channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-channel-history}
     *
     * @param channelId       Channel to fetch history for
     * @param latestTimestamp End of time range of messages to include in results. Leave it blank to select current time.
     * @param oldestTimestamp Start of time range of messages to include in results. Leave it blank for timestamp 0
     * @param mountOfMessages Number of messages to return, the value should be between 1 and 1000.
     * @return List of messages of a Channel
     */

    @OAuthProtected
    @Summary("This processor returns a portion of messages/events from the specified channel.")
    @Processor(friendlyName = "Channel - History")
    @MetaDataScope(ChannelCategory.class)
    public List<Message> getChannelHistory(@FriendlyName("Channel ID") @Summary("Channel to fetch history for") @MetaDataKeyParam String channelId,
            @Optional @Summary("End of time range of messages to include in results. Leave it blank to select current time.") String latestTimestamp,
            @Optional @Summary("Start of time range of messages to include in results. Leave it blank for timestamp 0") String oldestTimestamp,
            @Default("100") @Summary(NUMBER_OF_MESSAGES) String mountOfMessages) {
        return slack().channels.getChannelHistory(channelId, latestTimestamp, oldestTimestamp, mountOfMessages);
    }

    /**
     * This processor returns information about a team channel specifying the ID.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-channel-info}
     *
     * @param channelId Channel to get info on
     * @return A Channel
     */

    @OAuthProtected
    @Summary("This processor returns information about a team channel specifying the ID.")
    @Processor(friendlyName = "Channel - Info")
    @MetaDataScope(ChannelCategory.class)
    public Channel getChannelInfo(@Summary("Channel to get info on") @FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().channels.getChannelById(channelId);
    }

    /**
     * This processor returns information about a team channel specifyng the name.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-channel-by-name}
     *
     * @param channelName Channel name to get info on
     * @return A Channel
     */

    @OAuthProtected
    @Processor(friendlyName = "Channel - Info by Name")
    public Channel getChannelByName(String channelName) {
        return slack().channels.getChannelByName(channelName);
    }

    /**
     * This processor is used to create a channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:create-channel}
     *
     * @param channelName Name of channel to create
     * @return A Channel
     */

    @OAuthProtected
    @Summary("This processor is used to create a channel.")
    @Processor(friendlyName = "Channel - Create")
    public Channel createChannel(@Summary("Name of channel to create") String channelName) {
        return slack().channels.createChannel(channelName);
    }

    /**
     * This method renames a team channel. The only people who can rename a channel are team admins, or the person that originally created the channel. Others will recieve a "not_authorized" error.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:rename-channel}
     *
     * @param channelId   Channel to rename
     * @param channelName New channel name
     * @return A Channel
     */

    @OAuthProtected
    @Summary("This method renames a team channel. The only people who can rename a channel are team admins, or the person that originally created the channel. Others will recieve a \"not_authorized\" error.")
    @Processor(friendlyName = "Channel - Rename")
    @MetaDataScope(ChannelCategory.class)
    public Channel renameChannel(@Summary("Channel to rename") @FriendlyName("Channel ID") @MetaDataKeyParam String channelId,
            @Summary("New name for channel") String channelName) {
        return slack().channels.renameChannel(channelId, channelName);
    }

    /**
     * This method is used to join a channel. If the channel does not exist, it is created.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:join-channel}
     *
     * @param channelName Name of the channel to Join
     * @return If successful, the command returns a channel object, including state information:
     */

    @OAuthProtected
    @Processor(friendlyName = "Channel - Join")
    @MetaDataScope(ChannelCategory.class)
    public Channel joinChannel(@MetaDataKeyParam String channelName) {
        return slack().channels.joinChannel(channelName);
    }

    /**
     * This processor is used to leave a channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:leave-channel}
     *
     * @param channelId ID of the channel to Leave
     * @return Boolean result. This method will not return an error if the user was not in the channel before it was called.
     */

    @OAuthProtected
    @Processor(friendlyName = "Channel - Leave")
    @MetaDataScope(ChannelCategory.class)
    public Boolean leaveChannel(@FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().channels.leaveChannel(channelId);
    }

    /**
     * This processor archives a channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:archive-channel}
     *
     * @param channelID ID of the Channel to Archive
     * @return Boolean result
     */

    @OAuthProtected
    @Processor(friendlyName = "Channel - Archive")
    @MetaDataScope(ChannelCategory.class)
    public Boolean archiveChannel(@MetaDataKeyParam String channelID) {
        return slack().channels.archiveChannel(channelID);
    }

    /**
     * This processor unarchives a channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:unarchive-channel}
     *
     * @param channelID ID of the Channel to Unarchive
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Channel - Unarchive")
    @MetaDataScope(ChannelCategory.class)
    public Boolean unarchiveChannel(@MetaDataKeyParam String channelID) {
        return slack().channels.unarchiveChannel(channelID);
    }

    /**
     * This processor is used to change the topic of a channel. The calling user must be a member of the channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:set-channel-topic}
     *
     * @param channelID ID of the channel to change the topic
     * @param topic     New channel topic
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Channel - Set topic")
    @MetaDataScope(ChannelCategory.class)
    public Boolean setChannelTopic(@MetaDataKeyParam String channelID, String topic) {
        return slack().channels.setChannelTopic(channelID, topic);
    }

    /**
     * This processor is used to change the purpose of a channel. The calling user must be a member of the channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:set-channel-purpose}
     *
     * @param channelID ID of the channel to change the purpose
     * @param purpose   New channel purpose
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Channel - Set purpose")
    @MetaDataScope(ChannelCategory.class)
    public Boolean setChannelPurpose(@MetaDataKeyParam String channelID, String purpose) {
        return slack().channels.setChannelPurpose(channelID, purpose);
    }

    //***********
    // Chat methods
    //***********

    /**
     * This processor posts a message to a channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:post-message}
     *
     * @param message   Message to post
     * @param channelId ID of the channel to post the message
     * @param username  Name to show in the message
     * @param iconURL   Icon URL of the icon to show in the message
     * @param asUser    Boolean indicating if the message is showed as a User or as a Bot
     * @return MessageResponse
     */
    @OAuthProtected
    @Processor(friendlyName = "Chat - Post message")
    @MetaDataScope(AllChannelCategory.class)
    public MessageResponse postMessage(String message, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, @Optional @FriendlyName("Name to show") String username,
            @Optional @FriendlyName("Icon URL") String iconURL, @Optional Boolean asUser) {
        return slack().chat.sendMessage(message, channelId, username, iconURL, BooleanUtils.toBoolean(asUser));
    }

    /**
     * /**
     * This processor post a message with attachments in a channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:post-message-with-attachment}
     *
     * @param message            Message to post
     * @param channelId          ID of the channel to post the message
     * @param username           Name to show in the message
     * @param iconURL            Icon URL of the icon to show in the message
     * @param chatAttachmentList List of attachments to be sent in the message
     * @param asUser             Boolean indicating if the message is showed as a User or as a Bot
     * @return MessageResponse
     */
    @OAuthProtected
    @Processor(friendlyName = "Chat - Post message with attachment")
    @MetaDataScope(AllChannelCategory.class)
    public MessageResponse postMessageWithAttachment(@Optional String message, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId,
            @Optional @FriendlyName("Name to show") String username, @Optional @FriendlyName("Icon URL") String iconURL,
            @FriendlyName("Attachment List") @Default("#[payload]") @RefOnly List<ChatAttachment> chatAttachmentList, @Optional Boolean asUser) {
        return slack().chat.sendMessageWithAttachment(message, channelId, username, iconURL, chatAttachmentList, BooleanUtils.toBoolean(asUser));
    }

    /**
     * This processor deletes a message from a channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:delete-message}
     *
     * @param timeStamp TimeStamp of the message to delete
     * @param channelId ID of the channel of the message
     * @return Boolean
     */
    @OAuthProtected
    @Processor(friendlyName = "Chat - Delete message")
    @MetaDataScope(AllChannelCategory.class)
    public Boolean deleteMessage(@FriendlyName("Message TimeStamp") String timeStamp, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().chat.deleteMessage(timeStamp, channelId);
    }

    /**
     * This processor updates a message in a channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:update-message}
     *
     * @param timeStamp TimeStamp of the message to delete
     * @param channelId ID of the channel of the message
     * @param message   New message
     * @return Boolean
     */
    @OAuthProtected
    @Processor(friendlyName = "Chat - Update message")
    @MetaDataScope(AllChannelCategory.class)
    public Boolean updateMessage(@FriendlyName("Message TimeStamp") String timeStamp, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, String message) {
        return slack().chat.updateMessage(timeStamp, channelId, message);
    }

    //***********
    // IM methods
    //***********

    /**
     * This processor opens a direct message channel with another member of your Slack team.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:open-direct-message-channel}
     *
     * @param userId User ID to open a direct message channel with.
     * @return DirectMessageChannelCreationResponse. If the channel was already open the response will include no_op and already_open properties:
     */
    @OAuthProtected
    @Processor(friendlyName = "IM - Open DM channel")
    @MetaDataScope(UserCategory.class)
    public DirectMessageChannelCreationResponse openDirectMessageChannel(@FriendlyName("User ID") @MetaDataKeyParam String userId) {
        return slack().im.openDirectMessageChannel(userId);
    }

    /**
     * Closes a direct message channel.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:close-direct-message-channel}
     *
     * @param channelId Direct message channel ID to close.
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "IM - Close DM channel")
    // @MetaDataScope(UserCategory.class) TODO
    public Boolean closeDirectMessageChannel(@FriendlyName("DM Channel ID") String channelId) {
        return slack().im.closeDirectMessageChannel(channelId);
    }

    /**
     * This processor returns a list of all im channels that the user has.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:list-direct-message-channels}
     *
     * @return List of the available Direct message channels
     */
    @OAuthProtected
    @Processor(friendlyName = "IM - List DM channels")
    public List<DirectMessageChannel> listDirectMessageChannels() {
        return slack().im.getDirectMessageChannelsList();
    }

    /**
     * This processor returns a portion of messages/events from the specified direct message channel. To read the entire history for a direct message channel, call the method with no latest or oldest arguments.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-d-m-history}
     *
     * @param channelID       Channel to fetch history for
     * @param latestTimestamp End of time range of messages to include in results. Leave it blank to select current time.
     * @param oldestTimestamp Start of time range of messages to include in results. Leave it blank for timestamp 0
     * @param mountOfMessages Number of messages to return. The value should be between 1 and 1000.
     * @return List of messages of a Channel
     */
    @OAuthProtected
    @Summary("This processor returns a portion of messages/events from the specified DM.")
    @Processor(friendlyName = "IM - History")
    @MetaDataScope(UserCategory.class)
    public List<Message> getDMHistory(@FriendlyName("Channel ID") @Summary("Channel to fetch history for") @MetaDataKeyParam String channelID,
            @Optional @Summary("End of time range of messages to include in results. Leave it blank to select current time.") String latestTimestamp,
            @Optional @Summary("Start of time range of messages to include in results. Leave it blank for timestamp 0") String oldestTimestamp,
            @Default("100") @Summary("Number of messages to return, between 1 and 1000.") String mountOfMessages) {
        return slack().im.getDirectChannelHistory(channelID, latestTimestamp, oldestTimestamp, mountOfMessages);
    }

    //***********
    // Groups methods
    //***********

    /**
     * This processor returns a list of groups in the team that the caller is in and archived groups that the caller was in. The list of (non-deactivated) members in each group is also returned.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-group-list}
     *
     * @return A list of the available Groups
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - List")
    public List<Group> getGroupList() {
        return slack().groups.getGroupList();
    }

    /**
     * This processor returns a portion of messages/events from the specified private group. To read the entire history for a group, call the method with no latest or oldest arguments.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-group-history}
     *
     * @param groupID         Group to fetch history for
     * @param latestTimestamp End of time range of messages to include in results. Leave it blank to select current time.
     * @param oldestTimestamp Start of time range of messages to include in results. Leave it blank for timestamp 0
     * @param mountOfMessages Number of messages to return, between 1 and 1000.
     * @return List of messages of a Channel
     */
    @OAuthProtected
    @Summary("This processor returns a portion of messages/events from the specified group.")
    @Processor(friendlyName = "Group - History")
    @MetaDataScope(GroupCategory.class)
    public List<Message> getGroupHistory(@FriendlyName("Channel ID") @Summary("Group to fetch history for") @MetaDataKeyParam String groupID,
            @Optional @Summary("End of time range of messages to include in results. Leave it blank to select current time.") String latestTimestamp,
            @Optional @Summary("Start of time range of messages to include in results. Leave it blank for timestamp 0") String oldestTimestamp,
            @Default("100") @Summary("Number of messages to return, between 1 and 1000.") String mountOfMessages) {
        return slack().groups.getGroupHistory(groupID, latestTimestamp, oldestTimestamp, mountOfMessages);
    }

    /**
     * This processor is used to change the topic of a private group. The calling user must be a member of the private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:set-group-topic}
     *
     * @param channelID ID of the group to set the new topic
     * @param topic     The new topic
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Set topic")
    @MetaDataScope(GroupCategory.class)
    public Boolean setGroupTopic(@MetaDataKeyParam String channelID, String topic) {
        return slack().groups.setGroupTopic(channelID, topic);
    }

    /**
     * This processor is used to change the purpose of a private group. The calling user must be a member of the private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:set-group-purpose}
     *
     * @param channelID ID of the group to set the new purpose
     * @param purpose   The new group purpose
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Set purpose")
    @MetaDataScope(GroupCategory.class)
    public Boolean setGroupPurpose(@MetaDataKeyParam String channelID, String purpose) {
        return slack().groups.setGroupPurpose(channelID, purpose);
    }

    /**
     * This processor creates a private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:create-group}
     *
     * @param groupName Name of the group to create
     * @return If successful, the command returns a group object, including state information.
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Create")
    public Group createGroup(String groupName) {
        return slack().groups.createGroup(groupName);
    }

    /**
     * This processor closes a private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:close-group}
     *
     * @param channelID ID of the group to close
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Close")
    @MetaDataScope(GroupCategory.class)
    public Boolean closeGroup(@MetaDataKeyParam String channelID) {
        return slack().groups.closeGroup(channelID);
    }

    /**
     * This processor opens a private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:open-group}
     *
     * @param channelID ID of the group to open
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Open")
    @MetaDataScope(GroupCategory.class)
    public Boolean openGroup(@MetaDataKeyParam String channelID) {
        return slack().groups.openGroup(channelID);
    }

    /**
     * This processor archives a private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:archive-group}
     *
     * @param channelID ID of the group to archive
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Archive")
    @MetaDataScope(GroupCategory.class)
    public Boolean archiveGroup(@MetaDataKeyParam String channelID) {
        return slack().groups.archiveGroup(channelID);
    }

    /**
     * This processor unarchives a private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:unarchive-group}
     *
     * @param channelID ID of the group to unarchive
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Unarchive")
    @MetaDataScope(GroupCategory.class)
    public Boolean unarchiveGroup(@MetaDataKeyParam String channelID) {
        return slack().groups.unarchiveGroup(channelID);
    }

    /**
     * This processor renames a private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:rename-group}
     *
     * @param groupId   ID of the group to rename
     * @param groupName Channel
     * @return Group
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Rename")
    @MetaDataScope(GroupCategory.class)
    public Group renameGroup(@Summary("Group to rename") @FriendlyName("Group ID") @MetaDataKeyParam String groupId, @Summary("New name for group") String groupName) {
        return slack().groups.renameGroup(groupId, groupName);
    }

    /**
     * This processor returns information about a private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:get-group-info}
     *
     * @param channelId ID of the group to get info on
     * @return Group
     */
    @OAuthProtected
    @Summary("This processor returns information about a private group.")
    @Processor(friendlyName = "Group - Info")
    @MetaDataScope(GroupCategory.class)
    public Group getGroupInfo(@Summary("Group to get info on") @FriendlyName("Group ID") @MetaDataKeyParam String channelId) {
        return slack().groups.getGroupInfo(channelId);
    }

    /**
     * This processor is used to leave a private group.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:leave-group}
     *
     * @param channelId ID of the group to leave
     * @return Boolean result
     */
    @OAuthProtected
    @Processor(friendlyName = "Group - Leave")
    @MetaDataScope(GroupCategory.class)
    public Boolean leaveGroup(@FriendlyName("Group ID") @MetaDataKeyParam String channelId) {
        return slack().groups.leaveGroup(channelId);
    }

    //***********
    // Files methods
    //***********

    /**
     * This processor allows you to create or upload an existing file.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:upload-file}
     *
     * @param channelID      ID of the channel to upload a file
     * @param fileName       File name
     * @param fileType       File type
     * @param title          Message title
     * @param initialComment Message initial comment
     * @param filePath       Path of the file to upload
     * @return File Upload Response
     * @throws IOException When the selected file doesn't exist
     */
    @OAuthProtected
    @Processor(friendlyName = "File - Upload")
    @MetaDataScope(AllChannelCategory.class)
    public FileUploadResponse uploadFile(@FriendlyName("Channel ID") @MetaDataKeyParam String channelID, @Optional String fileName, @Optional String fileType,
            @Optional String title, @Optional String initialComment, @Summary("File path of the file to upload") @Path String filePath) throws IOException {
        return slack().files.sendFile(channelID, fileName, fileType, title, initialComment, filePath);
    }

    /**
     * This processor allows you to create or upload an existing file.
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:upload-file-as-input-streams}
     *
     * @param channelID      ID of the channel to upload a file
     * @param fileName       File name
     * @param fileType       File type
     * @param title          Message title
     * @param initialComment Message initial comment
     * @param inputStream    Input stream of the file to upload
     * @return File upload Response
     * @throws IOException When the selected inputStream doesn't exist
     */
    @OAuthProtected
    @Processor(friendlyName = "File - Upload as Input Stream")
    @MetaDataScope(AllChannelCategory.class)
    public FileUploadResponse uploadFileAsInputStreams(@Summary("Channel ID to send the message") @FriendlyName("Channel ID") @MetaDataKeyParam String channelID,
            @Summary("File name to show in the Slack message") @Optional String fileName, @Optional String fileType, @Summary("Message title") @Optional String title,
            @Optional String initialComment, @Summary("Input Stream Reference of where to look the file to upload") @Default("#[payload]") InputStream inputStream)
            throws IOException {
        return slack().files.sendFile(channelID, fileName, fileType, title, initialComment, inputStream);
    }

    //******************
    // UserGroups methods
    //******************

    /**
     * This method returns a list of all User Groups in the team. This can optionally include disabled User Groups.
     *
     * @param includeDisabled Include disabled user groups
     * @param includeCount    Include the number of user in each user group
     * @param includeUsers    Include the list of user for each user group
     * @return The list of User Groups in the team
     */
    @OAuthProtected
    @Processor(friendlyName = "UserGroups - List")
    public List<Usergroup> listUserGroups(@Summary("Include disabled user groups.") @Default("#[false]") Boolean includeDisabled,
            @Summary("Include the number of user in each user group.") @Default("#[false]") Boolean includeCount,
            @Summary("Include the list of user for each user group.") @Default("#[false]") Boolean includeUsers) {
        return slack().usergroups.listUserGroups(includeDisabled, includeCount, includeUsers);
    }

    /**
     * This method enables a User Group which was previously disabled.
     *
     * @param userGroupId  The encoded ID of the User Group to enable
     * @param includeCount Include the number of user in each user group.
     * @return the enabled User Group
     */
    @OAuthProtected
    @Processor(friendlyName = "UserGroups - Enable")
    public Usergroup enableUserGroup(@Summary("The encoded ID of the User Group to enable.") String userGroupId,
            @Summary("Include the number of user in each user group.") @Default("#[false]") Boolean includeCount) {
        return slack().usergroups.enableUserGroup(userGroupId, includeCount);
    }

    /**
     * This method disables a User Group
     *
     * @param userGroupId  The encoded ID of the User Group to disable
     * @param includeCount Include the number of user in each user group.
     * @return the disabled User Group
     */
    @OAuthProtected
    @Processor(friendlyName = "UserGroups - Disable")
    public Usergroup disableUserGroup(@Summary("The encoded ID of the User Group to disable.") String userGroupId,
            @Summary("Include the number of user in each user group.") @Default("#[false]") Boolean includeCount) {
        return slack().usergroups.disableUserGroup(userGroupId, includeCount);
    }

    /**
     * Create a User Group
     *
     * @param userGroupName A name for the User Group. Must be unique among User Groups.
     * @param handle        A mention handle. Must be unique among channels, user and User Groups
     * @param description   A short description of the User Group
     * @param channels      A list channel IDs for which the User Group uses as a default
     * @param includeCount  Include the number of user in each user group
     * @return the created User Group
     */
    @OAuthProtected
    @Processor(friendlyName = "UserGroups - Create")
    public Usergroup createUserGroup(@Summary("A name for the User Group. Must be unique among User Groups.") String userGroupName,
            @Optional @Summary("A mention handle. Must be unique among channels, user and User Groups.") String handle,
            @Optional @Summary("A short description of the User Group.") String description,
            @Optional @Summary("A list channel IDs for which the User Group uses as a default.") List<String> channels,
            @Summary("Include the number of user in each user group.") @Default("#[false]") Boolean includeCount) {
        return slack().usergroups.createUserGroup(userGroupName, handle, description, channels, includeCount);
    }

    /**
     * Update an existing User Group
     *
     * @param userGroupId   The ID of the User Group
     * @param userGroupName A name for the User Group. Must be unique among User Groups.
     * @param handle        A mention handle. Must be unique among channels, user and User Groups
     * @param description   A short description of the User Group
     * @param channels      A list channel IDs for which the User Group uses as a default
     * @param includeCount  Include the number of user in each user group
     * @return the updated User Group
     */
    @OAuthProtected
    @Processor(friendlyName = "UserGroups - Update")
    public Usergroup updateUserGroup(@Summary("The ID of the User Group") String userGroupId,
            @Optional @Summary("A name for the User Group. Must be unique among User Groups.") String userGroupName,
            @Optional @Summary("A mention handle. Must be unique among channels, user and User Groups.") String handle,
            @Optional @Summary("A short description of the User Group.") String description,
            @Optional @Summary("A list channel IDs for which the User Group uses as a default.") List<String> channels,
            @Summary("Include the number of user in each user group.") @Default("#[false]") Boolean includeCount) {
        return slack().usergroups.updateUserGroup(userGroupId, userGroupName, handle, description, channels, includeCount);
    }

    /**
     * Update an existing User Group
     *
     * @param userGroupId     The ID of the User Group."
     * @param includeDisabled Include disabled users
     * @return a list with all the Users id of the UserGroup
     */
    @OAuthProtected
    @Processor(friendlyName = "UserGroups - Users - List")
    public List<String> listUsersFromUserGroup(@Summary("The ID of the User Group.") String userGroupId,
            @Summary("Include the number of user in each user group.") @Default("#[false]") Boolean includeDisabled) {
        return slack().usergroups.listUsersFromUserGroup(userGroupId, includeDisabled);
    }

    /**
     * This operation updates the list of users that belong to a User Group.
     * This operation replaces all users in a User Group with the list of users provided in the users parameter.
     *
     * @param userGroupId  The ID of the User Group
     * @param users        A string list of user IDs that represent the entire list of users for the User Group.
     * @param includeCount Include disabled users
     * @return the updated UserGroup
     */
    @OAuthProtected
    @Processor(friendlyName = "UserGroups - Users - Update")
    public Usergroup updateUsersFromUserGroup(@Summary("The ID of the User Group.") String userGroupId,
            @Summary("A string list of user IDs that represent the entire list of users for the User Group.") List<String> users,
            @Summary("Include the number of user in each user group.") @Default("#[false]") Boolean includeCount) {
        return slack().usergroups.updateUsersFromUserGroup(userGroupId, users, includeCount);
    }

    //************
    // Source methods
    //************

    /**
     * gdgdf
     *
     * @param sourceCallback    gdf
     * @param messages          gdf
     * @param userTyping        gdf
     * @param directMessages    gfd
     * @param onlyNewMessages   gdf
     * @param ignoreSelfEvents  gfd
     * @param imCreated         gfd
     * @param fileCreated       gdf
     * @param fileShared        gfd
     * @param filePublic        gfd
     * @param allEvents         dfg
     * @param filterClassName   dfg
     * @param notifierClassName dfg
     * @throws IOException          dfg
     * @throws InterruptedException dfg
     * @throws DeploymentException  dfg
     */
    @Source(friendlyName = "Retrieve events")
    public void retrieveEvents(final SourceCallback sourceCallback, @Placement(group = "Events to accept") @Default("#[false]") Boolean messages,
            @Placement(group = "Events to accept") @Default("#[false]") Boolean userTyping,
            @Placement(group = "Events Filters") @FriendlyName(value = "Only Direct Messages") @Default("#[false]") Boolean directMessages,
            @Placement(group = "Events Filters") @FriendlyName(value = "Only New Messages") @Default("#[false]") Boolean onlyNewMessages,
            @Placement(group = "Events Filters") @Default("#[false]") Boolean ignoreSelfEvents, @Placement(group = "Events to accept") @Default("#[false]") Boolean imCreated,
            @Placement(group = "Events to accept") @Default("#[false]") Boolean fileCreated, @Placement(group = "Events to accept") @Default("#[false]") Boolean fileShared,
            @Placement(group = "Events to accept") @Default("#[false]") Boolean filePublic, @Placement(group = "Events to accept") @Default("#[false]") Boolean allEvents,
            @Placement(group = "Custom Filter", tab = "Advanced") @Summary("You can refer an external class to work as a custom filter. (This class must implement 'org.mule.modules.slack.client.rtm.filter.EventFilter')") @Optional String filterClassName,
            @Placement(group = "Custom Notifier", tab = "Advanced") @Summary("You can refer an external class to work as a custom notifier. (This class must implement 'org.mule.modules.slack.client.rtm.filter.EventNotifier')") @Optional String notifierClassName)
            throws IOException, InterruptedException, DeploymentException {

        if (getSlackConfig() instanceof SlackOAuth2Config) {
            logger.error("Retrieve Events source doesn't work with OAuth 2 configuration, please use Token Config");
            logger.error("Shutting down Retrieving of Messages!");
            return;
        }

        List<EventNotifier> observerList = new ArrayList<>();
        List<EventFilter> eventFilterList = new ArrayList<>();

        if (messages) {
            observerList.add(new MessagesNotifier(directMessages, onlyNewMessages));
        }

        if (userTyping) {
            observerList.add(new OnlyTypeNotifier(USER_TYPING_EVENT));
        }

        if (imCreated) {
            observerList.add(new OnlyTypeNotifier(IM_CREATED));
        }

        if (fileCreated) {
            observerList.add(new OnlyTypeNotifier(FILE_CREATED));
        }

        if (fileShared) {
            observerList.add(new OnlyTypeNotifier(FILE_SHARED));
        }

        if (filePublic) {
            observerList.add(new OnlyTypeNotifier(FILE_PUBLIC));
        }

        if (allEvents) {
            observerList.add(new AllEventNotifier());
        }

        if (StringUtils.isNotEmpty(filterClassName)) {
            eventFilterList.add(getFilterInstance(filterClassName));
        }

        if (StringUtils.isNotEmpty(notifierClassName)) {
            observerList.add(getNotifierInstance(notifierClassName));
        }

        if (ignoreSelfEvents) {
            eventFilterList.add(new SelfEventsFilter(slack().auth.getSelfId()));
        }

        slack().startRealTimeCommunication(new ConfigurableHandler(sourceCallback, observerList, eventFilterList));
    }

    private EventFilter getFilterInstance(String className) {
        try {
            logger.info("Detected custom filter class: " + className);
            Class<?> aClass = Class.forName(className, true, getMuleContext().getExecutionClassLoader());
            return (EventFilter) aClass.newInstance();
        } catch (ClassCastException e) {
            String errorMessage = String.format("The configured class [%s] does not implements 'org.mule.modules.slack.client.rtm.filter.SlackEventFilter'", className);
            logger.error(errorMessage);
            throw new SlackException(errorMessage);
        } catch (Exception e) {
            logger.error("Error loading Custom filter class", e);
            throw new SlackException("Error loading Custom filter class", e);
        }
    }

    private EventNotifier getNotifierInstance(String className) {
        try {
            logger.info("Detected custom filter class: " + className);
            Class<?> aClass = Class.forName(className, true, getMuleContext().getExecutionClassLoader());
            return (EventNotifier) aClass.newInstance();
        } catch (ClassCastException e) {
            String errorMessage = String.format("The configured class [%s] does not implements 'org.mule.modules.slack.client.rtm.filter.EventObserver'", className);
            logger.error(errorMessage);
            throw new SlackException(errorMessage);
        } catch (Exception e) {
            logger.error("Error loading Custom filter class", e);
            throw new SlackException("Error loading Custom filter class", e);
        }
    }

    //    @Processor
    public Object slackUserScopeCache(String userId, NestedProcessor nestedProcessor, MuleEvent muleEvent,
            @Placement(tab = "Advanced") @Optional ObjectStore<Serializable> objectStore) throws Exception {

        Object process;
        SlackStorage slackStorage;

        final Set<String> pareExistentFlowVars = new HashSet<>();

        for (String flowVarName : muleEvent.getFlowVariableNames()) {
            pareExistentFlowVars.add(flowVarName);
        }

        if (objectStore != null) {
            slackStorage = new ObjectStoreStorage(objectStore);
        } else {
            slackStorage = new ObjectStoreStorage(muleContext);
        }

        for (Tuple<String, Object> flowVar : slackStorage.restoreFlowVars(userId)) {
            muleEvent.setFlowVariable(flowVar.getLeft(), flowVar.getRight());
        }

        muleEvent.setFlowVariable("rootMessage", muleEvent.getMessage());

        process = nestedProcessor.process(muleEvent.getMessage().getPayload());

        final ArrayList<Tuple<String, Object>> newFlowVars = new ArrayList<>();

        muleEvent.removeFlowVariable("rootMessage");

        for (String flowVarName : muleEvent.getFlowVariableNames()) {
            if (!pareExistentFlowVars.contains(flowVarName)) {
                newFlowVars.add(new Tuple<>(flowVarName, muleEvent.getFlowVariable(flowVarName)));
                muleEvent.removeFlowVariable(flowVarName);
            }
        }

        slackStorage.storageFlowVars(userId, newFlowVars);

        return process;
    }

    private Boolean falseIfNull(Boolean aBoolean) {
        if (aBoolean == null) {
            return false;
        } else {
            return aBoolean;
        }
    }

    /**
     * This Source retrieves messages from the desired channel, private group or direct message channel
     * <p/>
     * {@sample.xml ../../../doc/slack-connector.xml.sample
     * slack:retrieve-messages}
     *
     * @param source                   SourceCallback
     * @param messageRetrieverInterval Pool interval
     * @param channelID                ID of the channel/group/DMC to poll messages
     * @return Messages
     * @throws Exception When the SourceCallback fails processing the messages
     */
    @Source(friendlyName = "Retrieve messages (DEPRECATED)")
    public Message retrieveMessages(SourceCallback source, Integer messageRetrieverInterval,
            @Summary("This source stream messages/events from the specified channel, group or direct message channel") @FriendlyName("Channel ID") String channelID)
            throws Exception {
        String oldestTimeStamp;

        if (getSlackConfig() instanceof SlackOAuth2Config) {
            logger.error("Retrieve Messages source doesn't work with OAuth 2 configuration, please use Token Config");
            logger.error("Shutting down Retrieving of Messages!");
            return new Message();
        }

        logger.warn("This Retrieve Messages Endpoint is deprecated. Please use 'Retrieve Events' endpoint.");

        while (!getSlackConfig().isAuthorized()) {
            Thread.sleep(1000);
            logger.debug("Waiting authorization!");
        }

        MessageRetriever messageRetriever = getMessageVerifierForChannel(channelID);

        oldestTimeStamp = messageRetriever.retrieve(slack(), channelID, null, null, "1").get(0).getTs();

        while (true) {
            Thread.sleep(messageRetrieverInterval);
            List<Message> messages = messageRetriever.retrieve(slack(), channelID, null, oldestTimeStamp, "1000");
            if (!messages.isEmpty()) {
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
        if (channelID.toLowerCase().startsWith("g")) {
            logger.info("Started retrieving messages of channel: " + slack().groups.getGroupInfo(channelID).getName() + "!");
            return new GroupMessageRetriever();
        }

        if (channelID.toLowerCase().startsWith("c")) {
            logger.info("Started retrieving messages of channel: " + slack().channels.getChannelById(channelID).getName() + "!");
            return new ChannelMessageRetriever();
        }

        if (channelID.toLowerCase().startsWith("d")) {
            logger.info("Started retrieving messages of direct message channel!");
            return new DirectMessageRetriever();
        }

        throw new Exception("Incorrect name for channel");
    }

    public SlackClient slack() {
        return slackConfig.getSlackClient();
    }

    public BasicSlackConfig getSlackConfig() {
        return slackConfig;
    }

    public void setSlackConfig(BasicSlackConfig slackConfig) {
        this.slackConfig = slackConfig;
    }

    public MuleContext getMuleContext() {
        return muleContext;
    }

    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }
}