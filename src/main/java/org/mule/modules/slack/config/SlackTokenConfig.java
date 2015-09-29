/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.config;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.*;
import org.mule.api.annotations.components.Configuration;
import org.mule.modules.slack.client.SlackClient;

/**
 * Connection Management Strategy
 *
 * @author Esteban Wasinger.
 */
@Configuration(configElementName = "config-type", friendlyName = "Token Configuration")
public class SlackTokenConfig implements BasicSlackConfig
{
    SlackClient slack;

    @Configurable
    String accessToken;

    /**
     *
     * @throws ConnectionException
     */
    @TestConnectivity
    public void connect()
        throws ConnectionException {
        slack = new SlackClient(accessToken);
        if(!slack.isConnected()){
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS,"Invalid Token", "Invalid Token");
        }
    }

    public SlackClient getSlackClient() {
        return slack;
    }

    public String getToken() {
        return accessToken;
    }

    public void setToken(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Boolean isAuthorized() {
        return accessToken != null;
    }
}