package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class UpdateAndDeleteMessageTest extends AbstractSlackTestCase {

    @Test
    public void testUpdateAndDeleteMessage() {
        MessageResponse response = getConnector().postMessage(TEST_MESSAGE, CHANNEL_ID, null, null, true);
        getConnector().updateMessage(response.getTs(),CHANNEL_ID,"updated message");
        getConnector().deleteMessage(response.getTs(),CHANNEL_ID);
    }

}
