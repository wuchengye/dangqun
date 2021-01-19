package com.dangqun.service.common;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.dangqun.entity.UserEntity;
import com.dangqun.service.UserService;
import com.dangqun.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wcy
 */
@Service
public class RedisService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;

    private static final String USER_DATA_PREFIX = "User_Data_";

    public void setLoginValid(String key,String value){
        redisUtil.set(key,value,1800L);
    }

    public String getLoginValid(String key){
        return redisUtil.get(key);
    }

    public void expireLoginValid(String key){
        redisUtil.expire(key,1800L);
    }

    /**
     * @date 2021-01-19 16:01
     * 缓存用户在用户表中数据
     */
    public void setUserData(UserEntity userEntity){
        String key = USER_DATA_PREFIX + userEntity.getUserName() + userEntity.getUserName().hashCode();
        String value = JSONObject.toJSONString(userEntity);
        redisUtil.set(key,value,1800L);
    }

    public UserEntity getUserData(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        String userName = JWT.decode(token).getAudience().get(0);
        UserEntity userEntity;
        String key = USER_DATA_PREFIX + userName + userName.hashCode();
        String value = redisUtil.get(key);
        if(value == null){
            userEntity = userService.selectOneByName(userName);
            if(userEntity == null){
                return null;
            }else {
                value = JSONObject.toJSONString(userEntity);
                redisUtil.set(key,value,1800L);
                return userEntity;
            }
        }else {
            userEntity = JSONObject.parseObject(value,UserEntity.class);
            redisUtil.expire(key,1800L);
            return userEntity;
        }
    }
}
