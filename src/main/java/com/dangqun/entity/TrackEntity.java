package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class TrackEntity {
    private Integer trackId;
    private String trackName;
    private Integer trackBranch;
    private String trackInnerPath;
    private String trackFullPath;
    private Integer trackLeftValue;
    private Integer trackRightValue;
    private Integer trackParentId;
    private Integer trackStatus;
}
