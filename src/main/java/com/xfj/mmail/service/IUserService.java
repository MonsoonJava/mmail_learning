package com.xfj.mmail.service;

import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.pojo.User;

/**
 * Created by asus on 2017/7/2.
 */
public interface IUserService {
    public ServerResponse<User> doLogin(String username, String password);

    public ServerResponse<String> doRegister(User user);

    public ServerResponse<String> checkVaild(String value,String type);

    public ServerResponse<String> getForgetUserQuestion(String username);

    public ServerResponse<String> checkForgetQuestionAnswer(String username,String question,String answer);

    public ServerResponse<String> forgetResetQuestion(String username,String passwordNew,String forgetToken);

    public ServerResponse<String> updatePasswordWithUserid(int userid,String passwordOld,String passwordNew);

    public ServerResponse<User>  updateUserInformation(User user);

    public ServerResponse<User> getUserInformationByUserid(Integer id);

    public ServerResponse<String> checkLoginUserRole(User user,int Role);
}
