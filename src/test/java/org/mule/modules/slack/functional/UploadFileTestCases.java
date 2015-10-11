package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.modules.slack.client.model.file.FileUploadResponse;
import org.mule.modules.slack.runner.AbstractSlackTestCase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class UploadFileTestCases extends AbstractSlackTestCase {

    final String message = "Hello Slack!";

    @Test
    public void testUploadFile() throws IOException {
        File licenseFile = File.createTempFile("slackFile", "slackFile");
        BufferedWriter writer = new BufferedWriter(new FileWriter(licenseFile));
        writer.write(message);
        writer.close();
        FileUploadResponse fileUploadResponse = getConnector().uploadFile(GROUP_ID, null, null, null, null, licenseFile.getAbsolutePath());
        assertEquals(message, fileUploadResponse.getPreview());
    }

}
