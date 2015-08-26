
package org.mule.modules.slack.connectivity;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.pool.KeyedObjectPool;
import org.mule.api.MetadataAware;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.api.context.MuleContextAware;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.devkit.capability.Capabilities;
import org.mule.api.devkit.capability.ModuleCapability;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.retry.RetryPolicyTemplate;
import org.mule.common.DefaultResult;
import org.mule.common.DefaultTestResult;
import org.mule.common.Result;
import org.mule.common.TestResult;
import org.mule.common.Testable;
import org.mule.common.metadata.ConnectorMetaDataEnabled;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataFailureType;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.key.property.TypeDescribingProperty;
import org.mule.common.metadata.property.StructureIdentifierMetaDataModelProperty;
import org.mule.config.PoolingProfile;
import org.mule.devkit.processor.ExpressionEvaluatorSupport;
import org.mule.devkit.shade.connection.management.ConnectionManagementConnectionAdapter;
import org.mule.devkit.shade.connection.management.ConnectionManagementConnectionManager;
import org.mule.devkit.shade.connection.management.ConnectionManagementConnectorAdapter;
import org.mule.devkit.shade.connection.management.ConnectionManagementConnectorFactory;
import org.mule.devkit.shade.connection.management.ConnectionManagementProcessTemplate;
import org.mule.devkit.shade.connection.management.UnableToAcquireConnectionException;
import org.mule.devkit.shade.connectivity.ConnectivityTestingErrorHandler;
import org.mule.modules.slack.SlackConnector;
import org.mule.modules.slack.adapters.SlackConnectorConnectionManagementAdapter;
import org.mule.modules.slack.metadata.AllChannelCategory;
import org.mule.modules.slack.metadata.ChannelCategory;
import org.mule.modules.slack.metadata.GroupCategory;
import org.mule.modules.slack.metadata.UserCategory;
import org.mule.modules.slack.pooling.DevkitGenericKeyedObjectPool;
import org.mule.modules.slack.strategy.ConnectionManagementStrategy;


/**
 * A {@code SlackConnectorConfigTypeConnectionManagementConnectionManager} is a wrapper around {@link SlackConnector } that adds connection management capabilities to the pojo.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class SlackConnectorConfigTypeConnectionManagementConnectionManager
    extends ExpressionEvaluatorSupport
    implements MetadataAware, MuleContextAware, ProcessAdapter<SlackConnectorConnectionManagementAdapter> , Capabilities, Disposable, Initialisable, Testable, ConnectorMetaDataEnabled, ConnectionManagementConnectionManager<ConnectionManagementConfigTypeSlackConnectorConnectionKey, SlackConnectorConnectionManagementAdapter, ConnectionManagementStrategy>
{

    /**
     * 
     */
    private String accessToken;
    /**
     * Mule Context
     * 
     */
    protected MuleContext muleContext;
    /**
     * Connector Pool
     * 
     */
    private KeyedObjectPool connectionPool;
    protected PoolingProfile poolingProfile;
    protected RetryPolicyTemplate retryPolicyTemplate;
    private final static String MODULE_NAME = "Slack";
    private final static String MODULE_VERSION = "1.0.0";
    private final static String DEVKIT_VERSION = "3.6.1";
    private final static String DEVKIT_BUILD = "UNNAMED.2405.44720b7";
    private final static String MIN_MULE_VERSION = "3.5.0";

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

    /**
     * Sets muleContext
     * 
     * @param value Value to set
     */
    public void setMuleContext(MuleContext value) {
        this.muleContext = value;
    }

    /**
     * Retrieves muleContext
     * 
     */
    public MuleContext getMuleContext() {
        return this.muleContext;
    }

    /**
     * Sets poolingProfile
     * 
     * @param value Value to set
     */
    public void setPoolingProfile(PoolingProfile value) {
        this.poolingProfile = value;
    }

    /**
     * Retrieves poolingProfile
     * 
     */
    public PoolingProfile getPoolingProfile() {
        return this.poolingProfile;
    }

    /**
     * Sets retryPolicyTemplate
     * 
     * @param value Value to set
     */
    public void setRetryPolicyTemplate(RetryPolicyTemplate value) {
        this.retryPolicyTemplate = value;
    }

    /**
     * Retrieves retryPolicyTemplate
     * 
     */
    public RetryPolicyTemplate getRetryPolicyTemplate() {
        return this.retryPolicyTemplate;
    }

    public void initialise() {
        connectionPool = new DevkitGenericKeyedObjectPool(new ConnectionManagementConnectorFactory(this), poolingProfile);
        if (retryPolicyTemplate == null) {
            retryPolicyTemplate = muleContext.getRegistry().lookupObject(MuleProperties.OBJECT_DEFAULT_RETRY_POLICY_TEMPLATE);
        }
    }

    @Override
    public void dispose() {
        try {
            connectionPool.close();
        } catch (Exception e) {
        }
    }

    public SlackConnectorConnectionManagementAdapter acquireConnection(ConnectionManagementConfigTypeSlackConnectorConnectionKey key)
        throws Exception
    {
        return ((SlackConnectorConnectionManagementAdapter) connectionPool.borrowObject(key));
    }

    public void releaseConnection(ConnectionManagementConfigTypeSlackConnectorConnectionKey key, SlackConnectorConnectionManagementAdapter connection)
        throws Exception
    {
        connectionPool.returnObject(key, connection);
    }

    public void destroyConnection(ConnectionManagementConfigTypeSlackConnectorConnectionKey key, SlackConnectorConnectionManagementAdapter connection)
        throws Exception
    {
        connectionPool.invalidateObject(key, connection);
    }

    /**
     * Returns true if this module implements such capability
     * 
     */
    public boolean isCapableOf(ModuleCapability capability) {
        if (capability == ModuleCapability.LIFECYCLE_CAPABLE) {
            return true;
        }
        if (capability == ModuleCapability.CONNECTION_MANAGEMENT_CAPABLE) {
            return true;
        }
        return false;
    }

    @Override
    public<P >ProcessTemplate<P, SlackConnectorConnectionManagementAdapter> getProcessTemplate() {
        return new ConnectionManagementProcessTemplate(this, muleContext);
    }

    @Override
    public ConnectionManagementConfigTypeSlackConnectorConnectionKey getDefaultConnectionKey() {
        return new ConnectionManagementConfigTypeSlackConnectorConnectionKey(getAccessToken());
    }

    @Override
    public ConnectionManagementConfigTypeSlackConnectorConnectionKey getEvaluatedConnectionKey(MuleEvent event)
        throws Exception
    {
        if (event!= null) {
            final String _transformedAccessToken = ((String) evaluateAndTransform(muleContext, event, this.getClass().getDeclaredField("accessToken").getGenericType(), null, getAccessToken()));
            if (_transformedAccessToken == null) {
                throw new UnableToAcquireConnectionException("Parameter accessToken in method connect can't be null because is not @Optional");
            }
            return new ConnectionManagementConfigTypeSlackConnectorConnectionKey(_transformedAccessToken);
        }
        return getDefaultConnectionKey();
    }

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleVersion() {
        return MODULE_VERSION;
    }

    public String getDevkitVersion() {
        return DEVKIT_VERSION;
    }

    public String getDevkitBuild() {
        return DEVKIT_BUILD;
    }

    public String getMinMuleVersion() {
        return MIN_MULE_VERSION;
    }

    @Override
    public ConnectionManagementConfigTypeSlackConnectorConnectionKey getConnectionKey(MessageProcessor messageProcessor, MuleEvent event)
        throws Exception
    {
        return getEvaluatedConnectionKey(event);
    }

    @Override
    public ConnectionManagementConnectionAdapter newConnection() {
        ConnectionManagementStrategySlackConnectorAdapter connection = new ConnectionManagementStrategySlackConnectorAdapter();
        return connection;
    }

    @Override
    public ConnectionManagementConnectorAdapter newConnector(ConnectionManagementConnectionAdapter<ConnectionManagementStrategy, ConnectionManagementConfigTypeSlackConnectorConnectionKey> connection) {
        SlackConnectorConnectionManagementAdapter connector = new SlackConnectorConnectionManagementAdapter();
        connector.setConnectionStrategy(connection.getStrategy());
        return connector;
    }

    public ConnectionManagementConnectionAdapter getConnectionAdapter(ConnectionManagementConnectorAdapter adapter) {
        SlackConnectorConnectionManagementAdapter connector = ((SlackConnectorConnectionManagementAdapter) adapter);
        ConnectionManagementConnectionAdapter strategy = ((ConnectionManagementConnectionAdapter) connector.getConnectionStrategy());
        return strategy;
    }

    public TestResult test() {
        SlackConnectorConnectionManagementAdapter connection = null;
        DefaultTestResult result;
        ConnectionManagementConfigTypeSlackConnectorConnectionKey key = getDefaultConnectionKey();
        try {
            connection = acquireConnection(key);
            result = new DefaultTestResult(Result.Status.SUCCESS);
        } catch (Exception e) {
            try {
                destroyConnection(key, connection);
            } catch (Exception ie) {
            }
            result = ((DefaultTestResult) ConnectivityTestingErrorHandler.buildFailureTestResult(e));
        } finally {
            if (connection!= null) {
                try {
                    releaseConnection(key, connection);
                } catch (Exception ie) {
                }
            }
        }
        return result;
    }

    @Override
    public Result<List<MetaDataKey>> getMetaDataKeys() {
        SlackConnectorConnectionManagementAdapter connection = null;
        ConnectionManagementConfigTypeSlackConnectorConnectionKey key = getDefaultConnectionKey();
        try {
            connection = acquireConnection(key);
            try {
                List<MetaDataKey> gatheredMetaDataKeys = new ArrayList<MetaDataKey>();
                GroupCategory groupCategory = new GroupCategory();
                groupCategory.setMyconnector(connection);
                gatheredMetaDataKeys.addAll(fillCategory(groupCategory.getEntities(), "GroupCategory"));
                UserCategory userCategory = new UserCategory();
                userCategory.setMyconnector(connection);
                gatheredMetaDataKeys.addAll(fillCategory(userCategory.getEntities(), "UserCategory"));
                AllChannelCategory allChannelCategory = new AllChannelCategory();
                allChannelCategory.setMyconnector(connection);
                gatheredMetaDataKeys.addAll(fillCategory(allChannelCategory.getEntities(), "AllChannelCategory"));
                ChannelCategory channelCategory = new ChannelCategory();
                channelCategory.setMyconnector(connection);
                gatheredMetaDataKeys.addAll(fillCategory(channelCategory.getEntities(), "ChannelCategory"));
                return new DefaultResult<List<MetaDataKey>>(gatheredMetaDataKeys, (Result.Status.SUCCESS));
            } catch (Exception e) {
                return new DefaultResult<List<MetaDataKey>>(null, (Result.Status.FAILURE), "There was an error retrieving the metadata keys from service provider after acquiring connection, for more detailed information please read the provided stacktrace", MetaDataFailureType.ERROR_METADATA_KEYS_RETRIEVER, e);
            }
        } catch (Exception e) {
            try {
                destroyConnection(key, connection);
            } catch (Exception ie) {
            }
            return ((DefaultResult<List<MetaDataKey>> ) ConnectivityTestingErrorHandler.buildFailureTestResult(e));
        } finally {
            if (connection!= null) {
                try {
                    releaseConnection(key, connection);
                } catch (Exception ie) {
                }
            }
        }
    }

    private List<MetaDataKey> fillCategory(List<MetaDataKey> metadataKeys, String categoryClassName) {
        for (MetaDataKey metaDataKey: metadataKeys) {
            ((DefaultMetaDataKey) metaDataKey).setCategory(categoryClassName);
        }
        return metadataKeys;
    }

    @Override
    public Result<MetaData> getMetaData(MetaDataKey metaDataKey) {
        SlackConnectorConnectionManagementAdapter connection = null;
        ConnectionManagementConfigTypeSlackConnectorConnectionKey key = getDefaultConnectionKey();
        try {
            connection = acquireConnection(key);
            try {
                MetaData metaData = null;
                TypeDescribingProperty property = metaDataKey.getProperty(TypeDescribingProperty.class);
                String category = ((DefaultMetaDataKey) metaDataKey).getCategory();
                if (category!= null) {
                    if (category.equals("GroupCategory")) {
                        GroupCategory groupCategory = new GroupCategory();
                        groupCategory.setMyconnector(connection);
                        metaData = groupCategory.describeEntity(metaDataKey);
                    } else {
                        if (category.equals("UserCategory")) {
                            UserCategory userCategory = new UserCategory();
                            userCategory.setMyconnector(connection);
                            metaData = userCategory.describeEntity(metaDataKey);
                        } else {
                            if (category.equals("AllChannelCategory")) {
                                AllChannelCategory allChannelCategory = new AllChannelCategory();
                                allChannelCategory.setMyconnector(connection);
                                metaData = allChannelCategory.describeEntity(metaDataKey);
                            } else {
                                if (category.equals("ChannelCategory")) {
                                    ChannelCategory channelCategory = new ChannelCategory();
                                    channelCategory.setMyconnector(connection);
                                    metaData = channelCategory.describeEntity(metaDataKey);
                                } else {
                                    throw new Exception(((("Invalid key type. There is no matching category for ["+ metaDataKey.getId())+"]. All keys must contain a category with any of the following options:[GroupCategory, UserCategory, AllChannelCategory, ChannelCategory]")+((", but found ["+ category)+"] instead")));
                                }
                            }
                        }
                    }
                } else {
                    throw new Exception((("Invalid key type. There is no matching category for ["+ metaDataKey.getId())+"]. All keys must contain a category with any of the following options:[GroupCategory, UserCategory, AllChannelCategory, ChannelCategory]"));
                }
                metaData.getPayload().addProperty(new StructureIdentifierMetaDataModelProperty(metaDataKey, false));
                return new DefaultResult<MetaData>(metaData);
            } catch (Exception e) {
                return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), getMetaDataException(metaDataKey), MetaDataFailureType.ERROR_METADATA_RETRIEVER, e);
            }
        } catch (Exception e) {
            try {
                destroyConnection(key, connection);
            } catch (Exception ie) {
            }
            return ((DefaultResult<MetaData> ) ConnectivityTestingErrorHandler.buildFailureTestResult(e));
        } finally {
            if (connection!= null) {
                try {
                    releaseConnection(key, connection);
                } catch (Exception ie) {
                }
            }
        }
    }

    private String getMetaDataException(MetaDataKey key) {
        if ((key!= null)&&(key.getId()!= null)) {
            return ("There was an error retrieving metadata from key: "+(key.getId()+" after acquiring the connection, for more detailed information please read the provided stacktrace"));
        } else {
            return "There was an error retrieving metadata after acquiring the connection, MetaDataKey is null or its id is null, for more detailed information please read the provided stacktrace";
        }
    }

}
