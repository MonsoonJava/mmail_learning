package com.xfj.mmail.controller.protial;

import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.common._Const;
import com.xfj.mmail.pojo.User;
import com.xfj.mmail.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by asus on 2017/7/2.
 */
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    public ServerResponse<User> userLogin(String username, String password, HttpSession session){
        ServerResponse<User> userServerResponse = iUserService.doLogin(username, password);
        if(userServerResponse.isSuccess()){
            session.setAttribute(_Const.CURRENT_LOGIN_USER,userServerResponse.getData());
        }
        return userServerResponse;
    }


}
