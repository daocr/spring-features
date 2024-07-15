package org.example.generator.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 栗子队列
 * @TableName leads_group
 */
public class LeadsGroup implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 类型
     */
    private String groupType;

    /**
     * 队列名称
     */
    private String name;

    /**
     * 标签
     */
    private Object tags;

    /**
     * 来源
     */
    private Object fform;

    /**
     * 渠道
     */
    private Object channel;

    /**
     * 设置
     */
    private Object preference;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 类型
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * 类型
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    /**
     * 队列名称
     */
    public String getName() {
        return name;
    }

    /**
     * 队列名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 标签
     */
    public Object getTags() {
        return tags;
    }

    /**
     * 标签
     */
    public void setTags(Object tags) {
        this.tags = tags;
    }

    /**
     * 来源
     */
    public Object getFform() {
        return fform;
    }

    /**
     * 来源
     */
    public void setFform(Object fform) {
        this.fform = fform;
    }

    /**
     * 渠道
     */
    public Object getChannel() {
        return channel;
    }

    /**
     * 渠道
     */
    public void setChannel(Object channel) {
        this.channel = channel;
    }

    /**
     * 设置
     */
    public Object getPreference() {
        return preference;
    }

    /**
     * 设置
     */
    public void setPreference(Object preference) {
        this.preference = preference;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 是否删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 是否删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LeadsGroup other = (LeadsGroup) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupType() == null ? other.getGroupType() == null : this.getGroupType().equals(other.getGroupType()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getTags() == null ? other.getTags() == null : this.getTags().equals(other.getTags()))
            && (this.getFform() == null ? other.getFform() == null : this.getFform().equals(other.getFform()))
            && (this.getChannel() == null ? other.getChannel() == null : this.getChannel().equals(other.getChannel()))
            && (this.getPreference() == null ? other.getPreference() == null : this.getPreference().equals(other.getPreference()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupType() == null) ? 0 : getGroupType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getTags() == null) ? 0 : getTags().hashCode());
        result = prime * result + ((getFform() == null) ? 0 : getFform().hashCode());
        result = prime * result + ((getChannel() == null) ? 0 : getChannel().hashCode());
        result = prime * result + ((getPreference() == null) ? 0 : getPreference().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupType=").append(groupType);
        sb.append(", name=").append(name);
        sb.append(", tags=").append(tags);
        sb.append(", fform=").append(fform);
        sb.append(", channel=").append(channel);
        sb.append(", preference=").append(preference);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}