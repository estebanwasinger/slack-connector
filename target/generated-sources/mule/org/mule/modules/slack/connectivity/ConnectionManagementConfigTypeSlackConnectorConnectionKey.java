
package org.mule.modules.slack.connectivity;

import javax.annotation.Generated;
import org.mule.devkit.shade.connection.management.ConnectionManagementConnectionKey;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class ConnectionManagementConfigTypeSlackConnectorConnectionKey implements ConnectionManagementConnectionKey
{

    /**
     * 
     */
    private String accessToken;

    public ConnectionManagementConfigTypeSlackConnectorConnectionKey(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Sets accessToken
     * 
     * @param value Value to set
     */
    public void setAccessToken(String value) {
        this.accessToken = value;
    }

    /**
     * Retrieves accessToken
     * 
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public int hashCode() {
        int result = ((this.accessToken!= null)?this.accessToken.hashCode(): 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConnectionManagementConfigTypeSlackConnectorConnectionKey)) {
            return false;
        }
        ConnectionManagementConfigTypeSlackConnectorConnectionKey that = ((ConnectionManagementConfigTypeSlackConnectorConnectionKey) o);
        if (((this.accessToken!= null)?(!this.accessToken.equals(that.accessToken)):(that.accessToken!= null))) {
            return false;
        }
        return true;
    }

}
