
package org.mule.modules.slack.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/slack</code>.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class SlackNamespaceHandler
    extends NamespaceHandlerSupport
{

    private static Logger logger = LoggerFactory.getLogger(SlackNamespaceHandler.class);

    private void handleException(String beanName, String beanScope, NoClassDefFoundError noClassDefFoundError) {
        String muleVersion = "";
        try {
            muleVersion = MuleManifest.getProductVersion();
        } catch (Exception _x) {
            logger.error("Problem while reading mule version");
        }
        logger.error(((((("Cannot launch the mule app, the  "+ beanScope)+" [")+ beanName)+"] within the connector [slack] is not supported in mule ")+ muleVersion));
        throw new FatalBeanException(((((("Cannot launch the mule app, the  "+ beanScope)+" [")+ beanName)+"] within the connector [slack] is not supported in mule ")+ muleVersion), noClassDefFoundError);
    }

    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        try {
            this.registerBeanDefinitionParser("config-type", new SlackConnectorConnectionManagementStrategyConfigDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("config-type", "@Config", ex);
        }
        try {
            this.registerBeanDefinitionParser("oauth2-type", new SlackConnectorOAuth2ConnectionStrategyConfigDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("oauth2-type", "@Config", ex);
        }
        try {
            this.registerBeanDefinitionParser("authorize", new AuthorizeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("authorize", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("unauthorize", new UnauthorizeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("unauthorize", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-user-info", new GetUserInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-user-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-user-info-by-name", new GetUserInfoByNameDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-user-info-by-name", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-user-list", new GetUserListDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-user-list", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-channel-list", new GetChannelListDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-channel-list", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-channel-history", new GetChannelHistoryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-channel-history", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-channel-info", new GetChannelInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-channel-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-channel-by-name", new GetChannelByNameDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-channel-by-name", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-channel", new CreateChannelDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-channel", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("rename-channel", new RenameChannelDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("rename-channel", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("join-channel", new JoinChannelDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("join-channel", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("leave-channel", new LeaveChannelDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("leave-channel", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("archive-channel", new ArchiveChannelDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("archive-channel", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("unarchive-channel", new UnarchiveChannelDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("unarchive-channel", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("set-channel-topic", new SetChannelTopicDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("set-channel-topic", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("set-channel-purpose", new SetChannelPurposeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("set-channel-purpose", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("post-message", new PostMessageDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("post-message", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("post-message-with-attachment", new PostMessageWithAttachmentDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("post-message-with-attachment", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("delete-message", new DeleteMessageDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("delete-message", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("update-message", new UpdateMessageDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("update-message", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("open-direct-message-channel", new OpenDirectMessageChannelDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("open-direct-message-channel", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("close-direct-message-channel", new CloseDirectMessageChannelDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("close-direct-message-channel", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("list-direct-message-channels", new ListDirectMessageChannelsDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("list-direct-message-channels", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-d-m-history", new GetDMHistoryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-d-m-history", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-group-list", new GetGroupListDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-group-list", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-group-history", new GetGroupHistoryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-group-history", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("set-group-topic", new SetGroupTopicDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("set-group-topic", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("set-group-purpose", new SetGroupPurposeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("set-group-purpose", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-group", new CreateGroupDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-group", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("close-group", new CloseGroupDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("close-group", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("open-group", new OpenGroupDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("open-group", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("archive-group", new ArchiveGroupDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("archive-group", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("unarchive-group", new UnarchiveGroupDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("unarchive-group", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("rename-group", new RenameGroupDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("rename-group", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-group-info", new GetGroupInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-group-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("leave-group", new LeaveGroupDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("leave-group", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("upload-file", new UploadFileDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("upload-file", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("upload-file-as-input-streams", new UploadFileAsInputStreamsDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("upload-file-as-input-streams", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("retrieve-messages", new RetrieveMessagesDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("retrieve-messages", "@Source", ex);
        }
    }

}
