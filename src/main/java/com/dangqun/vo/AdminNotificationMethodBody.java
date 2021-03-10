package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wcy
 */
@Data
public class AdminNotificationMethodBody {
    @NotBlank
    private String mesName;
    @NotBlank
    private String mesContent;
}
