
package org.mule.modules.slack.processors;

import java.util.regex.Pattern;
import javax.annotation.Generated;
import org.mule.modules.slack.oauth.SlackConnectorOAuthManager;
import org.mule.security.oauth.processor.BaseOAuth2AuthorizeMessageProcessor;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-08-26T07:19:14-03:00", comments = "Build UNNAMED.2405.44720b7")
public class AuthorizeMessageProcessor
    extends BaseOAuth2AuthorizeMessageProcessor<SlackConnectorOAuthManager>
{

    private final static Pattern AUTH_CODE_PATTERN = Pattern.compile("code=([^&]+)");

    @Override
    protected String getAuthCodeRegex() {
        return AUTH_CODE_PATTERN.pattern();
    }

    @Override
    protected Class<SlackConnectorOAuthManager> getOAuthManagerClass() {
        return SlackConnectorOAuthManager.class;
    }

}
