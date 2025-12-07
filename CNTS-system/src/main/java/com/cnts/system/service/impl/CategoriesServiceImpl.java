package com.cnts.system.service.impl;

import java.util.List;
import com.cnts.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cnts.system.mapper.CategoriesMapper;
import com.cnts.system.domain.Categories;
import com.cnts.system.service.ICategoriesService;

/**
 * 故障类别Service业务层处理
 * 
 * @author u679c
 * @date 2025-12-07
 */
@Service
public class CategoriesServiceImpl implements ICategoriesService 
{
    @Autowired
    private CategoriesMapper categoriesMapper;

    /**
     * 查询故障类别
     * 
     * @param id 故障类别主键
     * @return 故障类别
     */
    @Override
    public Categories selectCategoriesById(Long id)
    {
        return categoriesMapper.selectCategoriesById(id);
    }

    /**
     * 查询故障类别列表
     * 
     * @param categories 故障类别
     * @return 故障类别
     */
    @Override
    public List<Categories> selectCategoriesList(Categories categories)
    {
        return categoriesMapper.selectCategoriesList(categories);
    }

    /**
     * 新增故障类别
     * 
     * @param categories 故障类别
     * @return 结果
     */
    @Override
    public int insertCategories(Categories categories)
    {
        categories.setCreateTime(DateUtils.getNowDate());
        return categoriesMapper.insertCategories(categories);
    }

    /**
     * 修改故障类别
     * 
     * @param categories 故障类别
     * @return 结果
     */
    @Override
    public int updateCategories(Categories categories)
    {
        categories.setUpdateTime(DateUtils.getNowDate());
        return categoriesMapper.updateCategories(categories);
    }

    /**
     * 批量删除故障类别
     * 
     * @param ids 需要删除的故障类别主键
     * @return 结果
     */
    @Override
    public int deleteCategoriesByIds(Long[] ids)
    {
        return categoriesMapper.deleteCategoriesByIds(ids);
    }

    /**
     * 删除故障类别信息
     * 
     * @param id 故障类别主键
     * @return 结果
     */
    @Override
    public int deleteCategoriesById(Long id)
    {
        return categoriesMapper.deleteCategoriesById(id);
    }
}
