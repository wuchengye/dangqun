package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class UpdateUserMethodBody {
    @NotNull(message = "未指定用户ID")
    private Integer userId;
    @NotNull(message = "未指定权限")
    private Integer userAuth;
    @NotNull(message = "未指定支部")
    private Integer userBranch;
}
