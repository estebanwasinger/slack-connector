/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client;

import org.json.JSONObject;
import org.mule.modules.slack.client.exceptions.SlackException;

/**
 * Created by estebanwasinger on 2/20/15.
 */
public class ErrorHandler {

    private ErrorHandler() {
        return;
    }

    public static void verifyResponse(String response) {
        if (isValidResponse(response)) {
            return;
        }

        String errorCause = new JSONObject(response).getString("error");

        throw new SlackException("Error: " + errorCause);
    }

    private static boolean isValidResponse(String response) {
        return new JSONObject(response).getBoolean("ok");
    }
}
