package com.cnts.system.service;

import java.util.List;
import com.cnts.system.domain.Categories;

/**
 * 故障类别Service接口
 * 
 * @author u679c
 * @date 2025-12-07
 */
public interface ICategoriesService 
{
    /**
     * 查询故障类别
     * 
     * @param id 故障类别主键
     * @return 故障类别
     */
    public Categories selectCategoriesById(Long id);

    /**
     * 查询故障类别列表
     * 
     * @param categories 故障类别
     * @return 故障类别集合
     */
    public List<Categories> selectCategoriesList(Categories categories);

    /**
     * 新增故障类别
     * 
     * @param categories 故障类别
     * @return 结果
     */
    public int insertCategories(Categories categories);

    /**
     * 修改故障类别
     * 
     * @param categories 故障类别
     * @return 结果
     */
    public int updateCategories(Categories categories);

    /**
     * 批量删除故障类别
     * 
     * @param ids 需要删除的故障类别主键集合
     * @return 结果
     */
    public int deleteCategoriesByIds(Long[] ids);

    /**
     * 删除故障类别信息
     * 
     * @param id 故障类别主键
     * @return 结果
     */
    public int deleteCategoriesById(Long id);
}
