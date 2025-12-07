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
import com.cnts.system.domain.TicketLogs;
import com.cnts.system.service.ITicketLogsService;
import com.cnts.common.utils.poi.ExcelUtil;
import com.cnts.common.core.page.TableDataInfo;

/**
 * 工单流转记录Controller
 * 
 * @author u679c
 * @date 2025-12-07
 */
@RestController
@RequestMapping("/system/ticket_Logs")
public class TicketLogsController extends BaseController
{
    @Autowired
    private ITicketLogsService ticketLogsService;

    /**
     * 查询工单流转记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:ticket_Logs:list')")
    @GetMapping("/list")
    public TableDataInfo list(TicketLogs ticketLogs)
    {
        startPage();
        List<TicketLogs> list = ticketLogsService.selectTicketLogsList(ticketLogs);
        return getDataTable(list);
    }

    /**
     * 导出工单流转记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:ticket_Logs:export')")
    @Log(title = "工单流转记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TicketLogs ticketLogs)
    {
        List<TicketLogs> list = ticketLogsService.selectTicketLogsList(ticketLogs);
        ExcelUtil<TicketLogs> util = new ExcelUtil<TicketLogs>(TicketLogs.class);
        util.exportExcel(response, list, "工单流转记录数据");
    }

    /**
     * 获取工单流转记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:ticket_Logs:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(ticketLogsService.selectTicketLogsById(id));
    }

    /**
     * 新增工单流转记录
     */
    @PreAuthorize("@ss.hasPermi('system:ticket_Logs:add')")
    @Log(title = "工单流转记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TicketLogs ticketLogs)
    {
        return toAjax(ticketLogsService.insertTicketLogs(ticketLogs));
    }

    /**
     * 修改工单流转记录
     */
    @PreAuthorize("@ss.hasPermi('system:ticket_Logs:edit')")
    @Log(title = "工单流转记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TicketLogs ticketLogs)
    {
        return toAjax(ticketLogsService.updateTicketLogs(ticketLogs));
    }

    /**
     * 删除工单流转记录
     */
    @PreAuthorize("@ss.hasPermi('system:ticket_Logs:remove')")
    @Log(title = "工单流转记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(ticketLogsService.deleteTicketLogsByIds(ids));
    }
}
