/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;

public class SlackRequester {

    String token;

    public SlackRequester(String token) {
        this.token = token;
    }

    public WebTarget getWebTarget() {
        ClientConfig clientConfig = new ClientConfig().register(MultiPartFeature.class);

        Client client = ClientBuilder.newClient(clientConfig);

        return client.target("https://slack.com/api/").queryParam("token", token);
    }

    public static String sendRequest(WebTarget webTarget) {
        Invocation.Builder invocationBuilder =
                webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        String output = response.readEntity(String.class);
        ErrorHandler.verifyResponse(output);
        return output;
    }

    public static String sendRequestWithFile(WebTarget webTarget, InputStream file) {
        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new StreamDataBodyPart("file", file, null, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        Response response = webTarget.request(MediaType.MULTIPART_FORM_DATA).post(Entity.entity(multiPart, multiPart.getMediaType()));

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.readEntity(String.class);

        ErrorHandler.verifyResponse(output);

        return output;
    }

    public static String sendAttachmentRequest(WebTarget webTarget, File file) {
        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        Response response = webTarget.request(MediaType.MULTIPART_FORM_DATA).post(Entity.entity(multiPart,multiPart.getMediaType()));

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.readEntity(String.class);

        ErrorHandler.verifyResponse(output);

        return output;

    }
}
