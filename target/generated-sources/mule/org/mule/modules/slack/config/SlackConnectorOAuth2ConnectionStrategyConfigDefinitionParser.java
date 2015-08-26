
package org.mule.modules.slack.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.mule.modules.slack.oauth.SlackConnectorOAuthManager;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class SlackConnectorOAuth2ConnectionStrategyConfigDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{

    private static Logger logger = LoggerFactory.getLogger(SlackConnectorOAuth2ConnectionStrategyConfigDefinitionParser.class);

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        parseConfigName(element);
        BeanDefinitionBuilder builder = getBeanDefinitionBuilder(parserContext);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        setInitMethodIfNeeded(builder, SlackConnectorOAuthManager.class);
        setDestroyMethodIfNeeded(builder, SlackConnectorOAuthManager.class);
        parseProperty(builder, element, "name", "name");
        parseProperty(builder, element, "authorizationUrl");
        parseProperty(builder, element, "accessTokenUrl");
        parseProperty(builder, element, "onNoToken");
        Element oauthHttpCallbackConfigElement = DomUtils.getChildElementByTagName(element, "oauth-callback-config");
        if (oauthHttpCallbackConfigElement!= null) {
            parseProperty(builder, oauthHttpCallbackConfigElement, "domain");
            parseProperty(builder, oauthHttpCallbackConfigElement, "localPort");
            parseProperty(builder, oauthHttpCallbackConfigElement, "remotePort");
            parseProperty(builder, oauthHttpCallbackConfigElement, "async");
            parseProperty(builder, oauthHttpCallbackConfigElement, "path");
            parseProperty(builder, oauthHttpCallbackConfigElement, "defaultAccessTokenId");
            if (hasAttribute(oauthHttpCallbackConfigElement, "connector-ref")) {
                builder.addPropertyValue("connector", new RuntimeBeanReference(oauthHttpCallbackConfigElement.getAttribute("connector-ref")));
            }
        }
        Element oauthStoreConfigElement = DomUtils.getChildElementByTagName(element, "oauth-store-config");
        if (oauthStoreConfigElement!= null) {
            parsePropertyRef(builder, oauthStoreConfigElement, "objectStore-ref", "accessTokenObjectStore");
        }
        parseProperty(builder, element, "consumerKey", "consumerKey");
        parseProperty(builder, element, "consumerSecret", "consumerSecret");
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        return definition;
    }

    private BeanDefinitionBuilder getBeanDefinitionBuilder(ParserContext parserContext) {
        try {
            return BeanDefinitionBuilder.rootBeanDefinition(SlackConnectorOAuthManager.class.getName());
        } catch (NoClassDefFoundError noClassDefFoundError) {
            String muleVersion = "";
            try {
                muleVersion = MuleManifest.getProductVersion();
            } catch (Exception _x) {
                logger.error("Problem while reading mule version");
            }
            logger.error(("Cannot launch the mule app, the configuration [oauth2-type] within the connector [slack] is not supported in mule "+ muleVersion));
            throw new BeanDefinitionParsingException(new Problem(("Cannot launch the mule app, the configuration [oauth2-type] within the connector [slack] is not supported in mule "+ muleVersion), new Location(parserContext.getReaderContext().getResource()), null, noClassDefFoundError));
        }
    }

}
