package com.cnts.system.service.impl;

import java.util.List;
import com.cnts.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cnts.system.mapper.TicketLogsMapper;
import com.cnts.system.domain.TicketLogs;
import com.cnts.system.service.ITicketLogsService;

/**
 * 工单流转记录Service业务层处理
 * 
 * @author u679c
 * @date 2025-12-07
 */
@Service
public class TicketLogsServiceImpl implements ITicketLogsService 
{
    @Autowired
    private TicketLogsMapper ticketLogsMapper;

    /**
     * 查询工单流转记录
     * 
     * @param id 工单流转记录主键
     * @return 工单流转记录
     */
    @Override
    public TicketLogs selectTicketLogsById(Long id)
    {
        return ticketLogsMapper.selectTicketLogsById(id);
    }

    /**
     * 查询工单流转记录列表
     * 
     * @param ticketLogs 工单流转记录
     * @return 工单流转记录
     */
    @Override
    public List<TicketLogs> selectTicketLogsList(TicketLogs ticketLogs)
    {
        return ticketLogsMapper.selectTicketLogsList(ticketLogs);
    }

    /**
     * 新增工单流转记录
     * 
     * @param ticketLogs 工单流转记录
     * @return 结果
     */
    @Override
    public int insertTicketLogs(TicketLogs ticketLogs)
    {
        ticketLogs.setCreateTime(DateUtils.getNowDate());
        return ticketLogsMapper.insertTicketLogs(ticketLogs);
    }

    /**
     * 修改工单流转记录
     * 
     * @param ticketLogs 工单流转记录
     * @return 结果
     */
    @Override
    public int updateTicketLogs(TicketLogs ticketLogs)
    {
        return ticketLogsMapper.updateTicketLogs(ticketLogs);
    }

    /**
     * 批量删除工单流转记录
     * 
     * @param ids 需要删除的工单流转记录主键
     * @return 结果
     */
    @Override
    public int deleteTicketLogsByIds(Long[] ids)
    {
        return ticketLogsMapper.deleteTicketLogsByIds(ids);
    }

    /**
     * 删除工单流转记录信息
     * 
     * @param id 工单流转记录主键
     * @return 结果
     */
    @Override
    public int deleteTicketLogsById(Long id)
    {
        return ticketLogsMapper.deleteTicketLogsById(id);
    }
}
