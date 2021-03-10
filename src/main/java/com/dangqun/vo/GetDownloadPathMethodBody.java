package com.dangqun.vo;

import com.dangqun.config.impl.ValidList;
import lombok.Data;
import javax.validation.constraints.NotNull;


/**
 * @author wcy
 */
@Data
public class GetDownloadPathMethodBody {
    @NotNull(message = "未指定下载方式")
    private Integer isDownloadAll;
    @NotNull(message = "未指定路径id")
    private Integer trackId;
    private ValidList<Integer> fileIds;
}
