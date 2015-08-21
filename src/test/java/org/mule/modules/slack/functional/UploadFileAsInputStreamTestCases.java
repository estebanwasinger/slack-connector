package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.file.FileUploadResponse;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class UploadFileAsInputStreamTestCases extends AbstractSlackTestCase {

    @Test
    public void testUploadFileAsInputStream() throws IOException {
        String text = "Text as inputStream";
        InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        FileUploadResponse fileUploadResponse = getConnector().uploadFileAsInputStreams(GROUP_ID,null,null,null,null,stream);
        assertEquals(text,fileUploadResponse.getPreview());
    }

}
