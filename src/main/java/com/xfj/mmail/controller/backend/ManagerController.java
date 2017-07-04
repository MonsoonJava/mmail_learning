package com.xfj.mmail.controller.backend;

import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.common._Const;
import com.xfj.mmail.pojo.User;
import com.xfj.mmail.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by asus on 2017/7/4.
 */
@Controller
@RequestMapping(value = "/manager/user")
public class ManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    public ServerResponse<User> managerLogin(HttpSession session,@RequestParam(value = "username") String username, @RequestParam(value = "password")String password){
        ServerResponse<User> userServerResponse = iUserService.doLogin(username, password);
        if(userServerResponse.isSuccess()){
            if(userServerResponse.getData().getRole() == _Const.Role.ADMIN){
                session.setAttribute(_Const.CURRENT_LOGIN_USER,userServerResponse.getData());
                return userServerResponse;
            }else {
                return ServerResponse.getServerResponseErrorMessage("the user do not have admin role,error login");
            }
        }
        return userServerResponse;
    }

}
