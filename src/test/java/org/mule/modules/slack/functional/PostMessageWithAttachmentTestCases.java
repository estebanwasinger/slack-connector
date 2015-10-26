package org.mule.modules.slack.functional;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mule.modules.slack.client.model.chat.MessageResponse;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.modules.slack.client.model.chat.attachment.Field;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class PostMessageWithAttachmentTestCases extends AbstractSlackTestCase {

    @Test
    public void testPostMessageWithAttachments() {
        ChatAttachment chatAttachment = new ChatAttachment();
        chatAttachment.setText("myText");
        chatAttachment.setTitle("myTitle");
        Field field = new Field();
        field.setTitle("myOtherTitle");
        field.setValue("myOtherValue");
        chatAttachment.setFields(Arrays.asList(field));
        List<ChatAttachment> chatAttachmentList = Arrays.asList(chatAttachment);
        MessageResponse response = getConnector().postMessageWithAttachment(TEST_MESSAGE,CHANNEL_ID,null,null,chatAttachmentList,null);
    }

}
