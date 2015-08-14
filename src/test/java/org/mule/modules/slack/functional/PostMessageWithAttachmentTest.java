package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.modules.slack.client.model.chat.attachment.Field;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class PostMessageWithAttachmentTest extends AbstractSlackTestCase {

    @Test
    public void testPostMessageWithAttachments() {
        ChatAttachment chatAttachment = new ChatAttachment();
        chatAttachment.setText("myText");
        chatAttachment.setTitle("myTitle");
        Field field = new Field();
        field.setTitle("myOtherTitle");
        field.setValue("myOtherValue");
        MessageResponse response = getConnector().postMessageWithAttachment(TEST_MESSAGE,CHANNEL_ID,null,null,chatAttachment,field,null);
    }

}
