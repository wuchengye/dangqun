package com.dangqun.service;

import com.dangqun.entity.UserEntity;
import com.dangqun.mapper.UserMapper;
import com.dangqun.vo.GetUsersMethodBody;
import com.dangqun.vo.UpdateUserMethodBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    public List<UserEntity> selectAllByAuth(int userAuth) {
        return userMapper.selectAllByAuth(userAuth);
    }

    public List<UserEntity> selectAllByBranch(Integer userBranch) {
        return userMapper.selectAllByBranch(userBranch);
    }

    public PageInfo getUserAndAuthAndBranch(GetUsersMethodBody body) {
        PageHelper.startPage(body.getPageNum(),body.getPageSize());
        List<Map> mapList =  userMapper.getUserAndAuthAndBranch(body.getUserName());
        return new PageInfo(mapList);
    }
}
