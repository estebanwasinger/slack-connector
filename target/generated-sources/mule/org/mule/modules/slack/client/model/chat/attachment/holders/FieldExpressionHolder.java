
package org.mule.modules.slack.client.model.chat.attachment.holders;

import javax.annotation.Generated;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class FieldExpressionHolder {

    protected Object title;
    protected String _titleType;
    protected Object value;
    protected String _valueType;

    /**
     * Sets title
     * 
     * @param value Value to set
     */
    public void setTitle(Object value) {
        this.title = value;
    }

    /**
     * Retrieves title
     * 
     */
    public Object getTitle() {
        return this.title;
    }

    /**
     * Sets value
     * 
     * @param value Value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Retrieves value
     * 
     */
    public Object getValue() {
        return this.value;
    }

}
