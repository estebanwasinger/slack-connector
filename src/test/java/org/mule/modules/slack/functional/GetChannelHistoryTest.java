package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class GetChannelHistoryTest extends AbstractSlackTestCase {

    @Test
    public void testGetChannelHistory() {
        String newMessage = "My new message";
        getConnector().postMessage(newMessage,CHANNEL_ID,null,null,true);
        Message message = getConnector().getChannelHistory(CHANNEL_ID, null, null, "1").get(0);
        assertEquals(newMessage,message.getText());
    }

}
