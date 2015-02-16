package org.mule.modules.slack.strategy;


import org.mule.api.ConnectionException;
import org.mule.api.annotations.*;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.oauth.*;
import org.mule.api.annotations.param.ConnectionKey;
import org.stevew.SlackClient;

@OAuth2( configElementName = "oauth2-type", friendlyName="OAuth2 Configuration",
        accessTokenUrl = "https://slack.com/api/oauth.access",
        authorizationUrl = "https://slack.com/oauth/authorize",
        accessTokenRegex = "\"access_token\":\"([^&]+?)\"",
        expirationRegex = "\"expires_in\":([^&]+?),",
        refreshTokenRegex = "\"refresh_token\":\"([^&]+?)\"" )
public class OAuth2ConnectionStrategy implements  SlackConnectionStrategy {

    SlackClient client;

    /**
     * The OAuth access token
     */
    @OAuthAccessToken
    private String accessToken;

    /**
     * The OAuth consumer key
     */
    @Configurable
    @OAuthConsumerKey
    private String consumerKey;

    /**
     * The OAuth consumer secret
     */
    @Configurable
    @OAuthConsumerSecret
    private String consumerSecret;

    @OAuthPostAuthorization
    public void postAuthorize() {
        client = new SlackClient(accessToken);
        System.out.println(client.testAuth());
    }

    /**
     * Connect
     *
     * @param username A username
     * @param password A password
     * @throws ConnectionException
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey String username, @Password String password)
            throws ConnectionException {
        /*
         * CODE FOR ESTABLISHING A CONNECTION GOES IN HERE
         */
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        /*
         * CODE FOR CLOSING A CONNECTION GOES IN HERE
         */
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
       if (client == null){
           return false;
       }else{
           return client.isConnected();
       }
    }


    /**
     * Are we connected
     */
    @ConnectionIdentifier
    public String connectionId() {
        return "001";
    }
    /**
     * Set accessToken
     *
     * @param accessToken
     *            The accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Get accessToken
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Set consumerKey
     *
     * @param consumerKey The consumerKey
     */
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    /**
     * Get consumerKey
     */
    public String getConsumerKey() {
        return this.consumerKey;
    }

    /**
     * Set consumerSecret
     *
     * @param consumerSecret The consumerSecret
     */
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    /**
     * Get consumerSecret
     */
    public String getConsumerSecret() {
        return this.consumerSecret;
    }

    @Override
    public SlackClient getSlackClient() {
        return client;
    }

    @Override
    public Boolean isAuthorized() {
        if(accessToken == null){
            return false;
        } else {
            return true;
        }
    }
}