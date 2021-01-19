package com.dangqun.service;

import com.dangqun.entity.AuthEntity;
import com.dangqun.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wcy
 */
@Service
public class AuthService {
    @Autowired
    private AuthMapper authMapper;

    public AuthEntity selectOneById(int userAuth) {
        return authMapper.selectOneById(userAuth);
    }

    public AuthEntity selectOneByName(String authName){
        return authMapper.selectOneByName(authName);
    }

    public int insertAuth(AuthEntity authEntity) {
        return authMapper.insertAuth(authEntity);
    }

    public int updateAuth(AuthEntity authEntity) {
        return authMapper.updateAuth(authEntity);
    }

    public int deleteAuthById(int authId) {
        return authMapper.deleteAuthById(authId);
    }

    public List<AuthEntity> selectAllAuth() {
        return authMapper.selectAllAuth();
    }
}
