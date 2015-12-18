package org.mule.modules.slack.functional;

import org.junit.Test;
import org.mule.common.Result;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
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
    @Test
    @MetaDataTest
    public void testGetMetaDataKeys() throws Exception {

        MetaDataKey userName = null;

        Result<List<MetaDataKey>> metaDataKeysResult = getDispatcher().fetchMetaDataKeys(UserCategory.class);

        assertTrue(Result.Status.SUCCESS.equals(metaDataKeysResult.getStatus()));
        List<MetaDataKey> metaDataKeys = metaDataKeysResult.get();

        for (MetaDataKey key : metaDataKeys) {
            if (userName == null && key.getId().equals("U03NE28RL")) {
                userName = key;
            }
        }

        assertNotNull(userName);

        Result<MetaData> accountKeyResult = getDispatcher().fetchMetaData(userName);
        assertTrue(Result.Status.SUCCESS.equals(accountKeyResult.getStatus()));


    }
}
