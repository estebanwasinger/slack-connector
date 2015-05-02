/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by estebanwasinger on 1/4/15.
 */
public class SlackRequest {

    private String operation;
    private Map<String, String> arguments;
    private String token;

    public SlackRequest(String token) {
        arguments = new HashMap<String, String>();
        this.token = token;
    }

    public String getOperation() {
        return operation;
    }

    public SlackRequest setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public SlackRequest addArgument(String argName, String argValue) {
        if (argValue == null) {
            return this;
        }
        try {
            arguments.put(argName, URLEncoder.encode(argValue, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public SlackRequest enablePretty() {
        arguments.put("pretty", "1");
        return this;
    }

    public String createUrl() {
        String url = "https://slack.com/api/" + operation + "?token=" + token;
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            url = url.concat("&" + entry.getKey() + "=" + entry.getValue());
        }
        return url;
    }
}
