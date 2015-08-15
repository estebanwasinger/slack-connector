package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import java.io.IOException;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class OpenDirectMessageChannelTestCases extends AbstractSlackTestCase {

    @Test
    public void testOpenDMChannel() throws IOException {
        getConnector().openDirectMessageChannel(USER_ID);
    }

}
