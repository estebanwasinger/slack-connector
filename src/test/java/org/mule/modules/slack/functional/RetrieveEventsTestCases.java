/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
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
