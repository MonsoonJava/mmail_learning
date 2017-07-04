package com.xfj.mmail.common;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by asus on 2017/7/3.
 */
public class TokenCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);

    public static final String USER_TOKEN_PRIFEX = "USER_TOKEN_";

    public static final LoadingCache<String,String> instance = CacheBuilder.newBuilder()
            .maximumSize(10000).initialCapacity(1000).expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void set(String key,String value){
        instance.put(key,value);
    }

    public static Object get(String key){
        try{
            String val = instance.get(key);
            if(val.equals("null"))
                return null;
            return val;
        }catch (Exception e){
            LOGGER.error("get" + key + "error",e);
            return null;
        }
    }
}
