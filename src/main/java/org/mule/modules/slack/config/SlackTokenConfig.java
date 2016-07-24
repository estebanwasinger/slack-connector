/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.config;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.modules.slack.client.SlackClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Connection Management Strategy
 *
 * @author Esteban Wasinger.
 */
@Configuration(configElementName = "config-type", friendlyName = "Token Configuration")
//@ConnectionManagement(configElementName = "config-type", friendlyName = "Token Configuration")
public class SlackTokenConfig implements BasicSlackConfig {

    Map<String, Map<String, Object>> userMap;

    public SlackTokenConfig(){
        userMap = new HashMap<>();
    }

    SlackClient slack;

    @Configurable
    String accessToken;

    @Start
    public void init(){
        System.out.println("tu mama");
    }

    /**
     * @throws ConnectionException
     */
    @TestConnectivity
//    @Connect
//    public void connect(@ConnectionKey String keyee) throws ConnectionException {
    public void connect() throws ConnectionException {
        SlackClient slackTestClient = new SlackClient(accessToken);
        if (!slackTestClient.auth.isConnected()) {
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, "Invalid Token", "Invalid Token");
        }
    }

//    @Disconnect
    public void dds(){

    }

//    @ValidateConnection
    public boolean isValiod(){
        return true;
    }

//    @ConnectionIdentifier
    public String sss(){
        return "dsad";
    }
    public SlackClient getSlackClient() {
        return slack;
    }

    public String getToken() {
        return accessToken;
    }

    public void setToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        slack = new SlackClient(accessToken);
        this.accessToken = accessToken;
    }

    public Boolean isAuthorized() {
        return accessToken != null;
    }

    @Override
    public Map<String, Map<String, Object>> getUserMap() {
        return userMap;
    }
}