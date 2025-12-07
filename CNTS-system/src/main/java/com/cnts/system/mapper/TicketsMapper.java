package com.cnts.system.mapper;

import java.util.List;
import com.cnts.system.domain.Tickets;

/**
 * 故障工单Mapper接口
 * 
 * @author u679c
 * @date 2025-12-07
 */
public interface TicketsMapper 
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
     * 删除故障工单
     * 
     * @param id 故障工单主键
     * @return 结果
     */
    public int deleteTicketsById(Long id);

    /**
     * 批量删除故障工单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTicketsByIds(Long[] ids);
}
