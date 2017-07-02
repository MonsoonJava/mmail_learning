package com.xfj.mmail.dao;

import com.xfj.mmail.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(@Param("username")String username);

    User doLoginWithUsernameAndPassword(@Param("username")String username,@Param("password")String password);
}