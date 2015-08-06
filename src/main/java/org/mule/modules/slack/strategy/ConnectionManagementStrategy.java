/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.strategy;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.*;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.modules.slack.client.SlackClient;

/**
 * Connection Management Strategy
 *
 * @author Esteban Wasinger.
 */
@ConnectionManagement(configElementName = "config-type", friendlyName = "Connection Managament type strategy")
public class ConnectionManagementStrategy implements SlackConnectionStrategy
{

    SlackClient slack;
    String accessToken;

    /**
     *
     * @param accessToken
     * @throws ConnectionException
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey String accessToken)
        throws ConnectionException {
        slack = new SlackClient(accessToken);
        if(!slack.isConnected()){
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS,"Invalid Token", "Invalid Token");
        }else{
            this.accessToken = accessToken;
        }
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        /*
         * CODE FOR CLOSING A CONNECTION GOES IN HERE
         */
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
       return slack == null ? false : true;
    }

    /**
     * Are we connected
     */
    @ConnectionIdentifier
    public String connectionId() {
        return "001";
    }


    public SlackClient getSlackClient() {
        return slack;
    }

    public String getToken() {
        return accessToken;
    }


    public Boolean isAuthorized() {
        if(accessToken == null){
            return false;
        } else {
            return true;
        }
    }
}