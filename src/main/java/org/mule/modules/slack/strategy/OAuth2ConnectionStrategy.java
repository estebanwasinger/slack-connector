/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.strategy;


import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.oauth.*;
import org.mule.modules.slack.client.SlackClient;

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
    @FriendlyName("Client ID")
    private String consumerKey;

    /**
     * The OAuth consumer secret
     */
    @Configurable
    @OAuthConsumerSecret
    @FriendlyName("Client Secret")
    private String consumerSecret;

    @OAuthPostAuthorization
    public void postAuthorize() {
        client = new SlackClient(accessToken);
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

    public SlackClient getSlackClient() {
        return client;
    }

    public String getToken() {
        return getAccessToken();
    }

    public Boolean isAuthorized() {
        if(accessToken == null){
            return false;
        } else {
            return true;
        }
    }
}