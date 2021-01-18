package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class AuthEntity {
    private int authId;
    private String authName;
    private int authLevel;
    private String authBranch;
    private String authBranchPath;
    private int authDefault;
}
