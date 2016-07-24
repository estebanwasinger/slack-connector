/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.chat.Message;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class GetChannelHistoryTestCases extends AbstractSlackTestCase {

    @Test
    public void testGetChannelHistory() {
        String newMessage = "My new message";
        getConnector().postMessage(newMessage,CHANNEL_ID,null,null,true);
        Message message = getConnector().getChannelHistory(CHANNEL_ID, null, null, "1").get(0);
        assertEquals(newMessage,message.getText());
    }

}
