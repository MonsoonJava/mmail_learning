package com.xfj.mmail.service;

import com.github.pagehelper.PageInfo;
import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.pojo.Product;
import com.xfj.mmail.vo.ProductDetialVo;

import java.util.List;

/**
 * Created by asus on 2017/7/5.
 */
public interface IProductService {

    public ServerResponse<String> saveOrUpdateProduct(Product product);

    public ServerResponse<String> setSaleStatuesWithProductId(Integer productId,Integer statues);

    public ServerResponse<ProductDetialVo> getProductDetialByProductId(Integer productId);

    public ServerResponse<PageInfo> getProductList(Integer pageNum,Integer pageSize);

    public  ServerResponse<PageInfo> searchProductListByKeywordOrProductId(Integer pageNum,Integer pageSize,String keyword,Integer productId);

    public ServerResponse<ProductDetialVo> selectProductByProductId(Integer productId);

    public ServerResponse<PageInfo> searchProductsByKeywordOrProductId(String keyword,Integer categoryId,Integer pageNum,Integer pageSize,String orderBy);

}
