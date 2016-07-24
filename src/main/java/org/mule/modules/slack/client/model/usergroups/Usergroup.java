/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.client.model.usergroups;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Usergroup {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("is_usergroup")
    @Expose
    private Boolean isUsergroup;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("is_external")
    @Expose
    private Boolean isExternal;
    @SerializedName("date_create")
    @Expose
    private Integer dateCreate;
    @SerializedName("date_update")
    @Expose
    private Integer dateUpdate;
    @SerializedName("date_delete")
    @Expose
    private Integer dateDelete;
    @SerializedName("auto_type")
    @Expose
    private Object autoType;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("deleted_by")
    @Expose
    private Object deletedBy;
    @SerializedName("prefs")
    @Expose
    private Prefs prefs;
    @SerializedName("user_count")
    @Expose
    private String userCount;

    /**
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The teamId
     */
    public String getTeamId() {
        return teamId;
    }

    /**
     *
     * @param teamId
     *     The team_id
     */
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    /**
     *
     * @return
     *     The isUsergroup
     */
    public Boolean getIsUsergroup() {
        return isUsergroup;
    }

    /**
     *
     * @param isUsergroup
     *     The is_usergroup
     */
    public void setIsUsergroup(Boolean isUsergroup) {
        this.isUsergroup = isUsergroup;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     *     The handle
     */
    public String getHandle() {
        return handle;
    }

    /**
     *
     * @param handle
     *     The handle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     *
     * @return
     *     The isExternal
     */
    public Boolean getIsExternal() {
        return isExternal;
    }

    /**
     *
     * @param isExternal
     *     The is_external
     */
    public void setIsExternal(Boolean isExternal) {
        this.isExternal = isExternal;
    }

    /**
     *
     * @return
     *     The dateCreate
     */
    public Integer getDateCreate() {
        return dateCreate;
    }

    /**
     *
     * @param dateCreate
     *     The date_create
     */
    public void setDateCreate(Integer dateCreate) {
        this.dateCreate = dateCreate;
    }

    /**
     *
     * @return
     *     The dateUpdate
     */
    public Integer getDateUpdate() {
        return dateUpdate;
    }

    /**
     *
     * @param dateUpdate
     *     The date_update
     */
    public void setDateUpdate(Integer dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    /**
     *
     * @return
     *     The dateDelete
     */
    public Integer getDateDelete() {
        return dateDelete;
    }

    /**
     *
     * @param dateDelete
     *     The date_delete
     */
    public void setDateDelete(Integer dateDelete) {
        this.dateDelete = dateDelete;
    }

    /**
     *
     * @return
     *     The autoType
     */
    public Object getAutoType() {
        return autoType;
    }

    /**
     *
     * @param autoType
     *     The auto_type
     */
    public void setAutoType(Object autoType) {
        this.autoType = autoType;
    }

    /**
     *
     * @return
     *     The createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     *     The created_by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     *     The updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     *
     * @param updatedBy
     *     The updated_by
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     *
     * @return
     *     The deletedBy
     */
    public Object getDeletedBy() {
        return deletedBy;
    }

    /**
     *
     * @param deletedBy
     *     The deleted_by
     */
    public void setDeletedBy(Object deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     *
     * @return
     *     The prefs
     */
    public Prefs getPrefs() {
        return prefs;
    }

    /**
     *
     * @param prefs
     *     The prefs
     */
    public void setPrefs(Prefs prefs) {
        this.prefs = prefs;
    }

    /**
     *
     * @return
     *     The userCount
     */
    public String getUserCount() {
        return userCount;
    }

    /**
     *
     * @param userCount
     *     The user_count
     */
    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

}
