package org.mule.modules.slack.runner;

import org.mule.modules.slack.SlackConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import java.util.Date;

public abstract class AbstractSlackTestCase extends AbstractTestCase<SlackConnector> {

    public static final String TEST_MESSAGE = "test message";
    public static final String USER_ID = "U03NE28RL";
    public static final String GROUP_ID = "G03R7DHNY";
    public static final String CHANNEL_ID = "C03NE28RY";
    public static final String CHANNEL_RENAMING ="C04KG7XAM";
    public static final String CHANNEL_NAME = "random";
    public static final String ESTEBANWASINGER = "estebanwasinger";
    public static final String DM_CHANNEL_ID = "D03NE28RN";

    public AbstractSlackTestCase() {
        super(SlackConnector.class);
    }

    protected String getDateString() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }

}
