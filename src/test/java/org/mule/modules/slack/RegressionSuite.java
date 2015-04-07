package org.mule.modules.slack;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mule.modules.slack.testcases.SlackTestCases;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;
import org.mule.tools.devkit.ctf.platform.PlatformManager;

/**
 * Created by estebanwasinger on 4/6/15.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(RegressionSuite.class)
@Suite.SuiteClasses({
        SlackTestCases.class
})
public class RegressionSuite {

    @BeforeClass
    public static void initialiseSuite(){

        ConnectorTestContext.initialize(SlackConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() throws Exception{

        ConnectorTestContext<SlackConnector> context = ConnectorTestContext.getInstance(SlackConnector.class);

        PlatformManager platform =  context.getPlatformManager();

        platform.shutdown();
    }
}