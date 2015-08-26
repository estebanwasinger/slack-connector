
package org.mule.modules.slack.connectivity;

import javax.annotation.Generated;
import org.mule.api.ConnectionException;
import org.mule.devkit.shade.connection.management.ConnectionManagementConnectionAdapter;
import org.mule.modules.slack.strategy.ConnectionManagementStrategy;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class ConnectionManagementStrategySlackConnectorAdapter
    extends ConnectionManagementStrategy
    implements ConnectionManagementConnectionAdapter<ConnectionManagementStrategy, ConnectionManagementConfigTypeSlackConnectorConnectionKey>
{


    @Override
    public void connect(ConnectionManagementConfigTypeSlackConnectorConnectionKey connectionKey)
        throws ConnectionException
    {
        super.connect(connectionKey.getAccessToken());
    }

    @Override
    public void disconnect() {
        super.disconnect();
    }

    @Override
    public String connectionId() {
        return super.connectionId();
    }

    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public ConnectionManagementStrategy getStrategy() {
        return this;
    }

}
