package com.xfj.mmail.dao;

import com.xfj.mmail.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectPageList();

    List<Product> searchProductByKeywordAndOId(@Param("keyword") String keyword, @Param("productId") Integer productId);

    List<Product> selectProductByKeywordAndCategoryIds(@Param("searchWord") String searchWord,@Param("categoryIdList") List<Integer> categoryIdList);
}