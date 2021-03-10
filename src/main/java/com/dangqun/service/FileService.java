package com.dangqun.service;

import com.dangqun.config.impl.ValidList;
import com.dangqun.entity.FileEntity;
import com.dangqun.entity.TrackEntity;
import com.dangqun.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public FileEntity selectOneById(int id) {
        return fileMapper.selectOneById(id);
    }

    public List<FileEntity> selectAllByIdAndTrackId(List<Integer> fileIds, Integer trackId) {
        return fileMapper.selectAllByIdAndTrackId(fileIds,trackId);
    }

    public void deleteAllByBranch(Integer id) {
        fileMapper.deleteAllByBranch(id);
    }

    public void deleteAllByTracks(List<TrackEntity> deleteSonList) {
        List<Integer> ids = new ArrayList<>();
        for (TrackEntity t : deleteSonList){
            ids.add(t.getTrackId());
        }
        fileMapper.deleteAllByTracks(ids);
    }

    public void deleteByIdsAndTrackId(List<Integer> fileIds, Integer trackId) {
        fileMapper.deleteByIdsAndTrackId(fileIds,trackId);
    }
}
