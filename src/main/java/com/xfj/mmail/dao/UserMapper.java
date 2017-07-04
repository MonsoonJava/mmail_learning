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

    int checkEmail(@Param("email")String email);

    User doLoginWithUsernameAndPassword(@Param("username")String username,@Param("password")String password);

    String getUserForgetQuestion(@Param("username")String username);

    int checkForgetQuestionAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);

    int resetPasswordWithUsername(@Param("username") String username,@Param("passwordNew")String passwordNew);

    int checkPassword(@Param("userid")int userid,@Param("password")String password);

    int updatePasswordWithUserid(@Param("userid")int userid,@Param("passwordOld") String passwordOld,@Param("passwordNew")String passwordNew);

    int checkEmailWithUserid(@Param("userid") int userid,@Param("email")String email);

}