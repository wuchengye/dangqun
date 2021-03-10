package com.dangqun.websocket;

import com.dangqun.constant.Constants;
import com.dangqun.entity.AuthEntity;
import com.dangqun.entity.MessageEntity;
import com.dangqun.entity.UserEntity;
import com.dangqun.service.AuthService;
import com.dangqun.service.MessageService;
import com.dangqun.service.common.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wcy
 */
@Component
@ServerEndpoint("/websocket/{userName}")
public class WebSocketServer {
    private static RedisService redisService;
    private static MessageService messageService;
    private static AuthService authService;

    @Autowired
    private void setRedisService(RedisService redisService){
        WebSocketServer.redisService = redisService;
    }
    @Autowired
    private void setMessageService(MessageService messageService){
        WebSocketServer.messageService = messageService;
    }
    @Autowired
    private void setAuthService(AuthService authService){
        WebSocketServer.authService = authService;
    }

    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userName")String userName){
        if(!StringUtils.isEmpty(userName)){
            String valid = redisService.getLoginValid(userName);
            if(valid != null){
                sessionPools.put(userName,session);
                sendMessage("连接成功",userName);
                UserEntity userEntity = redisService.getUserData(userName);
                if(userEntity != null){
                    List<MessageEntity> mesList = new ArrayList<>();
                    AuthEntity authEntity = authService.selectOneById(userEntity.getUserAuth());
                    if(authEntity.getAuthLevel() == Constants.AUTH_LEVEL_ADMIN){
                        mesList.addAll(messageService.selectByStatusAndRecUser(userName,Constants.MES_NOT_CONFIRM_STATUS));
                        mesList.addAll(messageService.selectByStatusAndType(Constants.MES_TYPE_FORGET_PWD,Constants.MES_NOT_CONFIRM_STATUS));
                    }else {
                        mesList.addAll(messageService.selectByStatusAndRecUser(userName,Constants.MES_NOT_CONFIRM_STATUS));
                    }
                    for (MessageEntity m : mesList){
                        sendMessage(m.toString(),userName);
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session){
        for (Map.Entry<String, Session> entry : sessionPools.entrySet()) {
            if(entry.getValue().getId().equals(session.getId())){
                sessionPools.remove(entry.getKey());
            }
        }
    }

    @OnError
    public void onError(Throwable error){
        error.printStackTrace();
    }

    public void sendMessage(String message,String userName){
        Session session = sessionPools.get(userName);
        if(session != null){
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
