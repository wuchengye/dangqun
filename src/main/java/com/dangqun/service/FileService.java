package com.dangqun.service;

import com.dangqun.entity.FileEntity;
import com.dangqun.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wcy
 */
@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;

    public List<FileEntity> selectAllByTrackId(Integer trackId) {
        return fileMapper.selectAllByTrackId(trackId);
    }

    public void insertFile(FileEntity fileEntity) {
        fileMapper.insertFile(fileEntity);
    }
}
