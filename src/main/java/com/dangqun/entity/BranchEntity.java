package com.dangqun.entity;

import lombok.Data;

/**
 * @author wcy
 */
@Data
public class BranchEntity {
    private int branchId;
    private String branchName;
    private String branchRootPath;
    private int branchCreator;
}
