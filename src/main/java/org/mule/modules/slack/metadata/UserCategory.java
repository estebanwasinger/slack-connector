/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.metadata;


import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.PojoMetaDataBuilder;
import org.mule.modules.slack.SlackConnector;
import org.mule.modules.slack.client.model.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@MetaDataCategory
public class UserCategory {

    @Inject
    private SlackConnector connector;

    @MetaDataKeyRetriever
    public List<MetaDataKey> getEntities() throws Exception {
        List<MetaDataKey> entities = new ArrayList<MetaDataKey>();
        List<User> userList = connector.slack().users.getUserList();
        for(User user: userList){
            entities.add(new DefaultMetaDataKey(user.getId(),user.getName()+" - "+user.getId()));
        }
        return entities;
    }
    @MetaDataRetriever
    public MetaData describeEntity(MetaDataKey entityKey) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        PojoMetaDataBuilder<?> pojo = builder.createPojo(User.class);

        return new DefaultMetaData(pojo.build());
    }

    public SlackConnector getConnector() {
        return connector;
    }

    public void setConnector(SlackConnector connector) {
        this.connector = connector;
    }

}