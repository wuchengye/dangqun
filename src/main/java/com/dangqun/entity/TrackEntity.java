package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class TrackEntity {
    private int trackId;
    private String trackName;
    private int trackBranch;
    private String trackInnerPath;
    private String trackFullPath;
    private int trackLeftValue;
    private int trackRightValue;
    private int trackStatus;
}
