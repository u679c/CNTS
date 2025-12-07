package com.cnts.system.service;

import java.util.List;
import com.cnts.system.domain.Tickets;

/**
 * 故障工单Service接口
 * 
 * @author u679c
 * @date 2025-12-07
 */
public interface ITicketsService 
{
    /**
     * 查询故障工单
     * 
     * @param id 故障工单主键
     * @return 故障工单
     */
    public Tickets selectTicketsById(Long id);

    /**
     * 查询故障工单列表
     * 
     * @param tickets 故障工单
     * @return 故障工单集合
     */
    public List<Tickets> selectTicketsList(Tickets tickets);

    /**
     * 新增故障工单
     * 
     * @param tickets 故障工单
     * @return 结果
     */
    public int insertTickets(Tickets tickets);

    /**
     * 修改故障工单
     * 
     * @param tickets 故障工单
     * @return 结果
     */
    public int updateTickets(Tickets tickets);

    /**
     * 更新工单状态并记录操作日志
     *
     * @param ticketId 工单ID
     * @param status 新状态
     * @param action 日志动作描述
     * @return 结果
     */
    public int updateStatus(Long ticketId, Long status, String action);

    /**
     * 指派工单给管理员并记录日志
     *
     * @param ticketId 工单ID
     * @param adminId 管理员ID
     * @return 结果
     */
    public int assign(Long ticketId, Long adminId);

    /**
     * 批量删除故障工单
     * 
     * @param ids 需要删除的故障工单主键集合
     * @return 结果
     */
    public int deleteTicketsByIds(Long[] ids);

    /**
     * 删除故障工单信息
     * 
     * @param id 故障工单主键
     * @return 结果
     */
    public int deleteTicketsById(Long id);
}
