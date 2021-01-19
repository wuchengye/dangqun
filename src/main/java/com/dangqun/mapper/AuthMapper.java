package com.dangqun.mapper;

import com.dangqun.entity.AuthEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wcy
 */
@Mapper
public interface AuthMapper {

    AuthEntity selectOneById(int userAuth);

    int insertAuth(AuthEntity authEntity);

    int updateAuth(AuthEntity authEntity);

    int deleteAuthById(int authId);

    List<AuthEntity> selectAllAuth();

    AuthEntity selectOneByName(String authName);
}
