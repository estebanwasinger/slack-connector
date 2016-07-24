/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client.model.usergroups;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Prefs {

    @SerializedName("channels")
    @Expose
    private List<Object> channels = new ArrayList<Object>();
    @SerializedName("groups")
    @Expose
    private List<Object> groups = new ArrayList<Object>();

    /**
     *
     * @return
     *     The channels
     */
    public List<Object> getChannels() {
        return channels;
    }

    /**
     *
     * @param channels
     *     The channels
     */
    public void setChannels(List<Object> channels) {
        this.channels = channels;
    }

    /**
     *
     * @return
     *     The groups
     */
    public List<Object> getGroups() {
        return groups;
    }

    /**
     *
     * @param groups
     *     The groups
     */
    public void setGroups(List<Object> groups) {
        this.groups = groups;
    }

}
