package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wcy
 */
@Data
public class LoginMethodBody {
    @NotBlank(message = "用户名为空")
    private String userName;
    @NotBlank(message = "密码为空")
    private String userPwd;
}
