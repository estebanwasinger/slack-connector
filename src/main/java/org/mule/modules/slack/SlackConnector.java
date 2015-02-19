/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack;

import org.mule.api.annotations.*;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.callback.SourceCallback;
import org.mule.common.metadata.*;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.slack.strategy.SlackConnectionStrategy;
import org.stevew.SlackClient;
import org.stevew.exceptions.UserNotFoundException;
import org.stevew.model.User;
import org.stevew.model.channel.Channel;
import org.stevew.model.channel.Message;
import org.stevew.model.chat.MessageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Slack Anypoint Connector
 *
 * @author Esteban Wasinger
 */
@Connector(name = "slack", friendlyName = "Slack")
public class SlackConnector {

    @ConnectionStrategy
    SlackConnectionStrategy connectionStrategy;

    @OAuthProtected
    @Processor
    public User getUserInfo(@FriendlyName("User ID") String id) {
        return slack().getUserInfo(id);
    }

    @OAuthProtected
    @Processor
    public List<Channel> getChannelList() {
        return slack().getChannelList();
    }

    @OAuthProtected
    @Processor
    public List<Message> getChannelHistory(@FriendlyName("Channel ID") String channelId, String latestTimestamp, String oldestTimestamp, String mountOfMessages) {
        return slack().getChannelHistory(channelId, latestTimestamp, oldestTimestamp, mountOfMessages);
    }



    @OAuthProtected
    @Processor(friendlyName = "Get Channel By ID")
    public Channel getChannelById(@FriendlyName("Channel ID") String channelId) {
        return slack().getChannelById(channelId);
    }

    @OAuthProtected
    @Processor
    public MessageResponse sendMessage(String message, @FriendlyName("Channel ID") String channelId, @FriendlyName("Name to show") String username, @FriendlyName("Icon URL") String iconURL) {
        return slack().sendMessage(message, channelId, username, iconURL);
    }

    @OAuthProtected
    @Processor
    public String deleteMessage(@FriendlyName("Message TimeStamp")String timeStamp, @FriendlyName("Channel ID") String channelId) {
        return slack().deleteMessage(timeStamp, channelId);
    }

    @OAuthProtected
    @Processor
    public String updateMessage(@FriendlyName("Message TimeStamp")String timeStamp, @FriendlyName("Channel ID") String channelId, String message) {
        return slack().updateMessage(timeStamp, channelId, message);
    }
    
    @OAuthProtected
    @Processor
    public Channel createChannel(String channelName){
        return slack().createChannel(channelName);
    }

    @OAuthProtected
    @Processor
    public Channel renameChannel(@FriendlyName("Channel ID") String channelId, String channelName){
        return slack().renameChannel(channelId, channelName);
    }

    @OAuthProtected
    @Processor
    public Channel joinChannel(String channelName){
        return slack().joinChannel(channelName);
    }

    @OAuthProtected
    @Processor
    public Boolean leaveChannel(@FriendlyName("Channel ID") String channelId){
        return slack().leaveChannel(channelId);
    }

    @OAuthProtected
    @Processor
    public String openDirectMessageChannel(@FriendlyName("User ID") String userId){
        return slack().openDirectMessageChannel(userId);
    }

    @OAuthProtected
    @Processor
    @MetaDataScope(ChannelCategory.class)
    public Channel getChannelByName(@MetaDataKeyParam String channelName) {
        return slack().getChannelByName(channelName);
    }

    

    @OAuthProtected
    @Processor
    @MetaDataScope(UserCategory.class)
    public User getUserInfoByName(@MetaDataKeyParam String username) throws UserNotFoundException {
        System.out.println(username);
        return slack().getUserInfoByName(username);
    }

    @OAuthProtected
    @Processor
    public String listDirectMessageChannels(){
        return slack().listDirectMessageChannels();
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