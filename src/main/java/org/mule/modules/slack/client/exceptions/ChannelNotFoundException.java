package org.mule.modules.slack.client.exceptions;

/**
 * Created by estebanwasinger on 1/4/15.
 */
public class ChannelNotFoundException extends RuntimeException {

    public ChannelNotFoundException(String msg) {
        super(msg);
    }

}

