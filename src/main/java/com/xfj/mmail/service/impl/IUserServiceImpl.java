package com.xfj.mmail.service.impl;

import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.common.TokenCache;
import com.xfj.mmail.common._Const;
import com.xfj.mmail.dao.UserMapper;
import com.xfj.mmail.pojo.User;
import com.xfj.mmail.service.IUserService;
import com.xfj.mmail.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by asus on 2017/7/2.
 */
@Service(value = "iUserService")
public class IUserServiceImpl implements IUserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(IUserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> doLogin(String username, String password) {
        if(StringUtils.isBlank(username)) return ServerResponse.getServerResponseErrorMessage("username must not blank");
        if(StringUtils.isBlank(password)) return ServerResponse.getServerResponseErrorMessage("password must not blank");
        int count = userMapper.checkUsername(username);
        if(0 == count) return ServerResponse.getServerResponseErrorMessage("username is not exist");
        String MD5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.doLoginWithUsernameAndPassword(username, MD5Password);
        if(null == user) return ServerResponse.getServerResponseErrorMessage("password is not correct");
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.getServerResponseSuccess("login success",user);
    }

    @Override
    public ServerResponse<String> doRegister(User user) {
        ServerResponse<String> checkUsernameResponse = this.checkVaild(user.getUsername(), _Const.USERNAME_TYPE);
        if(!checkUsernameResponse.isSuccess())
            return checkUsernameResponse;
        ServerResponse<String> checkEmailResponse = this.checkVaild(user.getEmail(), _Const.EMAIL_TYPE);
        if(!checkEmailResponse.isSuccess())
            return checkEmailResponse;
        //对user进行MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setRole(_Const.Role.USER);
        int insertNumber = userMapper.insert(user);
        if(insertNumber == 0){
            return ServerResponse.getServerResponseErrorMessage("do register failed");
        }
        return ServerResponse.getServerResponseSuccessMessage("do register success");
    }

    @Override
    public ServerResponse<String> checkVaild(String value, String type) {
        if(StringUtils.isNotBlank(type) && _Const.USERNAME_TYPE.equals(type)){
            if(StringUtils.isBlank(value))
                return ServerResponse.getServerResponseErrorMessage("username must not be blank");
            int count = userMapper.checkUsername(value);
            if(count > 0)
                return ServerResponse.getServerResponseErrorMessage("username has exist");
            return ServerResponse.getServerResponseSuccess();
        }
        if(StringUtils.isNotBlank(type) && _Const.EMAIL_TYPE.equals(type)){
            if(StringUtils.isBlank(value))
                return ServerResponse.getServerResponseErrorMessage("email must not be blank");
            int count = userMapper.checkUsername(value);
            if(count > 0)
                return ServerResponse.getServerResponseErrorMessage("email has exist");
            return ServerResponse.getServerResponseSuccess();
        }
        return ServerResponse.getServerResponseErrorMessage("with arguments wrong");
    }

    @Override
    public ServerResponse<String> getForgetUserQuestion(String username) {
        if(StringUtils.isBlank(username))
            return ServerResponse.getServerResponseErrorMessage("username must not be blank");
        String question = userMapper.getUserForgetQuestion(username);
        if(StringUtils.isNotBlank(question))
            return ServerResponse.getServerResponseSuccess(question);
        return ServerResponse.getServerResponseErrorMessage("the user do not set any question");
    }

    @Override
    public ServerResponse<String> checkForgetQuestionAnswer(String username, String question, String answer) {
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(question) && StringUtils.isNotBlank(answer)){
            int count = userMapper.checkForgetQuestionAnswer(username, question, answer);
            if(count > 0 ){
                // answer is correct
                String questionToken = UUID.randomUUID().toString();
                //set questionToken in local cache
                TokenCache.instance.put(TokenCache.USER_TOKEN_PRIFEX + username,questionToken);
                return ServerResponse.getServerResponseSuccess(questionToken);
            }
            return ServerResponse.getServerResponseErrorMessage("the answer is wrong");
        }
        return ServerResponse.getServerResponseErrorMessage("username or question or answer is blank");
    }

    @Override
    public ServerResponse<String> forgetResetQuestion(String username, String passwordNew, String forgetToken) {
        //检查用户名
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(passwordNew) && StringUtils.isNotBlank(forgetToken)){
            //获取缓存中的TOKEN
            try {
                String cacheToken = TokenCache.instance.get(TokenCache.USER_TOKEN_PRIFEX + username);
                if(StringUtils.isNotBlank(cacheToken) && StringUtils.equals(cacheToken,forgetToken)){
                    String MD5PasswordNew = MD5Util.MD5EncodeUtf8(passwordNew);
                    int rowCount = userMapper.resetPasswordWithUsername(username, MD5PasswordNew);
                    if(rowCount > 0){
                        return ServerResponse.getServerResponseSuccessMessage("reset password success");
                    }
                }else{
                    return ServerResponse.getServerResponseErrorMessage("token is blank or has timeout");
                }
            } catch (ExecutionException e) {
                LOGGER.error("reset password with new is failed",e);
                return ServerResponse.getServerResponseErrorMessage("reset password with new is failed");
            }
        }
        return ServerResponse.getServerResponseErrorMessage("reset password failed");
    }


    @Override
    public ServerResponse<String> updatePasswordWithUserid(int userid, String passwordOld, String passwordNew) {
        if(StringUtils.isBlank(passwordNew)){
            return ServerResponse.getServerResponseErrorMessage("new password must not be blank");
        }
        int checkCount  = userMapper.checkPassword(userid, passwordOld);
        if(checkCount == 0)
            return ServerResponse.getServerResponseErrorMessage("old password is wrong");
        int rowCount = userMapper.updatePasswordWithUserid(userid, MD5Util.MD5EncodeUtf8(passwordOld), MD5Util.MD5EncodeUtf8(passwordNew));
        if(rowCount > 0){
            return ServerResponse.getServerResponseSuccessMessage("reset password success");
        }
        return ServerResponse.getServerResponseErrorMessage("reset password failed");
    }

    @Override
    public ServerResponse<User> updateUserInformation(User user) {
        int rowCount = userMapper.checkEmailWithUserid(user.getId(), user.getEmail());
        if(rowCount > 0)
            return ServerResponse.getServerResponseErrorMessage("the email has been used,please change another");
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setUsername(user.getUsername());
        int updateRow = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateRow > 1){
            return ServerResponse.getServerResponseSuccess("update information success",updateUser);
        }
        return ServerResponse.getServerResponseErrorMessage("update information failed");
    }

    @Override
    public ServerResponse<User> getUserInformationByUserid(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(null == user) {
            return ServerResponse.getServerResponseErrorMessage("can get user information");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.getServerResponseSuccess(user);
    }


    @Override
    public ServerResponse<String> checkLoginUserRole(User user,int Role){
        if(user.getRole() == Role){
            return ServerResponse.getServerResponseSuccess();
        }
        return ServerResponse.getServerResponseError();
    }

}
