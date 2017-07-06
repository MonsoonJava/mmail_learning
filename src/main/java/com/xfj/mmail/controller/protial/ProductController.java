package com.xfj.mmail.controller.protial;

import com.github.pagehelper.PageInfo;
import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.pojo.Product;
import com.xfj.mmail.service.IProductService;
import com.xfj.mmail.vo.ProductDetialVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.invoke.util.VerifyAccess;

/**
 * Created by asus on 2017/7/6.
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;



    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetialVo> getProductDetial(@RequestParam("productId") Integer productId){
        return iProductService.selectProductByProductId(productId);
    }


    @RequestMapping(value = "search.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> searcgProductByKeywordAndCategory(@RequestParam(value = "keyword",required = false)String keyword,
                                                                      @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                                                      @RequestParam(value = "pageNum",defaultValue = "0")Integer pageNum,
                                                                      @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                                                      @RequestParam(value = "orderBy",defaultValue = " ") String orderBy){
        return iProductService.searchProductsByKeywordOrProductId(keyword,categoryId,pageNum,pageSize,orderBy);
    }

}
