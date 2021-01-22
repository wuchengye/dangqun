package com.dangqun.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wcy
 */
@Data
public class DeleteTrackMethodBody {
    @NotNull
    private Integer trackId;
    @NotNull
    private Integer trackBranch;
}
