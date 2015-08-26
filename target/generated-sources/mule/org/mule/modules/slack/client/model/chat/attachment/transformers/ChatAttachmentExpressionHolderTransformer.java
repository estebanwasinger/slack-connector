
package org.mule.modules.slack.client.model.chat.attachment.transformers;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.endpoint.ImmutableEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.MessageTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transformer.TransformerMessagingException;
import org.mule.config.i18n.CoreMessages;
import org.mule.devkit.processor.ExpressionEvaluatorSupport;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.modules.slack.client.model.chat.attachment.Field;
import org.mule.modules.slack.client.model.chat.attachment.holders.ChatAttachmentExpressionHolder;
import org.mule.transformer.types.DataTypeFactory;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class ChatAttachmentExpressionHolderTransformer
    extends ExpressionEvaluatorSupport
    implements DiscoverableTransformer, MessageTransformer
{

    private int weighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;
    private ImmutableEndpoint endpoint;
    private MuleContext muleContext;
    private String name;

    public int getPriorityWeighting() {
        return weighting;
    }

    public void setPriorityWeighting(int weighting) {
        this.weighting = weighting;
    }

    public boolean isSourceTypeSupported(Class<?> aClass) {
        return (aClass == ChatAttachmentExpressionHolder.class);
    }

    public boolean isSourceDataTypeSupported(DataType<?> dataType) {
        return (dataType.getType() == ChatAttachmentExpressionHolder.class);
    }

    public List<Class<?>> getSourceTypes() {
        return Arrays.asList(new Class<?> [] {ChatAttachmentExpressionHolder.class });
    }

    public List<DataType<?>> getSourceDataTypes() {
        return Arrays.asList(new DataType<?> [] {DataTypeFactory.create(ChatAttachmentExpressionHolder.class)});
    }

    public boolean isAcceptNull() {
        return false;
    }

    public boolean isIgnoreBadInput() {
        return false;
    }

    public Object transform(Object src)
        throws TransformerException
    {
        throw new UnsupportedOperationException();
    }

    public Object transform(Object src, String encoding)
        throws TransformerException
    {
        throw new UnsupportedOperationException();
    }

    public void setReturnClass(Class<?> theClass) {
        throw new UnsupportedOperationException();
    }

    public Class<?> getReturnClass() {
        return ChatAttachment.class;
    }

    public void setReturnDataType(DataType<?> type) {
        throw new UnsupportedOperationException();
    }

    public DataType<?> getReturnDataType() {
        return DataTypeFactory.create(ChatAttachment.class);
    }

    public void setEndpoint(ImmutableEndpoint ep) {
        endpoint = ep;
    }

    public ImmutableEndpoint getEndpoint() {
        return endpoint;
    }

    public void dispose() {
    }

    public void initialise()
        throws InitialisationException
    {
    }

    public void setMuleContext(MuleContext context) {
        muleContext = context;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public Object transform(Object src, MuleEvent event)
        throws TransformerMessagingException
    {
        return transform(src, null, event);
    }

    public Object transform(Object src, String encoding, MuleEvent event)
        throws TransformerMessagingException
    {
        if (src == null) {
            return null;
        }
        ChatAttachmentExpressionHolder holder = ((ChatAttachmentExpressionHolder) src);
        ChatAttachment result = new ChatAttachment();
        try {
            final String _transformedFallback = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_fallbackType").getGenericType(), null, holder.getFallback()));
            result.setFallback(_transformedFallback);
            final String _transformedColor = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_colorType").getGenericType(), null, holder.getColor()));
            result.setColor(_transformedColor);
            final String _transformedPretext = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_pretextType").getGenericType(), null, holder.getPretext()));
            result.setPretext(_transformedPretext);
            final String _transformedAuthorName = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_authorNameType").getGenericType(), null, holder.getAuthorName()));
            result.setAuthorName(_transformedAuthorName);
            final String _transformedAuthorLink = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_authorLinkType").getGenericType(), null, holder.getAuthorLink()));
            result.setAuthorLink(_transformedAuthorLink);
            final String _transformedAuthorIcon = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_authorIconType").getGenericType(), null, holder.getAuthorIcon()));
            result.setAuthorIcon(_transformedAuthorIcon);
            final String _transformedTitle = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_titleType").getGenericType(), null, holder.getTitle()));
            result.setTitle(_transformedTitle);
            final String _transformedTitleLink = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_titleLinkType").getGenericType(), null, holder.getTitleLink()));
            result.setTitleLink(_transformedTitleLink);
            final String _transformedText = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_textType").getGenericType(), null, holder.getText()));
            result.setText(_transformedText);
            final List<Field> _transformedFields = ((List<Field> ) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_fieldsType").getGenericType(), null, holder.getFields()));
            result.setFields(_transformedFields);
            final String _transformedImageUrl = ((String) evaluateAndTransform(this.muleContext, event, ChatAttachmentExpressionHolder.class.getDeclaredField("_imageUrlType").getGenericType(), null, holder.getImageUrl()));
            result.setImageUrl(_transformedImageUrl);
        } catch (NoSuchFieldException e) {
            throw new TransformerMessagingException(CoreMessages.createStaticMessage("internal error"), event, this, e);
        } catch (TransformerException e) {
            throw new TransformerMessagingException(e.getI18nMessage(), event, this, e);
        }
        return result;
    }

    public MuleEvent process(MuleEvent event) {
        return null;
    }

    public String getMimeType() {
        return null;
    }

    public String getEncoding() {
        return null;
    }

    public MuleContext getMuleContext() {
        return muleContext;
    }

}
