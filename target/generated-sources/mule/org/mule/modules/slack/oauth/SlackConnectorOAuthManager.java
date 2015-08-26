
package org.mule.modules.slack.oauth;

import java.io.Serializable;
import javax.annotation.Generated;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.store.ObjectStore;
import org.mule.modules.slack.SlackConnector;
import org.mule.modules.slack.adapters.SlackConnectorOAuth2Adapter;
import org.mule.modules.slack.strategy.OAuth2ConnectionStrategy;
import org.mule.security.oauth.BaseOAuth2Manager;
import org.mule.security.oauth.OAuth2Adapter;
import org.mule.security.oauth.OAuth2Manager;
import org.mule.security.oauth.OnNoTokenPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A {@code SlackConnectorOAuthManager} is a wrapper around {@link SlackConnector } that adds access token management capabilities to the pojo.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class SlackConnectorOAuthManager
    extends BaseOAuth2Manager<OAuth2Adapter>
{

    private static Logger logger = LoggerFactory.getLogger(SlackConnectorOAuthManager.class);

    @Override
    protected Logger getLogger() {
        return logger;
    }

    /**
     * Sets consumerKey
     * 
     * @param key to set
     */
    public void setConsumerKey(String value) {
        super.setConsumerKey(value);
    }

    /**
     * Sets consumerSecret
     * 
     * @param secret to set
     */
    public void setConsumerSecret(String value) {
        super.setConsumerSecret(value);
    }

    @Override
    protected OAuth2Adapter instantiateAdapter() {
        return new SlackConnectorOAuth2Adapter(this);
    }

    @Override
    protected KeyedPoolableObjectFactory createPoolFactory(OAuth2Manager<OAuth2Adapter> oauthManager, ObjectStore<Serializable> objectStore) {
        return new SlackConnectorOAuthClientFactory(oauthManager, objectStore);
    }

    @Override
    protected void setCustomProperties(OAuth2Adapter adapter) {
        SlackConnectorOAuth2Adapter connector = ((SlackConnectorOAuth2Adapter) adapter);
        ((OAuth2ConnectionStrategy) connector.getConnectionStrategy()).setConsumerKey(getConsumerKey());
        ((OAuth2ConnectionStrategy) connector.getConnectionStrategy()).setConsumerSecret(getConsumerSecret());
    }

    protected void fetchCallbackParameters(OAuth2Adapter adapter, String response) {
        SlackConnectorOAuth2Adapter connector = ((SlackConnectorOAuth2Adapter) adapter);
        ExpressionManager expressionManager = (muleContext.getExpressionManager());
        MuleMessage muleMessage = new DefaultMuleMessage(response, (muleContext));
    }

    public void setOnNoToken(OnNoTokenPolicy policy) {
        this.getDefaultUnauthorizedConnector().setOnNoTokenPolicy(policy);
    }

}
