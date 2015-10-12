package org.mule.modules.slack.unit;

import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.slack.SlackConnector;
import org.mule.modules.slack.client.model.chat.Message;
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
    private static final String CHANNEL_ID = "D08ET3WF5";

    @Before
    public void setUp() throws Exception {

        Properties properties = readAutomationCredentialsFromFile(AUTOMATION_CREDENTIALS);

        connector = new SlackConnector();
        SlackTokenConfig tokenConfig = new SlackTokenConfig();
        tokenConfig.setAccessToken(properties.getProperty("config-type.accessToken"));
        tokenConfig.connect();
        connector.setSlackConfig(tokenConfig);

        callback = mock(SourceCallback.class);
        when(callback.process(anyObject())).then(new Answer<Object>() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
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
        connector.postMessage("Test", CHANNEL_ID, null, null, true);
        singleThreadExecutor.awaitTermination(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        verify(callback, atLeast(1)).process(any(Map.class));
    }

    @Test
    public void testRetrieveMessages() throws Exception {
        doReturn(callback).when(callback).process(any(Message.class));

        Callable<Void> callable = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                connector.retrieveMessages(callback, 1000, CHANNEL_ID);
                return null;
            }
        };
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Future<Void> future = singleThreadExecutor.submit(callable);
        singleThreadExecutor.shutdown();
        Thread.sleep(3000);
        connector.postMessage("Test", CHANNEL_ID, null, null, true);
        singleThreadExecutor.awaitTermination(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        verify(callback, times(1)).process(any(Message.class));
    }

    private static Properties readAutomationCredentialsFromFile(String automationCredentialsFile) {
        Properties properties = new Properties();
        try (final InputStream input = RetrieveEventTest.class.getResourceAsStream("/" + automationCredentialsFile)) {
            Validate.notNull(input, String.format("%s could not be found", automationCredentialsFile));
            properties.load(input);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(String.format("Can not load automation credentials file: %s . Please place the credentials file in ./slack-connector/src/test/resources folder.", automationCredentialsFile), e);
        }
        return properties;
    }


}
