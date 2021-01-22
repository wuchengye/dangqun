package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class BranchEntity {
    private Integer branchId;
    private String branchName;
    private String branchRootPath;
    private Integer branchCreator;
}
