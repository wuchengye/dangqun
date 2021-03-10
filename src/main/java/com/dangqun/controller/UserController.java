package com.dangqun.controller;

import com.dangqun.annotation.CheckIsManager;
import com.dangqun.annotation.PassToken;
import com.dangqun.config.impl.ValidList;
import com.dangqun.constant.Constants;
import com.dangqun.entity.AuthEntity;
import com.dangqun.entity.MessageEntity;
import com.dangqun.entity.UserEntity;
import com.dangqun.service.MessageService;
import com.dangqun.service.common.RedisService;
import com.dangqun.utils.DateTimeUtils;
import com.dangqun.vo.*;
import com.dangqun.vo.restful.Result;
import com.dangqun.service.AuthService;
import com.dangqun.service.UserService;
import com.dangqun.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private RedisService redisService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WebSocketServer webSocketServer;

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
        redisService.setUserData(userEntity);
        Map<String,Object> map = new HashMap(3);
        map.put("userName",userEntity.getUserName());
        map.put("userAuthLevel",authEntity.getAuthLevel());
        map.put("userPwd",userEntity.getUserPwd());
        return Result.success(map);
    }

    @PostMapping("/logout")
    public Result logout(){
        UserEntity userEntity = redisService.getUserData();
        redisService.delToken(userEntity.getUserName());
        return Result.success();
    }

    @PostMapping("/userModifyPwd")
    public Result userModifyPwd(@RequestBody @Valid UserModifyPwdBody body){
        UserEntity userEntity = redisService.getUserData();
        if(userEntity == null || !userEntity.getUserPwd().equals(body.getUserOldPwd())){
            return Result.failure("密码错误");
        }
        int update = userService.modifyPwd(userEntity.getUserId(),body.getUserNewPwd());
        return update == 0 ? Result.failure("修改失败") : Result.success();
    }

    @PassToken
    @PostMapping("/forgetPwd")
    public Result forgetPwd(@RequestBody @Valid NameBody body){
        UserEntity userEntity = userService.selectOneByName(body.getName());
        if(userEntity != null){
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setMesName("忘记密码");
            messageEntity.setMesSendUser(userEntity.getUserName());
            messageEntity.setMesBeginTime(DateTimeUtils.getTime());
            messageEntity.setMesStatus(Constants.MES_NOT_CONFIRM_STATUS);
            messageEntity.setMesType(Constants.MES_TYPE_FORGET_PWD);
            messageService.insertOne(messageEntity);
            List<UserEntity> list = userService.findUserByAuthLevel(Constants.AUTH_LEVEL_ADMIN);
            for (UserEntity u : list){
                webSocketServer.sendMessage(messageEntity.toString(),u.getUserName());
            }
            return Result.success();
        }else {
            return Result.failure("用户不存在");
        }
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
        UserEntity creator = redisService.getUserData();
        if(creator != null){
            userEntity.setUserCreator(creator.getUserId());
        }
        int update = userService.insertUser(userEntity);
        return update == 0 ?  Result.failure("新增用户失败") : Result.success();
    }

    @PostMapping("/updateUser")
    @CheckIsManager
    public Result updateUser(@RequestBody @Valid ValidList<UpdateUserMethodBody> bodyList){
        UserEntity u = redisService.getUserData();
        int successTotal = 0;
        for (UpdateUserMethodBody body : bodyList){
            if (body.getUserId().intValue() == u.getUserId()){
                continue;
            }
            successTotal = successTotal + userService.updateUser(body);
        }
        return successTotal == 0 ? Result.failure() : Result.success("成功:" + successTotal);
    }

    @PostMapping("/resetPwd")
    @CheckIsManager
    public Result resetPwd(@RequestBody @Valid ValidList<IdBody> userIdList) {
        int successTotal = 0;
        for (IdBody body : userIdList) {
            successTotal = successTotal + userService.modifyPwd(body.getId(), Constants.USER_DEFAULT_PASSWORD);
        }
        return successTotal == 0 ? Result.failure() : Result.success("成功:" + successTotal);
    }

    @PostMapping("/deleteUser")
    @CheckIsManager
    public Result deleteUser(@RequestBody @Valid ValidList<IdBody> userIdList) {
        int successTotal = 0;
        for (IdBody body : userIdList){
            int delete = userService.deleteUser(body.getId());
            successTotal = successTotal + delete;
        }
        return successTotal == 0 ? Result.failure() : Result.success("成功:" + successTotal);
    }

    @PostMapping("/getUsers")
    @CheckIsManager
    public Result getUsers(@RequestBody @Valid GetUsersMethodBody body){
        return Result.success(userService.getUserAndAuthAndBranch(body));
    }
}
