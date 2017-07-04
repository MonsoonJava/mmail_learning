package com.xfj.mmail.controller.protial;

import com.xfj.mmail.common.ResopnseCode;
import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.common._Const;
import com.xfj.mmail.pojo.User;
import com.xfj.mmail.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by asus on 2017/7/2.
 */
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> userLogin(String username, String password, HttpSession session){
        ServerResponse<User> userServerResponse = iUserService.doLogin(username, password);
        if(userServerResponse.isSuccess()){
            session.setAttribute(_Const.CURRENT_LOGIN_USER,userServerResponse.getData());
        }
        return userServerResponse;
    }

    @RequestMapping(value = "/login_out.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> userLoginOut(HttpSession session){
        session.setAttribute(_Const.CURRENT_LOGIN_USER,null);
        return ServerResponse.getServerResponseErrorMessage("login  out success");
    }

    @RequestMapping(value = "/register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> userRegister(User user){
        return iUserService.doRegister(user);
    }

    @RequestMapping(value = "/check_value.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkVaild(@RequestParam("value") String value,@RequestParam("type") String type){
        return iUserService.checkVaild(value,type);
    }

    @RequestMapping(value = "/get_login_user_info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User loginUser = (User)session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == loginUser){
            return ServerResponse.getServerResponseErrorMessage("user is not login");
        }
        return ServerResponse.getServerResponseSuccess(loginUser);
    }

    @RequestMapping(value = "/forget_get_question.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> getForgetUserQuestion(@RequestParam("username") String username){
        return iUserService.getForgetUserQuestion(username);
    }


    @RequestMapping(value = "/check_question_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkForgetQuestionAnswer( @RequestParam("username") String username,
                                                              @RequestParam("question") String question,
                                                              @RequestParam("answer")  String answer){
        return iUserService.checkForgetQuestionAnswer(username,question,answer);

    }



    @RequestMapping(value = "/forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetQuestion(@RequestParam("username") String username,
                                                      @RequestParam("passwordNew") String passwordNew,
                                                      @RequestParam("forgetToken") String forgetToken){
        return iUserService.forgetResetQuestion(username,passwordNew,forgetToken);
    }

    @RequestMapping(value = "/login_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> loginResetPassword(HttpSession session,@RequestParam("passwordOld") String passwordOld,
                                                     @RequestParam("passwordNew") String passwordNew){
        User loginUser = (User)session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == loginUser) {
            return ServerResponse.getServerResponseErrorMessage("please login first");
        }
       return iUserService.updatePasswordWithUserid(loginUser.getId(),passwordOld,passwordNew);
    }

    @RequestMapping(value = "/update_information.do",method = RequestMethod.POST)
    public ServerResponse<User> updateInformation(HttpSession session,User user){
        User loginUser = (User)session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == loginUser) {
            return ServerResponse.getServerResponseErrorMessage("please login first");
        }
        user.setId(loginUser.getId());
        user.setUsername(loginUser.getUsername());
        ServerResponse<User> updateResponse = iUserService.updateUserInformation(user);
        if(updateResponse.isSuccess()){
            session.setAttribute(_Const.CURRENT_LOGIN_USER,updateResponse.getData());
        }
        return updateResponse;
    }


    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInformation(HttpSession session){
        User loginUser = (User)session.getAttribute(_Const.CURRENT_LOGIN_USER);
        if(null == loginUser) {
            return ServerResponse.getServerResponseCodeAndMessage(ResopnseCode.NEED_LOGIN.getCode(),"need login,status code: 10");
        }
        return iUserService.getUserInformationByUserid(loginUser.getId());
    }



}
