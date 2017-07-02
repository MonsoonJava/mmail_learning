package com.xfj.mmail.service;

import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.pojo.User;

/**
 * Created by asus on 2017/7/2.
 */
public interface IUserService {
    public ServerResponse<User> doLogin(String username, String password);
}
