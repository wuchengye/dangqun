package com.dangqun.controller;

import com.dangqun.annotation.CheckIsManager;
import com.dangqun.constant.Constants;
import com.dangqun.entity.BranchEntity;
import com.dangqun.entity.UserEntity;
import com.dangqun.service.BranchService;
import com.dangqun.service.UserService;
import com.dangqun.service.common.RedisService;
import com.dangqun.utils.FileUtils;
import com.dangqun.vo.IdBody;
import com.dangqun.vo.NameBody;
import com.dangqun.vo.restful.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.io.File;
import java.util.List;

/**
 * @author wcy
 */
@RestController
@RequestMapping("/branch")
public class BranchController {
    @Autowired
    private BranchService branchService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;

    @PostMapping("/addBranch")
    @CheckIsManager
    public Result addBranch(@RequestBody @Valid NameBody nameBody){
        BranchEntity branchEntity = branchService.selectOneByName(nameBody.getName());
        if(branchEntity != null){
            return Result.failure("支部名重复");
        }
        UserEntity creator = redisService.getUserData();
        branchEntity = new BranchEntity();
        branchEntity.setBranchName(nameBody.getName());
        if(creator != null){
            branchEntity.setBranchCreator(creator.getUserId());
        }
        //插入并返回id
        branchService.insertBranchReturnId(branchEntity);
        branchEntity.setBranchRootPath(Constants.BRANCH_AND_FILE_PARENT_PATH
                + Constants.BRANCH_AND_FILE_PATH_SPLIT
                + branchEntity.getBranchId()
        );
        int update = branchService.updateBranch(branchEntity);
        if(update == 0){
            return Result.failure("新增失败");
        }
        File branchFolder = new File(branchEntity.getBranchRootPath());
        branchFolder.mkdirs();
        return Result.success(branchEntity);
    }

    @PostMapping("/deleteBranch")
    @CheckIsManager
    public Result deleteBranch(@RequestBody @Valid IdBody body){
        BranchEntity deleteBranch = branchService.selectOneById(body.getId());
        if(deleteBranch == null){
            return Result.failure("删除对象不存在");
        }
        List<UserEntity> userList = userService.selectAllByBranch(deleteBranch.getBranchId());
        if(userList.size() != 0){
            return Result.failure("还有用户绑定该支部");
        }
        int delete = branchService.deleteBranch(body.getId());
        if(delete == 0){
            return Result.failure("删除失败");
        }
        //删除路径表中对应路径

        //删除文件表对应支部id数据

        //递归删除文件夹
        FileUtils.delDir(deleteBranch.getBranchRootPath());
        return Result.success();
    }
}
