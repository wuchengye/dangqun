package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class DeleteAuthMethodBody {
    @NotNull(message = "没有参数")
    private Integer authId;
}
