package com.cnts.system.service.impl;

import java.util.List;
import com.cnts.common.utils.DateUtils;
import com.cnts.common.utils.SecurityUtils;
import com.cnts.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cnts.system.mapper.TicketsMapper;
import com.cnts.system.mapper.TicketLogsMapper;
import com.cnts.system.domain.Tickets;
import com.cnts.system.domain.TicketLogs;
import com.cnts.system.service.ITicketsService;

/**
 * 故障工单Service业务层处理
 * 
 * @author u679c
 * @date 2025-12-07
 */
@Service
public class TicketsServiceImpl implements ITicketsService 
{
    @Autowired
    private TicketsMapper ticketsMapper;

    @Autowired
    private TicketLogsMapper ticketLogsMapper;

    /**
     * 查询故障工单
     * 
     * @param id 故障工单主键
     * @return 故障工单
     */
    @Override
    public Tickets selectTicketsById(Long id)
    {
        return ticketsMapper.selectTicketsById(id);
    }

    /**
     * 查询故障工单列表
     * 
     * @param tickets 故障工单
     * @return 故障工单
     */
    @Override
    public List<Tickets> selectTicketsList(Tickets tickets)
    {
        return ticketsMapper.selectTicketsList(tickets);
    }

    /**
     * 新增故障工单
     * 
     * @param tickets 故障工单
     * @return 结果
     */
    @Override
    public int insertTickets(Tickets tickets)
    {
        tickets.setCreateTime(DateUtils.getNowDate());
        return ticketsMapper.insertTickets(tickets);
    }

    /**
     * 修改故障工单
     * 
     * @param tickets 故障工单
     * @return 结果
     */
    @Override
    public int updateTickets(Tickets tickets)
    {
        tickets.setUpdateTime(DateUtils.getNowDate());
        return ticketsMapper.updateTickets(tickets);
    }

    @Override
    public int updateStatus(Long ticketId, Long status, String action)
    {
        Tickets ticket = ticketsMapper.selectTicketsById(ticketId);
        if (StringUtils.isNull(ticket))
        {
            return 0;
        }
        ticket.setStatus(status);
        ticket.setUpdateBy(SecurityUtils.getUsername());
        ticket.setUpdateTime(DateUtils.getNowDate());
        int rows = ticketsMapper.updateTickets(ticket);
        recordLog(ticketId, action);
        return rows;
    }

    @Override
    public int assign(Long ticketId, Long adminId)
    {
        Tickets ticket = ticketsMapper.selectTicketsById(ticketId);
        if (StringUtils.isNull(ticket))
        {
            return 0;
        }
        ticket.setAdminId(adminId);
        ticket.setStatus(2L);
        ticket.setUpdateBy(SecurityUtils.getUsername());
        ticket.setUpdateTime(DateUtils.getNowDate());
        int rows = ticketsMapper.updateTickets(ticket);
        recordLog(ticketId, "指派工单");
        return rows;
    }

    private void recordLog(Long ticketId, String action)
    {
        TicketLogs log = new TicketLogs();
        log.setTicketId(ticketId);
        log.setAction(action);
        log.setOperatorId(SecurityUtils.getUserId());
        log.setCreateTime(DateUtils.getNowDate());
        ticketLogsMapper.insertTicketLogs(log);
    }

    /**
     * 批量删除故障工单
     * 
     * @param ids 需要删除的故障工单主键
     * @return 结果
     */
    @Override
    public int deleteTicketsByIds(Long[] ids)
    {
        return ticketsMapper.deleteTicketsByIds(ids);
    }

    /**
     * 删除故障工单信息
     * 
     * @param id 故障工单主键
     * @return 结果
     */
    @Override
    public int deleteTicketsById(Long id)
    {
        return ticketsMapper.deleteTicketsById(id);
    }
}
