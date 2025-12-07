package com.cnts.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cnts.common.annotation.Excel;
import com.cnts.common.core.domain.BaseEntity;

/**
 * 工单流转记录对象 ticket_logs
 * 
 * @author u679c
 * @date 2025-12-07
 */
public class TicketLogs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long id;

    /** 工单ID */
    @Excel(name = "工单ID")
    private Long ticketId;

    /** 动作 */
    @Excel(name = "动作")
    private String action;

    /** 操作人ID */
    @Excel(name = "操作人ID")
    private Long operatorId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTicketId(Long ticketId) 
    {
        this.ticketId = ticketId;
    }

    public Long getTicketId() 
    {
        return ticketId;
    }

    public void setAction(String action) 
    {
        this.action = action;
    }

    public String getAction() 
    {
        return action;
    }

    public void setOperatorId(Long operatorId) 
    {
        this.operatorId = operatorId;
    }

    public Long getOperatorId() 
    {
        return operatorId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ticketId", getTicketId())
            .append("action", getAction())
            .append("operatorId", getOperatorId())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .toString();
    }
}
