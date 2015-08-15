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
        ArchiveAndUnArchiveChannelTestCases.class,
        ArchiveAndUnArchiveGroupTestCases.class,
        CloseAndOpenGroupTestCases.class,
        CloseDirectMessageChannelTestCases.class,
        GetChannelByNameTestCases.class,
        GetChannelHistoryTestCases.class,
        GetChannelInfoTestCases.class,
        GetGroupListTestCases.class,
        GetUserInfoByNameTestCases.class,
        GetUserListTestCases.class,
        GetUserInfoTestCases.class,
        LeaveAndJoinChannelTestCases.class,
        ListChannelsTestCases.class,
        ListDirectMessageChannelTestCases.class,
        OpenDirectMessageChannelTestCases.class,
        PostMessageTestCases.class,
        PostMessageWithAttachmentTestCases.class,
        RenameChannelTestCases.class,
        RenameGroupTestCases.class,
        SetChannelPurposeTestCases.class,
        SetChannelTopicTestCases.class,
        SetGroupPurposeTestCases.class,
        SetGroupTopicTestCases.class,
        UpdateAndDeleteMessageTestCases.class,
        UploadFileAsInputStreamTestCases.class
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