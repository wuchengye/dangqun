package com.dangqun.vo;

import com.dangqun.config.impl.ValidList;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class DeleteFilesMethodBody {
    @NotNull(message = "未指定路径id")
    private Integer trackId;
    private ValidList<Integer> fileIds;
}
