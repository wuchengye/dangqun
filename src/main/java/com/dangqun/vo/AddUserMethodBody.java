package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class AddUserMethodBody {
    @NotBlank(message = "用户名为空")
    private String userName;
    @NotNull
    private Integer userAuth;
    @NotNull
    private Integer userBranch;
}
