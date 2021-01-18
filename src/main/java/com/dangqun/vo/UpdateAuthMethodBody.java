package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class UpdateAuthMethodBody {
    @NotNull(message = "没有指定权限")
    private Integer authId;
    @NotBlank(message = "权限名为空")
    private String authName;
    @NotBlank(message = "支部为空")
    private String authBranch;
    @NotBlank(message = "内部路径为空")
    private String authBranchPath;
}
