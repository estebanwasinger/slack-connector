package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.exceptions.UserNotFoundException;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class  RenameChannelTestCases extends AbstractSlackTestCase {

    @Test
    public void testRenameChannel() throws UserNotFoundException, InterruptedException {
        Thread.sleep(2000);
        String actualDate = getDateString();
        getConnector().renameChannel(CHANNEL_RENAMING, actualDate);
        assertEquals(actualDate,getConnector().getChannelInfo(CHANNEL_RENAMING).getName());
    }

}
