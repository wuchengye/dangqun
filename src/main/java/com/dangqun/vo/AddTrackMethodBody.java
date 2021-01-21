package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class AddTrackMethodBody {
    @NotNull(message = "未指定支部")
    private Integer branchId;
    @NotBlank(message = "文件夹名为空")
    private String trackName;
    private Integer trackParentId;
}
