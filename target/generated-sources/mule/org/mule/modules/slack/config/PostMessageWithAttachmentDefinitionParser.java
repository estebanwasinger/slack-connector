
package org.mule.modules.slack.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.mule.modules.slack.client.model.chat.attachment.holders.ChatAttachmentExpressionHolder;
import org.mule.modules.slack.client.model.chat.attachment.holders.FieldExpressionHolder;
import org.mule.modules.slack.processors.PostMessageWithAttachmentMessageProcessor;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser.ParseDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class PostMessageWithAttachmentDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{

    private static Logger logger = LoggerFactory.getLogger(PostMessageWithAttachmentDefinitionParser.class);

    private BeanDefinitionBuilder getBeanDefinitionBuilder(ParserContext parserContext) {
        try {
            return BeanDefinitionBuilder.rootBeanDefinition(PostMessageWithAttachmentMessageProcessor.class.getName());
        } catch (NoClassDefFoundError noClassDefFoundError) {
            String muleVersion = "";
            try {
                muleVersion = MuleManifest.getProductVersion();
            } catch (Exception _x) {
                logger.error("Problem while reading mule version");
            }
            logger.error(("Cannot launch the mule app, the @Processor [post-message-with-attachment] within the connector [slack] is not supported in mule "+ muleVersion));
            throw new BeanDefinitionParsingException(new Problem(("Cannot launch the mule app, the @Processor [post-message-with-attachment] within the connector [slack] is not supported in mule "+ muleVersion), new Location(parserContext.getReaderContext().getResource()), null, noClassDefFoundError));
        }
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = getBeanDefinitionBuilder(parserContext);
        builder.addConstructorArgValue("postMessageWithAttachment");
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        if (!hasAttribute(element, "config-ref")) {
            throw new BeanDefinitionParsingException(new Problem("It seems that the config-ref for @Processor [post-message-with-attachment] within the connector [slack] is null or missing. Please, fill the value with the correct global element.", new Location(parserContext.getReaderContext().getResource()), null));
        }
        parseConfigRef(element, builder);
        parseProperty(builder, element, "message", "message");
        parseProperty(builder, element, "channelId", "channelId");
        parseProperty(builder, element, "username", "username");
        parseProperty(builder, element, "iconURL", "iconURL");
        if (!parseObjectRefWithDefault(element, builder, "chat-attachment", "chatAttachment", "#[payload]")) {
            BeanDefinitionBuilder chatAttachmentBuilder = BeanDefinitionBuilder.rootBeanDefinition(ChatAttachmentExpressionHolder.class.getName());
            Element chatAttachmentChildElement = DomUtils.getChildElementByTagName(element, "chat-attachment");
            if (chatAttachmentChildElement!= null) {
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "fallback", "fallback");
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "color", "color");
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "pretext", "pretext");
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "authorName", "authorName");
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "authorLink", "authorLink");
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "authorIcon", "authorIcon");
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "title", "title");
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "titleLink", "titleLink");
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "text", "text");
                parseListAndSetProperty(chatAttachmentChildElement, chatAttachmentBuilder, "fields", "fields", "field", new ParseDelegate<BeanDefinition>() {


                    public BeanDefinition parse(Element element) {
                        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(FieldExpressionHolder.class);
                        parseProperty(builder, element, "title", "title");
                        parseProperty(builder, element, "value", "value");
                        return builder.getBeanDefinition();
                    }

                }
                );
                parseProperty(chatAttachmentBuilder, chatAttachmentChildElement, "imageUrl", "imageUrl");
                builder.addPropertyValue("chatAttachment", chatAttachmentBuilder.getBeanDefinition());
            }
        }
        if (!parseObjectRef(element, builder, "field", "field")) {
            BeanDefinitionBuilder fieldBuilder = BeanDefinitionBuilder.rootBeanDefinition(FieldExpressionHolder.class.getName());
            Element fieldChildElement = DomUtils.getChildElementByTagName(element, "field");
            if (fieldChildElement!= null) {
                parseProperty(fieldBuilder, fieldChildElement, "title", "title");
                parseProperty(fieldBuilder, fieldChildElement, "value", "value");
                builder.addPropertyValue("field", fieldBuilder.getBeanDefinition());
            }
        }
        parseProperty(builder, element, "asUser", "asUser");
        parseProperty(builder, element, "accessTokenId");
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContext, definition);
        return definition;
    }

}
