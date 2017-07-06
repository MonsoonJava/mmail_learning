package com.xfj.mmail.controller.backend;

import com.xfj.mmail.common.ResopnseCode;
import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.common._Const;
import com.xfj.mmail.pojo.Category;
import com.xfj.mmail.pojo.User;
import com.xfj.mmail.service.ICategoryService;
import com.xfj.mmail.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.net.ResponseCache;
import java.util.List;

/**
 * Created by asus on 2017/7/4.
 */

@Controller
@RequestMapping(value = "/manage/category")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "/add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategoryNode(HttpSession session, @RequestParam("categoryName") String categoryName,
                                                  @RequestParam(value = "parentId",defaultValue ="0") Integer parentId){
        User user = (User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iCategoryService.addCategory(categoryName, parentId);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }
    }

    @RequestMapping(value = "/set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session ,@RequestParam("categoryName")String categoryName,@RequestParam("categoryId") Integer categoryId){
        User user = (User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iCategoryService.updateCategoryNameById(categoryName, categoryId);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }
    }

    @RequestMapping(value = "/get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getCategoryAndParalleByCategoryId(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iCategoryService.getCategoryAndParalleByCategoryId(categoryId);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }
    }

    @RequestMapping(value = "/get_deep_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Integer>> getCategoryAndDeepSearchCategoryByCategoryId(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == user){
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login in");
        }
        //检查当前用户是否有管理员权限    }
        ServerResponse<String> checkLoginUserRole = iUserService.checkLoginUserRole(user, _Const.Role.ADMIN);
        if(checkLoginUserRole.isSuccess()){
            return iCategoryService.getCategoryAndDeepSearchCategoryByCategoryId(categoryId);
        }else{
            return ServerResponse.getServerResponseErrorMessage("login user do not have the role to add category");
        }

    }
}
