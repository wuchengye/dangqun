package com.dangqun.service;

import com.dangqun.entity.MessageEntity;
import com.dangqun.mapper.MessageMapper;
import com.dangqun.vo.SelectMesMethodBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wcy
 */
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public int insertOne(MessageEntity messageEntity) {
        return messageMapper.insertOne(messageEntity);
    }

    public MessageEntity selectById(Integer id) {
        return messageMapper.selectById(id);
    }

    public int updateMes(MessageEntity messageEntity) {
        return messageMapper.updateMes(messageEntity);
    }

    public List<MessageEntity> selectByStatusAndRecUser(String userName, int mesStatus) {
        return messageMapper.selectByStatusAndRecUser(userName,mesStatus);
    }

    public List<MessageEntity> selectByStatusAndType(int mesType, int mesStatus) {
        return messageMapper.selectByStatusAndType(mesType,mesStatus);
    }

    public PageInfo selectMes(SelectMesMethodBody body) {
        PageHelper.startPage(body.getPageNum(),body.getPageSize());
        List<MessageEntity> list = messageMapper.selectMes(body);
        return new PageInfo(list);
    }
}
