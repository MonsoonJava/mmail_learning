package com.xfj.mmail.service.impl;

import com.google.common.collect.Lists;
import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.dao.CategoryMapper;
import com.xfj.mmail.pojo.Category;
import com.xfj.mmail.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by asus on 2017/7/4.
 */
@Service(value = "iCategoryService")
public class ICategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if(null == parentId || StringUtils.isBlank(categoryName))
            return ServerResponse.getServerResponseErrorMessage("the param of add category is wrong");
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int rowCount = categoryMapper.insertSelective(category);
        if(rowCount > 1){
            return ServerResponse.getServerResponseSuccessMessage("add category success");
        }
        return  ServerResponse.getServerResponseErrorMessage("add category failed");
    }


    @Override
    public ServerResponse<String> updateCategoryNameById(String categoryName, Integer categoryId) {
        if(null == categoryId || StringUtils.isBlank(categoryName))
            return ServerResponse.getServerResponseErrorMessage("the param of add category is wrong");
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 1){
            return ServerResponse.getServerResponseSuccessMessage("update category name success");
        }
        return  ServerResponse.getServerResponseErrorMessage("update category name failed");
    }

    @Override
    public ServerResponse<List<Category>> getCategoryAndParalleByCategoryId(Integer categoryId) {
        List<Category> categoryList = categoryMapper.getCategoryAndParalleByCategoryId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            return ServerResponse.getServerResponseErrorMessage("can not find the categorys");
        }
        return ServerResponse.getServerResponseSuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> getCategoryAndDeepSearchCategoryByCategoryId(Integer categoryId) {
        if(null == categoryId)
            return ServerResponse.getServerResponseErrorMessage("the categoryId can not null");
        Set<Category> categorySet = new HashSet<>();
        getAllCategoryByCategoryWithRecursion(categorySet, categoryId);
        if(CollectionUtils.isEmpty(categorySet)){
            return  ServerResponse.getServerResponseErrorMessage("can not find any category by the categoryId");
        }
        List<Integer> categoryList = new ArrayList<>();
        for(Category item : categorySet){
            categoryList.add(item.getId());
        }
        return ServerResponse.getServerResponseSuccess(categoryList);
    }

    private Set<Category> getAllCategoryByCategoryWithRecursion(Set<Category> categorySet, Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(null != category){
            categorySet.add(category);
        }
        if(null != categoryId){
            List<Category> categoryList = categoryMapper.getCategoryAndParalleByCategoryId(categoryId);
            if(CollectionUtils.isNotEmpty(categoryList)){
                for(Category categoryItem : categoryList){
                    getAllCategoryByCategoryWithRecursion(categorySet, categoryItem.getId());
                }
            }
        }
        return categorySet;
    }
}
