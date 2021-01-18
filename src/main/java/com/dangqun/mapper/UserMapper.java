package com.dangqun.mapper;

import com.dangqun.entity.UserEntity;
import com.dangqun.vo.UpdateUserMethodBody;
import org.apache.ibatis.annotations.Mapper;

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
}
