package com.dangqun.controller;

import com.dangqun.annotation.CheckIsManager;
import com.dangqun.constant.Constants;
import com.dangqun.entity.AuthEntity;
import com.dangqun.vo.AddAuthMethodBody;
import com.dangqun.vo.DeleteAuthMethodBody;
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
        AuthEntity authEntity = new AuthEntity();
        authEntity.setAuthName(body.getAuthName());
        authEntity.setAuthBranch(body.getAuthBranch());
        authEntity.setAuthBranchPath(body.getAuthBranchPath());
        authEntity.setAuthLevel(Constants.AUTH_LEVEL_SECONDARY);
        authEntity.setAuthDefault(Constants.AUTH_NOT_DEFAULT);
        int insert = authService.insertAuth(authEntity);
        if(insert == 0){
            return Result.failure("新增失败");
        }
        return Result.success();
    }

    @PostMapping("/updateAuth")
    @CheckIsManager
    public Result updateAuth(@RequestBody @Valid UpdateAuthMethodBody body){
        AuthEntity authEntity = authService.selectOneById(body.getAuthId());
        if(authEntity == null || authEntity.getAuthDefault() == Constants.AUTH_DEFAULT){
            return Result.failure("没有该权限或不允许修改");
        }
        authEntity.setAuthName(body.getAuthName());
        authEntity.setAuthBranch(body.getAuthBranch());
        authEntity.setAuthBranchPath(body.getAuthBranchPath());
        int update = authService.updateAuth(authEntity);
        if(update == 0){
            return Result.failure("更新失败");
        }
        return Result.success();
    }

    @PostMapping("/deleteAuth")
    @CheckIsManager
    public Result deleteAuth(@RequestBody @Valid DeleteAuthMethodBody body){
        AuthEntity authEntity = authService.selectOneById(body.getAuthId());
        if(authEntity == null || authEntity.getAuthDefault() == Constants.AUTH_DEFAULT){
            return Result.failure("没有该权限或不允许删除");
        }
        int delete = authService.deleteAuthById(authEntity.getAuthId());
        if(delete == 0){
            return Result.failure("删除失败");
        }
        return Result.success();
    }

    @PostMapping("/getAuth")
    @CheckIsManager
    public Result getAuth(){
        List<AuthEntity> authEntityList = authService.selectAllAuth();
        return Result.success(authEntityList);
    }
}
