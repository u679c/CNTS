package com.cnts.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cnts.common.annotation.Excel;
import com.cnts.common.core.domain.BaseEntity;

/**
 * 故障工单对象 tickets
 * 
 * @author u679c
 * @date 2025-12-07
 */
public class Tickets extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工单ID */
    private Long id;

    /** 故障标题 */
    @Excel(name = "故障标题")
    private String title;

    /** 故障描述 */
    @Excel(name = "故障描述")
    private String content;

    /** 类别ID */
    @Excel(name = "类别ID")
    private Long categoryId;

    /** 故障地点 */
    @Excel(name = "故障地点")
    private String location;

    /** 图片JSON，支持多图 */
    @Excel(name = "图片JSON，支持多图")
    private String imgs;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 上报用户ID */
    @Excel(name = "上报用户ID")
    private Long userId;

    /** 处理管理员ID */
    @Excel(name = "处理管理员ID")
    private Long adminId;

    /** 工程师姓名 */
    @Excel(name = "工程师姓名")
    private String engineerName;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setImgs(String imgs) 
    {
        this.imgs = imgs;
    }

    public String getImgs() 
    {
        return imgs;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setAdminId(Long adminId) 
    {
        this.adminId = adminId;
    }

    public Long getAdminId() 
    {
        return adminId;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("categoryId", getCategoryId())
            .append("location", getLocation())
            .append("imgs", getImgs())
            .append("status", getStatus())
            .append("userId", getUserId())
            .append("adminId", getAdminId())
            .append("engineerName", getEngineerName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
