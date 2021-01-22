package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class AuthEntity {
    private Integer authId;
    private String authName;
    private Integer authLevel;
    private String authBranch;
    private String authBranchPath;
    private Integer authDefault;
}
