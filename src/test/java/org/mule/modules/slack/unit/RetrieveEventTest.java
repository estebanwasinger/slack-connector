package org.mule.modules.slack.unit;

import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.slack.SlackConnector;
import org.mule.modules.slack.config.SlackTokenConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

import static org.mockito.Mockito.*;

public class RetrieveEventTest {

    SourceCallback callback;
    SlackConnector connector;
    private static final int THREAD_TIMEOUT_SECONDS = 10;
    private static final String AUTOMATION_CREDENTIALS = "automation-credentials.properties";

    @Before
    public void setUp() throws Exception {

        Properties properties = readAutomationCredentialsFromFile(AUTOMATION_CREDENTIALS);

        connector = new SlackConnector();
        SlackTokenConfig tokenConfig = new SlackTokenConfig();
        tokenConfig.connect(properties.getProperty("config-type.accessToken"));
        connector.setSlackConfig(tokenConfig);

        callback = mock(SourceCallback.class);
        when(callback.process(anyObject())).then(new Answer<Object>() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                System.out.println(invocationOnMock.getArguments()[0]);
                return invocationOnMock.getArguments()[0];
            }
        });
    }

    @Test
    public void testRetrieveEvents() throws Exception {

        doReturn(callback).when(callback).process(any(Map.class));

        Callable<Void> callable = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                connector.retrieveEvents(callback, true, false, true, true, false);
                return null;
            }
        };
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Future<Void> future = singleThreadExecutor.submit(callable);
        singleThreadExecutor.shutdown();
        Thread.sleep(8000);
        connector.postMessage("Test","D08ET3WF5",null,null,true);
        singleThreadExecutor.awaitTermination(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);


        verify(callback, times(1));


    }

    private static Properties readAutomationCredentialsFromFile(String automationCredentialsFile) {
        Properties properties = new Properties();
        try (final InputStream input = RetrieveEventTest.class.getResourceAsStream("/" + automationCredentialsFile)) {
            Validate.notNull(input, String.format("%s could not be found", automationCredentialsFile));
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can not load automation credentials file: %s", automationCredentialsFile), e);
        }
        return properties;
    }


}
