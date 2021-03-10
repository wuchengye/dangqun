package com.dangqun.service;

import com.dangqun.entity.LogEntity;
import com.dangqun.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wcy
 */
@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;

    public void insertOne(LogEntity logEntity) {
        logMapper.insertOne(logEntity);
    }
}
