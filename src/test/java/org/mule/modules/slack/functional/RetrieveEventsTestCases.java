package org.mule.modules.slack.functional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import java.util.List;

public class RetrieveEventsTestCases extends AbstractSlackTestCase {

    @Before
    public void setUp() throws Throwable{
        Object[] signature = {null, true, false, false, true, false, false, false, false, false, null, null};
        getDispatcher().initializeSource("retrieveEvents", signature);
    }

    @Test
    public void testSource() throws InterruptedException {
        getConnector().postMessage("Test", CHANNEL_ID, null, null, true);
        Thread.sleep(5000);
        getConnector().postMessage("Test", CHANNEL_ID, null, null, true);
        getConnector().postMessage("Test", CHANNEL_ID, null, null, true);
        getConnector().postMessage("Test", CHANNEL_ID, null, null, true);
        Thread.sleep(5000);
        getConnector().postMessage("Test", CHANNEL_ID, null, null, true);
        getConnector().postMessage("Test", CHANNEL_ID, null, null, true);
        List<Object> events = getDispatcher().getSourceMessages("retrieveEvents");
        System.out.println(events);
    }

    @After
    public void tearDown() throws Throwable{
        getDispatcher().shutDownSource("retrieveEvents");
    }

}
