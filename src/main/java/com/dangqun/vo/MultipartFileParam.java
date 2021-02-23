package com.dangqun.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class MultipartFileParam {
    @NotNull
    private Integer trackId;
    @NotNull
    private Integer chunks;
    @NotNull
    private Integer chunk;
    private long size = 0L;
    @NotBlank
    private String name;
}
