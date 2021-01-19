package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author wcy
 */
@Data
public class NameBody {
    @NotBlank(message = "name为空")
    private String name;
}
