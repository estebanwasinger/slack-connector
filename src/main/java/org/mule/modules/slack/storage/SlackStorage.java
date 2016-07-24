/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.storage;

import org.mule.modules.slack.client.utils.Tuple;

import java.util.List;

public interface SlackStorage {

    public List<Tuple<String, Object>> restoreFlowVars(String id);

    public void storageFlowVars(String id, List<Tuple<String, Object>> flowVars);

}
