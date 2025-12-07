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
import com.cnts.system.domain.Categories;
import com.cnts.system.service.ICategoriesService;
import com.cnts.common.utils.poi.ExcelUtil;
import com.cnts.common.core.page.TableDataInfo;

/**
 * 故障类别Controller
 * 
 * @author u679c
 * @date 2025-12-07
 */
@RestController
@RequestMapping("/system/categories")
public class CategoriesController extends BaseController
{
    @Autowired
    private ICategoriesService categoriesService;

    /**
     * 查询故障类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:categories:list')")
    @GetMapping("/list")
    public TableDataInfo list(Categories categories)
    {
        startPage();
        List<Categories> list = categoriesService.selectCategoriesList(categories);
        return getDataTable(list);
    }

    /**
     * 导出故障类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:categories:export')")
    @Log(title = "故障类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Categories categories)
    {
        List<Categories> list = categoriesService.selectCategoriesList(categories);
        ExcelUtil<Categories> util = new ExcelUtil<Categories>(Categories.class);
        util.exportExcel(response, list, "故障类别数据");
    }

    /**
     * 获取故障类别详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:categories:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(categoriesService.selectCategoriesById(id));
    }

    /**
     * 新增故障类别
     */
    @PreAuthorize("@ss.hasPermi('system:categories:add')")
    @Log(title = "故障类别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Categories categories)
    {
        return toAjax(categoriesService.insertCategories(categories));
    }

    /**
     * 修改故障类别
     */
    @PreAuthorize("@ss.hasPermi('system:categories:edit')")
    @Log(title = "故障类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Categories categories)
    {
        return toAjax(categoriesService.updateCategories(categories));
    }

    /**
     * 删除故障类别
     */
    @PreAuthorize("@ss.hasPermi('system:categories:remove')")
    @Log(title = "故障类别", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(categoriesService.deleteCategoriesByIds(ids));
    }
}
