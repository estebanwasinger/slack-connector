/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Path;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.slack.strategy.SlackConnectionStrategy;
import org.stevew.SlackClient;
import org.stevew.exceptions.UserNotFoundException;
import org.stevew.model.User;
import org.stevew.model.channel.Channel;
import org.stevew.model.channel.Message;
import org.stevew.model.chat.MessageResponse;
import org.stevew.model.im.DirectMessageChannel;
import org.stevew.model.im.DirectMessageChannelCreationResponse;

import java.io.IOException;
import java.util.List;

/**
 * Slack Anypoint Connector
 *
 * @author Esteban Wasinger
 */
@Connector(name = "slack", friendlyName = "Slack")
public class SlackConnector {

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
    public List<User> getUserList(){
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
    @MetaDataScope(ChannelCategory.class)
    public MessageResponse postMessage(String message, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId, @FriendlyName("Name to show") String username, @FriendlyName("Icon URL") String iconURL) {
        return slack().sendMessage(message, channelId, username, iconURL);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(ChannelCategory.class)
    public Boolean deleteMessage(@FriendlyName("Message TimeStamp") String timeStamp, @FriendlyName("Channel ID") @MetaDataKeyParam String channelId) {
        return slack().deleteMessage(timeStamp, channelId);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(ChannelCategory.class)
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
    
    /*@OAuthProtected
    @Processor
    public List<Message> getGroupHistory(){
        slack().getGroupHistory();
    }*/

    //***********
    // Files methods
    //***********
    
    @OAuthProtected
    @Processor
    public String uploadFile(@FriendlyName("Channel ID") String channelID, @Optional String fileName, @Optional String fileType, @Optional String title, @Optional String initialComment,@Summary("File path of the file to upload")@Path String filePath) throws IOException {
     return slack().sendFile(channelID,fileName,fileType,title,initialComment,filePath);
    }

    /*@OAuthProtected
    @Source
    public void retrieveMessages(SourceCallback source, Integer authSleep, Integer messageRetreiveInterval) throws Exception{

        while(!getConnectionStrategy().isAuthorized()){
            Thread.sleep(authSleep);
        }

    	while (true)
        {	
            Thread.sleep(messageRetreiveInterval);
            Message ms = new Message();
            ms.setText(slack().isConnected().toString());
            source.process(ms);
        }
    }*/

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