package com.dangqun.controller;

import com.auth0.jwt.JWT;
import com.dangqun.annotation.CheckIsManager;
import com.dangqun.config.impl.ValidList;
import com.dangqun.constant.Constants;
import com.dangqun.entity.AuthEntity;
import com.dangqun.entity.UserEntity;
import com.dangqun.vo.AddUserMethodBody;
import com.dangqun.vo.LoginMethodBody;
import com.dangqun.vo.UpdateUserMethodBody;
import com.dangqun.vo.UserModifyPwdBody;
import com.dangqun.vo.restful.Result;
import com.dangqun.service.AuthService;
import com.dangqun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wcy
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result login(@RequestBody @Valid LoginMethodBody body){
        UserEntity userEntity = userService.selectOneByName(body.getUserName());
        if(userEntity == null){
            return Result.failure("用户不存在");
        }
        if(!userEntity.getUserPwd().equals(body.getUserPwd())){
            return Result.failure("密码错误");
        }
        AuthEntity authEntity = authService.selectOneById(userEntity.getUserAuth());
        if(authEntity == null){
            return Result.failure("用户无权限");
        }
        Map<String,Object> map = new HashMap(3);
        map.put("userName",userEntity.getUserName());
        map.put("userAuthLevel",authEntity.getAuthLevel());
        map.put("userPwd",userEntity.getUserPwd());
        return Result.success(map);
    }

    @PostMapping("/userModifyPwd")
    public Result userModifyPwd(@RequestBody @Valid UserModifyPwdBody body){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        String userName = JWT.decode(token).getAudience().get(0);
        UserEntity userEntity = userService.selectOneByName(userName);
        if(userEntity == null || userEntity.getUserPwd().equals(body.getUserOldPwd())){
            return Result.failure("密码错误");
        }
        int update = userService.modifyPwd(userEntity.getUserId(),body.getUserNewPwd());
        return update == 0 ? Result.failure("修改失败") : Result.success();
    }

    @PostMapping("/addUser")
    @CheckIsManager
    public Result addUser(@RequestBody @Valid AddUserMethodBody body){
        UserEntity userEntity = userService.selectOneByName(body.getUserName());
        if(userEntity != null){
            return Result.failure("用户名已存在");
        }
        userEntity = new UserEntity();
        userEntity.setUserName(body.getUserName());
        userEntity.setUserAuth(body.getUserAuth());
        userEntity.setUserBranch(body.getUserBranch());
        userEntity.setUserPwd(Constants.USER_DEFAULT_PASSWORD);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        UserEntity creator = userService.selectOneByName(JWT.decode(token).getAudience().get(0));
        if(creator != null){
            userEntity.setUserCreator(creator.getUserId());
        }
        int update = userService.insertUser(userEntity);
        if(update == 0){
            return Result.failure("新增用户失败");
        }
        return Result.success();
    }

    @PostMapping("/updateUser")
    @CheckIsManager
    public Result updateUser(@RequestBody @Valid ValidList<UpdateUserMethodBody> bodyList){
        int successTotal = 0;
        for (UpdateUserMethodBody body : bodyList){
            successTotal = successTotal + userService.updateUser(body);
        }
        return successTotal == 0 ? Result.failure() : Result.success("成功:" + successTotal);
    }

    @PostMapping("/resetPwd")
    @CheckIsManager
    public Result resetPwd(@RequestBody @Valid ValidList<Integer> userIdList) {
        int successTotal = 0;
        for (int id : userIdList) {
            successTotal = successTotal + userService.modifyPwd(id, Constants.USER_DEFAULT_PASSWORD);
        }
        return successTotal == 0 ? Result.failure() : Result.success("成功:" + successTotal);
    }

    @PostMapping("/deleteUser")
    @CheckIsManager
    public Result deleteUser(@RequestBody @Valid ValidList<Integer> userIdList) {
        int successTotal = 0;
        for (int id : userIdList){
            successTotal = successTotal + userService.deleteUser(id);
        }
        return successTotal == 0 ? Result.failure() : Result.success("成功:" + successTotal);
    }
}
