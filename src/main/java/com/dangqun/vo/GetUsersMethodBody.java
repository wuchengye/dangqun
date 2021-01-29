package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class GetUsersMethodBody {
    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
    private String userName;
}
