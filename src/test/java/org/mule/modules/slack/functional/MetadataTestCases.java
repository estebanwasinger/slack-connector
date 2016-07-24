/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.common.Result;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.slack.metadata.UserCategory;
import org.mule.modules.slack.runner.AbstractSlackTestCase;
import org.mule.tools.devkit.ctf.junit.MetaDataTest;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by estebanwasinger on 11/19/15.
 */
public class MetadataTestCases extends AbstractSlackTestCase {

    public static final String KEY = "U03NE28RL";

    @Test
    @MetaDataTest
    public void testGetMetaDataKeys() throws Exception {

        MetaDataKey userName = null;

        // Get keys for a given Metadata Category
        Result<List<MetaDataKey>> metaDataKeysResult = getDispatcher().fetchMetaDataKeys(UserCategory.class);

        // Asserting that the Keys fetching finished successfully
        assertTrue(Result.Status.SUCCESS.equals(metaDataKeysResult.getStatus()));
        List<MetaDataKey> metaDataKeys = metaDataKeysResult.get();

        // Assertion if the keys have been generated correctly
        for (MetaDataKey key : metaDataKeys) {
            if (userName == null && key.getId().equals(KEY)) {
                userName = key;
            }
        }
        // Check if we found or not our key
        assertNotNull(userName);

        // Fetch the Metadata for a given Key
        Result<MetaData> accountKeyResult = getDispatcher().fetchMetaData(userName);
        assertTrue(Result.Status.SUCCESS.equals(accountKeyResult.getStatus()));

        // Assert that fetched Metadata is the wanted type
        assertTrue(accountKeyResult.get().getPayload().getDataType().equals(DataType.POJO));

    }
}
