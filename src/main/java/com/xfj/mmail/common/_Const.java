package com.xfj.mmail.common;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by asus on 2017/7/3.
 */
public class _Const {
    public static final String CURRENT_LOGIN_USER = "CURRENT_LOGIN_USER";

    public static final String EMAIL_TYPE = "email";

    public static final String USERNAME_TYPE = "username";

    public static final int PRODUCT_STATUS_ON = 1;
    public interface Role{
        int ADMIN = 1;
        int USER = -1;
    }

    public interface ORDERBY{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");

    }




}
