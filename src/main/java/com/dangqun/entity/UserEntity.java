package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class UserEntity {
    private Integer userId;
    private String userName;
    private String userPwd;
    private Integer userAuth;
    private Integer userBranch;
    private Integer userCreator;
}
