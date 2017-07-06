package com.xfj.mmail.service;

import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.pojo.Category;

import java.util.List;

/**
 * Created by asus on 2017/7/4.
 */
public interface ICategoryService {

    public ServerResponse<String> addCategory(String categoryName,Integer parentId);

    public ServerResponse<String> updateCategoryNameById(String categoryName,Integer categoryId);

    public ServerResponse<List<Category>> getCategoryAndParalleByCategoryId(Integer categoryId);

    public ServerResponse<List<Integer>> getCategoryAndDeepSearchCategoryByCategoryId(Integer categoryId);


}
