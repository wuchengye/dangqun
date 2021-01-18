package com.dangqun.service;

import com.dangqun.entity.UserEntity;
import com.dangqun.mapper.UserMapper;
import com.dangqun.vo.UpdateUserMethodBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wcy
 */

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserEntity selectOneByName(String userName) {
        return userMapper.selectOneByName(userName);
    }

    public int insertUser(UserEntity userEntity) {
        return userMapper.insertUser(userEntity);
    }

    public int updateUser(UpdateUserMethodBody body) {
        return userMapper.updateUser(body);
    }

    public int modifyPwd(int userId,String userPwd) {
        return userMapper.modifyPwd(userId,userPwd);
    }

    public int deleteUser(int userId) {
        return userMapper.deleteUser(userId);
    }
}
