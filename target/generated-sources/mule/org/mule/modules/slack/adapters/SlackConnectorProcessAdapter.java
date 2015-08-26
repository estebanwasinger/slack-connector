
package org.mule.modules.slack.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.slack.SlackConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>SlackConnectorProcessAdapter</code> is a wrapper around {@link SlackConnector } that enables custom processing strategies.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class SlackConnectorProcessAdapter
    extends SlackConnectorLifecycleInjectionAdapter
    implements ProcessAdapter<SlackConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, SlackConnectorCapabilitiesAdapter> getProcessTemplate() {
        final SlackConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,SlackConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, SlackConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, SlackConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
