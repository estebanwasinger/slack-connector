/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.storage;

import org.mule.api.MuleContext;
import org.mule.api.config.MuleProperties;
import org.mule.api.store.ObjectAlreadyExistsException;
import org.mule.api.store.ObjectDoesNotExistException;
import org.mule.api.store.ObjectStore;
import org.mule.api.store.ObjectStoreException;
import org.mule.modules.slack.client.exceptions.SlackException;
import org.mule.modules.slack.client.utils.Tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectStoreStorage implements SlackStorage {

    private final ObjectStore<Serializable> objectStore;

    public ObjectStoreStorage(ObjectStore<Serializable> objectStore) {
        this.objectStore = objectStore;
    }

    public ObjectStoreStorage(MuleContext muleContext) {
        objectStore = muleContext.getRegistry().lookupObject(MuleProperties.DEFAULT_USER_OBJECT_STORE_NAME);

        if (objectStore == null) {
            throw new SlackException("Unable to find an ObjectStore");
        }
    }

    @Override
    public List<Tuple<String, Object>> restoreFlowVars(String id) {
        ArrayList<Tuple<String, Object>> flowVars;
        try {
            flowVars = (ArrayList<Tuple<String, Object>>) objectStore.retrieve(id);
        } catch (org.mule.api.store.ObjectStoreException e) {
            flowVars = new ArrayList<>();
        }

        return flowVars;
    }

    @Override
    public void storageFlowVars(String id, List<Tuple<String, Object>> flowVars) {
        try {
            try {
                objectStore.store(id, (Serializable) flowVars);
            } catch (ObjectStoreException e) {
                objectStore.remove(id);
                objectStore.store(id, (Serializable) flowVars);
            }
        } catch (ObjectStoreException e) {
            throw new RuntimeException(e);
        }
    }
}
