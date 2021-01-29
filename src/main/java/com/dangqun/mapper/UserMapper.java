package com.dangqun.mapper;

import com.dangqun.entity.UserEntity;
import com.dangqun.vo.UpdateUserMethodBody;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author wcy
 */
@Mapper
public interface UserMapper {

    UserEntity selectOneByName(String userName);

    int insertUser(UserEntity userEntity);

    int updateUser(UpdateUserMethodBody body);

    int modifyPwd(int userId, String userPwd);

    int deleteUser(int userId);

    List<UserEntity> selectAllByAuth(int userAuth);

    List<UserEntity> selectAllByBranch(int userBranch);

    List<Map> getUserAndAuthAndBranch(String userName);
}
