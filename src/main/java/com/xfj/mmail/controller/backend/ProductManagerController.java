package com.xfj.mmail.controller.backend;

import com.github.pagehelper.PageInfo;
import com.xfj.mmail.common.ResopnseCode;
import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.common._Const;
import com.xfj.mmail.pojo.Product;
import com.xfj.mmail.pojo.User;
import com.xfj.mmail.service.IProductService;
import com.xfj.mmail.service.IUploadService;
import com.xfj.mmail.service.IUserService;
import com.xfj.mmail.vo.ProductDetialVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by asus on 2017/7/5.
 */
@Controller
@RequestMapping(value = "/manage/product")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IUploadService iUploadService;


    @RequestMapping(value = "/save_or_update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> insertOrUpdateProduct(HttpSession session,Product product){
        User user =(User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }
    }

    @RequestMapping(value = "/set_sale_statues.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setSaleStatues(HttpSession session, @RequestParam("productId") Integer productId, @RequestParam("statues") Integer statues){
        User user =(User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iProductService.setSaleStatuesWithProductId(productId,statues);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }
    }

    @RequestMapping(value = "/get_product_detial.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetialVo> getProductDetial(HttpSession session,Integer productId){
        User user =(User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iProductService.getProductDetialByProductId(productId);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }
    }


    @RequestMapping(value = "/get_product_list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(HttpSession session,@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user =(User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iProductService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }
    }

    @RequestMapping(value = "/get_search_product_list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> doSearchProductList(HttpSession session,@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                                        @RequestParam("keyword") String keyword,@RequestParam("productId") Integer productId){
        User user =(User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iProductService.searchProductListByKeywordOrProductId(pageNum,pageSize,keyword,productId);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }
    }



    @RequestMapping(value = "/upload.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> upload(HttpSession session, @Param("multipartFile") MultipartFile multipartFile, HttpServletRequest request){
        User user =(User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            String path = session.getServletContext().getRealPath("upload");
            return iUploadService.uploadFile(path, multipartFile);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to upload file");
        }
    }
}
