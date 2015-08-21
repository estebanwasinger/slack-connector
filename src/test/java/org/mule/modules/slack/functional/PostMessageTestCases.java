package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

public class PostMessageTestCases extends AbstractSlackTestCase {

    @Test
    public void testPostMessage(){
        MessageResponse response = getConnector().postMessage(TEST_MESSAGE, CHANNEL_ID, null, null, null);
        assertEquals(TEST_MESSAGE, response.getMessage().getText());
    }

}
