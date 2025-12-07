package com.cnts.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cnts.common.annotation.Log;
import com.cnts.common.core.controller.BaseController;
import com.cnts.common.core.domain.AjaxResult;
import com.cnts.common.enums.BusinessType;
import com.cnts.system.domain.Tickets;
import com.cnts.system.domain.TicketAssignDTO;
import com.cnts.system.service.ITicketsService;
import com.cnts.common.utils.poi.ExcelUtil;
import com.cnts.common.core.page.TableDataInfo;

/**
 * 故障工单Controller
 * @author u679c
 * @date 2025-12-07
 */
@RestController
@RequestMapping("/system/tickets")
public class TicketsController extends BaseController
{
    @Autowired
    private ITicketsService ticketsService;

    /**
     * 查询故障工单列表
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:list')")
    @GetMapping("/list")
    public TableDataInfo list(Tickets tickets)
    {
        startPage();
        List<Tickets> list = ticketsService.selectTicketsList(tickets);
        return getDataTable(list);
    }

    /**
     * 导出故障工单列表
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:export')")
    @Log(title = "故障工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Tickets tickets)
    {
        List<Tickets> list = ticketsService.selectTicketsList(tickets);
        ExcelUtil<Tickets> util = new ExcelUtil<Tickets>(Tickets.class);
        util.exportExcel(response, list, "故障工单数据");
    }

    /**
     * 获取故障工单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(ticketsService.selectTicketsById(id));
    }

    /**
     * 新增故障工单
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:add')")
    @Log(title = "故障工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Tickets tickets)
    {
        return toAjax(ticketsService.insertTickets(tickets));
    }

    /**
     * 受理工单
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:edit')")
    @Log(title = "故障工单", businessType = BusinessType.UPDATE)
    @PostMapping("/accept/{id}")
    public AjaxResult accept(@PathVariable Long id)
    {
        return toAjax(ticketsService.updateStatus(id, 1L, "受理工单"));
    }

    /**
     * 指派工单
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:edit')")
    @Log(title = "故障工单", businessType = BusinessType.UPDATE)
    @PostMapping("/assign")
    public AjaxResult assign(@RequestBody TicketAssignDTO dto)
    {
        return toAjax(ticketsService.assign(dto.getTicketId(), dto.getAdminId()));
    }

    /**
     * 标记处理中
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:edit')")
    @Log(title = "故障工单", businessType = BusinessType.UPDATE)
    @PostMapping("/processing/{id}")
    public AjaxResult processing(@PathVariable Long id)
    {
        return toAjax(ticketsService.updateStatus(id, 2L, "处理中"));
    }

    /**
     * 完成工单
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:edit')")
    @Log(title = "故障工单", businessType = BusinessType.UPDATE)
    @PostMapping("/finish/{id}")
    public AjaxResult finish(@PathVariable Long id)
    {
        return toAjax(ticketsService.updateStatus(id, 3L, "已完成"));
    }

    /**
     * 修改故障工单
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:edit')")
    @Log(title = "故障工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Tickets tickets)
    {
        return toAjax(ticketsService.updateTickets(tickets));
    }

    /**
     * 删除故障工单
     */
    @PreAuthorize("@ss.hasPermi('system:tickets:remove')")
    @Log(title = "故障工单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(ticketsService.deleteTicketsByIds(ids));
    }
}
