package org.mule.modules.slack;

/**
 * Created by estebanwasinger on 4/6/15.
 */
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mule.tools.devkit.ctf.mockup.ConnectorDispatcher;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;
import org.mule.tools.devkit.ctf.platform.PlatformManager;

public class TestParent{

    private SlackConnector connector;
    private ConnectorDispatcher<SlackConnector> dispatcher;


    protected SlackConnector getConnector() {
        return connector;
    }


    protected ConnectorDispatcher<SlackConnector> getDispatcher() {
        return dispatcher;
    }

    @Before
    public void init() throws Exception {

        //Context instance
        ConnectorTestContext<SlackConnector> context = ConnectorTestContext.getInstance(SlackConnector.class);

        //Connector dispatcher
        dispatcher = context.getConnectorDispatcher();

        connector = dispatcher.createMockup();

        setUp();
    }


    protected void setUp() throws Exception{
    }
}