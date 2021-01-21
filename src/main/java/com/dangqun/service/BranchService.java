package com.dangqun.service;

import com.dangqun.entity.BranchEntity;
import com.dangqun.mapper.BranchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wcy
 */
@Service
public class BranchService {
    @Autowired
    private BranchMapper branchMapper;

    public BranchEntity selectOneByName(String branchName) {
        return branchMapper.selectOneByName(branchName);
    }

    public int insertBranchReturnId(BranchEntity branchEntity) {
        return branchMapper.insertBranchReturnId(branchEntity);
    }

    public int updateBranch(BranchEntity branchEntity) {
        return branchMapper.updateBranch(branchEntity);
    }

    public int deleteBranch(Integer branchId) {
        return branchMapper.deleteBranch(branchId);
    }

    public BranchEntity selectOneById(Integer branchId) {
        return branchMapper.selectOneById(branchId);
    }
}
