package com.dangqun.controller;

import com.dangqun.annotation.CheckIsManager;
import com.dangqun.constant.Constants;
import com.dangqun.entity.MessageEntity;
import com.dangqun.entity.UserEntity;
import com.dangqun.service.MessageService;
import com.dangqun.service.UserService;
import com.dangqun.service.common.RedisService;
import com.dangqun.utils.DateTimeUtils;
import com.dangqun.vo.AdminNotificationMethodBody;
import com.dangqun.vo.IdBody;
import com.dangqun.vo.SelectMesMethodBody;
import com.dangqun.vo.restful.Result;
import com.dangqun.websocket.WebSocketServer;
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
@RequestMapping("/mes")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @CheckIsManager
    @PostMapping("/adminNotification")
    public Result adminNotification(@RequestBody @Valid AdminNotificationMethodBody body){
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMesName(body.getMesName());
        messageEntity.setMesContent(body.getMesContent());
        messageEntity.setMesBeginTime(DateTimeUtils.getTime());
        messageEntity.setMesType(Constants.MES_TYPE_ADMIN_INFORM);
        messageEntity.setMesStatus(Constants.MES_NOT_CONFIRM_STATUS);
        messageEntity.setMesSendUser(redisService.getUserData().getUserName());
        List<UserEntity> list = userService.selectAll();
        for (UserEntity u : list){
            messageEntity.setMesRecUser(u.getUserName());
            messageService.insertOne(messageEntity);
            if(messageEntity.getMesId() != null){
                webSocketServer.sendMessage(messageEntity.toString(),u.getUserName());
            }
            messageEntity.setMesId(null);
            messageEntity.setMesRecUser(null);
        }
        return Result.success();
    }

    @PostMapping("/mesConfirm")
    public Result mesConfirm(@RequestBody @Valid IdBody body){
        MessageEntity messageEntity = messageService.selectById(body.getId());
        if(messageEntity == null){
            return Result.failure("消息未记录");
        }
        messageEntity.setMesStatus(Constants.MES_CONFIRM_STATUS);
        messageEntity.setMesEndTime(DateTimeUtils.getTime());
        messageEntity.setMesConfirmUser(redisService.getUserData().getUserName());
        int update = messageService.updateMes(messageEntity);
        return update > 0 ? Result.success() : Result.failure();
    }

    @PostMapping("/selectMes")
    @CheckIsManager
    public Result selectMes(@RequestBody @Valid SelectMesMethodBody body){
        return Result.success(messageService.selectMes(body));
    }

}
