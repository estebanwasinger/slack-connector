/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.client.utils;

/**
 * Created by estebanwasinger on 6/19/15.
 */
public enum SortDirection {

    Ascending("asc"), Descending("desc");
    private String value;

    private SortDirection(String value) {
        this.value = value;
    }

    public String toString(){
        return value;
    }
}
