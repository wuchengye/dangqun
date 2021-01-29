package com.dangqun.mapper;

import com.dangqun.entity.BranchEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wcy
 */
@Mapper
public interface BranchMapper {

    BranchEntity selectOneByName(String branchName);

    int insertBranchReturnId(BranchEntity branchEntity);

    int updateBranch(BranchEntity branchEntity);

    int deleteBranch(Integer branchId);

    BranchEntity selectOneById(Integer branchId);

    List<BranchEntity> selectAll();
}
