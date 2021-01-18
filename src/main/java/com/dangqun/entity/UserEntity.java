package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class UserEntity {
    private int userId;
    private String userName;
    private String userPwd;
    private int userAuth;
    private int userBranch;
    private int userCreator;
}
