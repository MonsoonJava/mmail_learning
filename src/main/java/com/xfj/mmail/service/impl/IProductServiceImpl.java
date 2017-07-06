package com.xfj.mmail.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xfj.mmail.common.ResopnseCode;
import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.common._Const;
import com.xfj.mmail.dao.CategoryMapper;
import com.xfj.mmail.dao.ProductMapper;
import com.xfj.mmail.pojo.Category;
import com.xfj.mmail.pojo.Product;
import com.xfj.mmail.service.IProductService;
import com.xfj.mmail.utils.DateTimeUtil;
import com.xfj.mmail.utils.PropertiesUtil;
import com.xfj.mmail.vo.ProductDetialVo;
import com.xfj.mmail.vo.ProductPageVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by asus on 2017/7/5.
 */
@Service("iProductService")
public class IProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if(null == product)
           return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.ILLEGAL_ARGUMENTS.getCode(),"insert or update product arguments wrong");
        if(null == product.getId()){
            // id not exist to insert the product
            int rowCount = productMapper.insert(product);
            if(rowCount > 0){
                return ServerResponse.getServerResponseSuccessMessage("insert product success");
            }else {
                return ServerResponse.getServerResponseErrorMessage("insert product failed");
            }
        }else{
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if(rowCount > 0){
                return ServerResponse.getServerResponseSuccessMessage("update product success");
            }else {
                return ServerResponse.getServerResponseErrorMessage("update product failed");
            }
        }
    }

    @Override
    public ServerResponse<String> setSaleStatuesWithProductId(Integer productId, Integer statues) {
        if(null == productId || null == statues )
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.ILLEGAL_ARGUMENTS.getCode(),"the arguments to set sale status is wrong");
        Product product = new Product();
        product.setId(productId);
        product.setStatus(statues);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0)
            return ServerResponse.getServerResponseSuccessMessage("set sale status success");
        return ServerResponse.getServerResponseSuccessMessage("set sale status failed");
    }


    @Override
    public ServerResponse<ProductDetialVo> getProductDetialByProductId(Integer productId) {
        if(null == productId )
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.ILLEGAL_ARGUMENTS.getCode(),"the product arguments is blank,can find any product");
        Product product = productMapper.selectByPrimaryKey(productId);
        if(null == product){
            return ServerResponse.getServerResponseErrorMessage("the product has set down or deletes");
        }
        return this.translateProductToProductDetialVo(product);
    }

    private ServerResponse<ProductDetialVo> translateProductToProductDetialVo(Product product){
        ProductDetialVo productDetialVo = new ProductDetialVo();
        productDetialVo.setId(product.getId());
        productDetialVo.setCategoryId(product.getCategoryId());
        productDetialVo.setName(product.getName());
        productDetialVo.setDetail(product.getDetail());
        productDetialVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productDetialVo.setPrice(product.getPrice());
        productDetialVo.setSubtitle(product.getSubtitle());
        productDetialVo.setStock(product.getStock());
        productDetialVo.setStatus(product.getStatus());
        productDetialVo.setMainImage(product.getMainImage());
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(null == category){
            productDetialVo.setParentCategoryId(0);
        }else {
            productDetialVo.setParentCategoryId(category.getParentId());
        }
        if(null == product.getSubImages()){
            String[] images = product.getMainImage().split(",");
            productDetialVo.setSubImages(images[0]);
        }else{
            productDetialVo.setSubImages(product.getSubImages());
        }
        productDetialVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetialVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return ServerResponse.getServerResponseSuccess(productDetialVo);
    }


    @Override
    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
        if(null == pageNum || null == pageSize || pageNum < 0 || pageSize < 0) {
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.ILLEGAL_ARGUMENTS.getCode(),"the pageNum or pageSize argument wrong");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectPageList();
        List<ProductPageVo> productPageVoList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(productList)){
            for(Product product : productList){
                productPageVoList.add(this.translateProductToPageVo(product));
            }
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productPageVoList);
        return ServerResponse.getServerResponseSuccess(pageInfo);
    }


    @Override
    public ServerResponse<PageInfo> searchProductListByKeywordOrProductId(Integer pageNum, Integer pageSize, String keyword, Integer productId) {
        if(null == pageNum || null == pageSize || pageNum < 0 || pageSize < 0) {
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.ILLEGAL_ARGUMENTS.getCode(),"the pageNum or pageSize argument wrong");
        }
        String theSearchKey =  new StringBuilder("").append("%").append(keyword.trim()).append("%").toString();
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.searchProductByKeywordAndOId(keyword,productId);
        List<ProductPageVo> searchProductList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(productList)){
            for(Product product : productList){
                searchProductList.add(this.translateProductToPageVo(product));
            }
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(searchProductList);
        return ServerResponse.getServerResponseSuccess(pageInfo);
    }

    private ProductPageVo translateProductToPageVo(Product product){
        ProductPageVo productPageVo = new ProductPageVo();
        productPageVo.setId(product.getId());
        productPageVo.setCategoryId(product.getCategoryId());
        productPageVo.setName(product.getName());
        productPageVo.setSubtitle(product.getSubtitle());
        productPageVo.setMainImage(product.getMainImage());
        productPageVo.setStatus(product.getStatus());
        productPageVo.setPrice(product.getPrice());
        productPageVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        return  productPageVo;
    }


    @Override
    public ServerResponse<ProductDetialVo> selectProductByProductId(Integer productId) {
        if( null == productId)
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.ILLEGAL_ARGUMENTS.getCode(),"productId argument must not blank");
        Product product = productMapper.selectByPrimaryKey(productId);
        if(null == product){
            return ServerResponse.getServerResponseErrorMessage("the product is not exits or deleted");
        }
        if(product.getStatus() == _Const.PRODUCT_STATUS_ON){
            return this.translateProductToProductDetialVo(product);
        }
        return ServerResponse.getServerResponseErrorMessage("the product is not exits or deleted");
    }

    @Override
    public ServerResponse<PageInfo> searchProductsByKeywordOrProductId(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {
        if(StringUtils.isBlank(keyword) || null == categoryId ){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.ILLEGAL_ARGUMENTS.getCode(),ResopnseCode.ILLEGAL_ARGUMENTS.getDesc());
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category == null &&  StringUtils.isBlank(keyword)){
            //没有对应的分类，且不存在查询关键字,返回空
            List<ProductDetialVo>  returnList = Lists.newArrayList();
            PageInfo pageInfo = new PageInfo(returnList);
            return ServerResponse.getServerResponseSuccess(pageInfo);
        }
        pageNum = pageNum < 0 ? 0 : pageNum;
        pageSize = pageSize < 0 ? 0 : pageSize;
        List<Integer> categoryIdList = Lists.newArrayList();
        if(null != categoryId){
            categoryIdList.add(categoryId);
        }
        List<Category> categoryList = categoryMapper.getCategoryAndParalleByCategoryId(categoryId);
        if(CollectionUtils.isNotEmpty(categoryIdList)){
            for(Category item : categoryList){
                categoryIdList.add(item.getId());
            }
        }
        String searchKey = null;
        if(StringUtils.isNotBlank(keyword)){
            searchKey = new StringBuilder("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(orderBy) && _Const.ORDERBY.PRICE_ASC_DESC.contains(orderBy)){
            String[] orderRole = orderBy.split("_");
            PageHelper.orderBy(orderRole[0] + " " + orderRole[1]);
        }
        List<Product> products = productMapper.selectProductByKeywordAndCategoryIds(StringUtils.isBlank(keyword) ? null : searchKey, categoryIdList.size() == 0 ? null : categoryIdList);
        PageInfo pageInfo = new PageInfo(products);
        List<ProductPageVo> productPageVoList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(products)){
            for(Product product : products){
                productPageVoList.add(this.translateProductToPageVo(product));
            }
        }
        pageInfo.setList(productPageVoList);
        return ServerResponse.getServerResponseSuccess(pageInfo);
    }
}
