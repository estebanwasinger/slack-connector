/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.retrievers;


import org.mule.modules.slack.client.SlackClient;
import org.mule.modules.slack.client.model.chat.Message;

import java.util.List;

public class ChannelMessageRetriever implements MessageRetriever {

    public List<Message> retrieve(SlackClient slackClient, String channelId, String latestTimestamp, String oldestTimestamp, String mountOfMessages) {
        return slackClient.channels.getChannelHistory(channelId,latestTimestamp,oldestTimestamp,mountOfMessages);
    }
}
