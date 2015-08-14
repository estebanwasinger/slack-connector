/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mule.modules.slack.SlackConnector;
import org.mule.modules.slack.functional.*;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArchiveAndUnArchiveChannelTest.class,
        ArchiveAndUnArchiveGroupTest.class,
        CloseAndOpenGroupTest.class,
        CloseDirectMessageChannelTest.class,
        GetChannelByNameTest.class,
        GetChannelHistoryTest.class,
        GetChannelInfoTest.class,
        GetGroupListTest.class,
        GetUserInfoByNameTest.class,
        GetUserListTest.class,
        GetUserInfoTest.class,
        LeaveAndJoinChannelTest.class,
        ListChannelsTest.class,
        ListDirectMessageChannelTest.class,
        OpenDirectMessageChannelTest.class,
        PostMessageTest.class,
        PostMessageWithAttachmentTest.class,
        RenameChannelTest.class,
        RenameGroupTest.class,
        SetChannelPurposeTest.class,
        SetChannelTopicTest.class,
        SetGroupPurposeTest.class,
        SetGroupTopicTest.class,
        UpdateAndDeleteMessageTest.class,
        UploadFileAsInputStreamTest.class
})
public class FunctionalTestSuite {

    @BeforeClass
    public static void initialiseSuite() {
        ConnectorTestContext.initialize(SlackConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() throws Exception {
        ConnectorTestContext.shutDown();
    }

}