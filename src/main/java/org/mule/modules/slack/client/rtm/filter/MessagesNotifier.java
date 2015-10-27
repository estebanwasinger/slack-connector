package org.mule.modules.slack.client.rtm.filter;


import java.util.Map;

public class MessagesNotifier implements EventNotifier {

    private final Boolean onlyDM;
    private final Boolean onlyNewMessages;

    public MessagesNotifier(Boolean onlyDM, Boolean onlyNewMessages) {
        this.onlyDM = onlyDM;
        this.onlyNewMessages = onlyNewMessages;
    }

    @Override
    public boolean shouldSend(Map<String, Object> message) {
        boolean returnValue = false;
        if (message.get("type").equals("message")) {
            if (!(onlyNewMessages && !isNewMessage(message))) {

                if (onlyDM && ((String) message.get("channel")).toLowerCase().startsWith("d")) {
                    returnValue = true;
                }
                if (!onlyDM) {
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }

    public Boolean isNewMessage(Map<String, Object> message) {
        return message.get("subtype") == null;
    }
}
