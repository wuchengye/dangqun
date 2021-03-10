package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class SelectMesMethodBody {
    private String mesName;
    private String mesSendUser;
    private String mesRecUser;
    private String beforeBeginTime;
    private String afterBeginTime;
    private Integer isConfirm;

    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
}
