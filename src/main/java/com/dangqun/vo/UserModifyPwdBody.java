package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wcy
 */
@Data
public class UserModifyPwdBody {
    @NotBlank(message = "新密码为空")
    private String userNewPwd;
    @NotBlank(message = "旧密码为空")
    private String userOldPwd;
}
