package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class FileEntity {
    private Integer fileId;
    private String fileName;
    private String filePath;
    private Integer fileTrack;
    private Integer fileBranch;
}
