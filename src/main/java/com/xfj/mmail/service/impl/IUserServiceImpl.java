package com.xfj.mmail.service.impl;

import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.dao.UserMapper;
import com.xfj.mmail.pojo.User;
import com.xfj.mmail.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by asus on 2017/7/2.
 */
@Service(value = "iUserService")
public class IUserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> doLogin(String username, String password) {
        int count = userMapper.checkUsername(username);
        if(0 == count) return ServerResponse.getServerResponseErrorMessage("username is not exist");
        User user = userMapper.doLoginWithUsernameAndPassword(username, password);
        if(null == user) return ServerResponse.getServerResponseErrorMessage("password is not correct");
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.getServerResponseSuccess("login success",user);
    }
}
