
package org.mule.modules.slack.sources;

import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MessagingException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.callback.SourceCallback;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.source.MessageSource;
import org.mule.modules.slack.SlackConnector;
import org.mule.security.oauth.callback.ProcessCallback;
import org.mule.security.oauth.processor.AbstractListeningMessageProcessor;


/**
 * RetrieveMessagesMessageSource wraps {@link org.mule.modules.slack.SlackConnector#retrieveMessages(org.mule.api.callback.SourceCallback, java.lang.Integer, java.lang.String)} method in {@link SlackConnector } as a message source capable of generating Mule events.  The POJO's method is invoked in its own thread.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class RetrieveMessagesMessageSource
    extends AbstractListeningMessageProcessor
    implements Runnable, FlowConstructAware, MuleContextAware, Startable, Stoppable, MessageSource
{

    protected Object messageRetrieverInterval;
    protected Integer _messageRetrieverIntervalType;
    protected Object channelID;
    protected String _channelIDType;
    /**
     * Thread under which this message source will execute
     * 
     */
    private Thread thread;

    public RetrieveMessagesMessageSource(String operationName) {
        super(operationName);
    }

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     * 
     * @throws InitialisationException
     */
    public void initialise()
        throws InitialisationException
    {
    }

    /**
     * Sets channelID
     * 
     * @param value Value to set
     */
    public void setChannelID(Object value) {
        this.channelID = value;
    }

    /**
     * Sets messageRetrieverInterval
     * 
     * @param value Value to set
     */
    public void setMessageRetrieverInterval(Object value) {
        this.messageRetrieverInterval = value;
    }

    /**
     * Method to be called when Mule instance gets started.
     * 
     */
    public void start()
        throws MuleException
    {
        if (thread == null) {
            thread = new Thread(this, "Receiving Thread");
        }
        thread.start();
    }

    /**
     * Method to be called when Mule instance gets stopped.
     * 
     */
    public void stop()
        throws MuleException
    {
        thread.interrupt();
    }

    /**
     * Implementation {@link Runnable#run()} that will invoke the method on the pojo that this message source wraps.
     * 
     */
    public void run() {
        Object moduleObject = null;
        try {
            moduleObject = findOrCreate(null, false, null);
            final ProcessTemplate<Object, Object> processTemplate = ((ProcessAdapter<Object> ) moduleObject).getProcessTemplate();
            final SourceCallback sourceCallback = this;
            final Integer transformedMessageRetrieverInterval = ((Integer) transform(getMuleContext(), ((MuleEvent) null), getClass().getDeclaredField("_messageRetrieverIntervalType").getGenericType(), null, messageRetrieverInterval));
            final String transformedChannelID = ((String) transform(getMuleContext(), ((MuleEvent) null), getClass().getDeclaredField("_channelIDType").getGenericType(), null, channelID));
            processTemplate.execute(new ProcessCallback<Object,Object>() {


                public List<Class<? extends Exception>> getManagedExceptions() {
                    return null;
                }

                public boolean isProtected() {
                    return false;
                }

                public Object process(Object object)
                    throws Exception
                {
                    ((SlackConnector) object).retrieveMessages(sourceCallback, transformedMessageRetrieverInterval, transformedChannelID);
                    return null;
                }

            }
            , null, ((MuleEvent) null));
        } catch (MessagingException e) {
            getFlowConstruct().getExceptionListener().handleException(e, e.getEvent());
        } catch (Exception e) {
            getMuleContext().getExceptionListener().handleException(e);
        }
    }

}
