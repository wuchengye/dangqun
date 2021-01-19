package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class IdBody {
    @NotNull
    private Integer id;
}
