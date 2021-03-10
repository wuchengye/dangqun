package com.dangqun.mapper;

import com.dangqun.entity.MessageEntity;
import com.dangqun.vo.SelectMesMethodBody;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wcy
 */
@Mapper
public interface MessageMapper {

    int insertOne(MessageEntity messageEntity);

    MessageEntity selectById(Integer mesId);

    int updateMes(MessageEntity messageEntity);

    List<MessageEntity> selectByStatusAndRecUser(String userName, int mesStatus);

    List<MessageEntity> selectByStatusAndType(int mesType, int mesStatus);

    List<MessageEntity> selectMes(SelectMesMethodBody body);
}
