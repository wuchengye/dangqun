package com.dangqun.mapper;

import com.dangqun.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wcy
 */
@Mapper
public interface LogMapper {
    void insertOne(LogEntity logEntity);
}
