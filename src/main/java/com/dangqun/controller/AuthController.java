package com.dangqun.controller;

import com.dangqun.annotation.CheckIsManager;
import com.dangqun.constant.Constants;
import com.dangqun.entity.AuthEntity;
import com.dangqun.vo.AddAuthMethodBody;
import com.dangqun.vo.IdBody;
import com.dangqun.vo.UpdateAuthMethodBody;
import com.dangqun.vo.restful.Result;
import com.dangqun.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

/**
 * @author wcy
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/addAuth")
    @CheckIsManager
    public Result addAuth(@RequestBody @Valid AddAuthMethodBody body){
        AuthEntity authEntity = authService.selectOneByName(body.getAuthName());
        if (authEntity != null){
            return Result.failure("权限名重复");
        }
        authEntity = new AuthEntity();
        authEntity.setAuthName(body.getAuthName());
        authEntity.setAuthBranch(body.getAuthBranch());
        authEntity.setAuthBranchPath(body.getAuthBranchPath());
        authEntity.setAuthLevel(Constants.AUTH_LEVEL_SECONDARY);
        authEntity.setAuthDefault(Constants.AUTH_NOT_DEFAULT);
        int insert = authService.insertAuth(authEntity);
        return insert == 0 ? Result.failure("新增失败") : Result.success();
    }

    @PostMapping("/updateAuth")
    @CheckIsManager
    public Result updateAuth(@RequestBody @Valid UpdateAuthMethodBody body){
        AuthEntity authEntity = authService.selectOneById(body.getAuthId());
        if(authEntity == null || authEntity.getAuthDefault() == Constants.AUTH_DEFAULT){
            return Result.failure("没有该权限或不允许修改");
        }
        if(!authEntity.getAuthName().equals(body.getAuthName())){
            AuthEntity selectByName = authService.selectOneByName(body.getAuthName());
            if(selectByName != null){
                return Result.failure("权限名重复");
            }
        }
        authEntity.setAuthName(body.getAuthName());
        authEntity.setAuthBranch(body.getAuthBranch());
        authEntity.setAuthBranchPath(body.getAuthBranchPath());
        int update = authService.updateAuth(authEntity);
        return update == 0 ? Result.failure("更新失败") : Result.success();
    }

    @PostMapping("/deleteAuth")
    @CheckIsManager
    public Result deleteAuth(@RequestBody @Valid IdBody body){
        AuthEntity authEntity = authService.selectOneById(body.getId());
        if(authEntity == null || authEntity.getAuthDefault() == Constants.AUTH_DEFAULT){
            return Result.failure("没有该权限或不允许删除");
        }
        int delete = authService.deleteAuthById(authEntity.getAuthId());
        return delete == 0 ? Result.failure("删除失败") : Result.success();
    }

    @PostMapping("/getAuth")
    @CheckIsManager
    public Result getAuth(){
        List<AuthEntity> authEntityList = authService.selectAllAuth();
        return Result.success(authEntityList);
    }
}
