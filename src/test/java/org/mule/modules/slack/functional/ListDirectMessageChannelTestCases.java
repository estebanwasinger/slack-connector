package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.im.DirectMessageChannel;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class ListDirectMessageChannelTestCases extends AbstractSlackTestCase {

    @Test
    public void testGetDirectMessageChannelList() {
        Boolean found = false;
        for (DirectMessageChannel directMessageChannel : getConnector().listDirectMessageChannels()) {
            if(directMessageChannel.getUser().equals("USLACKBOT")) found = true;
        }

        assertEquals(true,found);
    }

}
