# Slack Anypoint Connector

## Mule Certified Connector
__Slack Anypoint Connector__ now is a MuleSoft Community Certified Connector, which ensures the quality of this project.

The connector can be download directly from the [Anypoint Exchange](https://www.mulesoft.com/exchange/#!/slack-integration-connector)!!

# Mule supported versions
- Mule 3.5+

# Documentation
http://estebanwasinger.github.io/slack-connector/

# Installation 
1. Clone this Slack SDK repository: **git clone https://github.com/estebanwasinger/slack-sdk**
2. Clone the connector repository: **git clone https://github.com/estebanwasinger/slack-connector**
3. Compile with Maven the Slack-SDK: **mvn clean install -DskipTests**
4. Import the connector in [Anypoint Studio](http://www.mulesoft.com/platform/mule-studio): **File** --> **Import** --> **Anypoint Connector from External Location**
5. Install it in Anypoint Studio: **Right Click in the connector project** --> **Anypoint Connector** --> **Install or Update**
6. Enjoy!

#Usage
## Configuration
### Connection Management
1. The easiest way to test the **Slack Connector** is to use the Connection Management configuration.
2. Drop a HTTP connector, after that drop the **Slack Connector**, add a new Global configuration.
3. Select Connection Management.
4. The configuration will ask for an Access Token, go to your [Slack Profile](https://api.slack.com/web) and search for it.
5. Complete the AccessToken, and click in **Test Connectivity**, if everything is OK, Anypoint Studio will show you that the Test Connection was successfull. 

# Reporting Issues

We use GitHub:Issues for tracking issues with this connector. You can report new issues at this link https://github.com/estebanwasinger/slack-connector/issues.
