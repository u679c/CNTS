package com.cnts.system.service;

import java.util.List;
import com.cnts.system.domain.TicketLogs;

/**
 * 工单流转记录Service接口
 * 
 * @author u679c
 * @date 2025-12-07
 */
public interface ITicketLogsService 
{
    /**
     * 查询工单流转记录
     * 
     * @param id 工单流转记录主键
     * @return 工单流转记录
     */
    public TicketLogs selectTicketLogsById(Long id);

    /**
     * 查询工单流转记录列表
     * 
     * @param ticketLogs 工单流转记录
     * @return 工单流转记录集合
     */
    public List<TicketLogs> selectTicketLogsList(TicketLogs ticketLogs);

    /**
     * 新增工单流转记录
     * 
     * @param ticketLogs 工单流转记录
     * @return 结果
     */
    public int insertTicketLogs(TicketLogs ticketLogs);

    /**
     * 修改工单流转记录
     * 
     * @param ticketLogs 工单流转记录
     * @return 结果
     */
    public int updateTicketLogs(TicketLogs ticketLogs);

    /**
     * 批量删除工单流转记录
     * 
     * @param ids 需要删除的工单流转记录主键集合
     * @return 结果
     */
    public int deleteTicketLogsByIds(Long[] ids);

    /**
     * 删除工单流转记录信息
     * 
     * @param id 工单流转记录主键
     * @return 结果
     */
    public int deleteTicketLogsById(Long id);
}
