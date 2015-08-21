package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class CloseDirectMessageChannelTestCases extends AbstractSlackTestCase {

    @Test
    public void testCloseDMChannel() throws IOException {
        Boolean aBoolean = getConnector().closeDirectMessageChannel(DM_CHANNEL_ID);
        assertEquals(true, aBoolean);
    }

}
