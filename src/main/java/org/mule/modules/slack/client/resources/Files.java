package org.mule.modules.slack.client.resources;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.file.FileUploadResponse;

import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Files {

    private final SlackRequester slackRequester;
    private final Gson gson;

    public Files(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    //TODO -- Delete duplicated code
    public FileUploadResponse sendFile(String channelId, String fileName, String fileType, String title, String initialComment, InputStream file) {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.FILES_UPLOAD)
                .queryParam("channels", channelId)
                .queryParam("filename", fileName)
                .queryParam("filetype", fileType)
                .queryParam("title", title)
                .queryParam("initial_comment", initialComment);

        String stringResponse = SlackRequester.sendRequestWithFile(webTarget, file);

        return gson.fromJson(new JSONObject(stringResponse).getJSONObject("file").toString(), FileUploadResponse.class);
    }

    public FileUploadResponse sendFile(String channelId, String fileName, String fileType, String title, String initialComment, String filePath) throws IOException {
        WebTarget webTarget = slackRequester.getWebTarget()
                .path(Operations.FILES_UPLOAD)
                .queryParam("channels", channelId)
                .queryParam("filename", fileName)
                .queryParam("filetype", fileType)
                .queryParam("title", title)
                .queryParam("initial_comment", initialComment);

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File " + file.getAbsolutePath() + " does not exist!");
        }

        String stringResponse = SlackRequester.sendAttachmentRequest(webTarget, file);

        return gson.fromJson(new JSONObject(stringResponse).getJSONObject("file").toString(), FileUploadResponse.class);
    }


}
