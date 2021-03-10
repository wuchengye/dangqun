package com.dangqun.service;

import com.dangqun.constant.Constants;
import com.dangqun.entity.TrackEntity;
import com.dangqun.mapper.TrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wcy
 */
@Service
public class TrackService {
    @Autowired
    private TrackMapper trackMapper;

    public List<TrackEntity> selectAllByBranch(Integer trackBranch) {
        return trackMapper.selectAllByBranch(trackBranch);
    }

    public int insertTrackReturnId(TrackEntity trackEntity) {
        return trackMapper.insertTrackReturnId(trackEntity);
    }

    public int updateTrack(TrackEntity trackEntity) {
        return trackMapper.updateTrack(trackEntity);
    }

    public int findMaxRightValue(List<TrackEntity> entityList){
        int maxRight = 0;
        for (TrackEntity t : entityList){
            if(maxRight < t.getTrackRightValue()){
                maxRight = t.getTrackRightValue();
            }
        }
        return maxRight;
    }

    public TrackEntity selectOneById(Integer trackId) {
        return trackMapper.selectOneById(trackId);
    }

    @Transactional(rollbackFor = Exception.class)
    public TrackEntity insertTrackAndUpdateOthers(TrackEntity parentTrack, TrackEntity newTrack) throws Exception {
        newTrack.setTrackLeftValue(parentTrack.getTrackRightValue());
        newTrack.setTrackRightValue(parentTrack.getTrackRightValue() + 1);
        newTrack.setTrackParentId(parentTrack.getTrackId());
        //更新父路径状态，再更新当前支部内部路径左右值，顺序不可调换
        parentTrack.setTrackStatus(Constants.TRACK_STATUS_SAVE_FOLDER);
        trackMapper.updateTrack(parentTrack);
        trackMapper.updateLeftWhenInsert(newTrack.getTrackBranch(),newTrack.getTrackLeftValue());
        trackMapper.updateRightWhenInsert(newTrack.getTrackBranch(),newTrack.getTrackLeftValue());
        trackMapper.insertTrackReturnId(newTrack);
        newTrack.setTrackInnerPath(Constants.BRANCH_AND_FILE_PATH_SPLIT + newTrack.getTrackId());
        newTrack.setTrackFullPath(parentTrack.getTrackFullPath() + newTrack.getTrackInnerPath());
        int update = updateTrack(newTrack);
        if (update == 0){
            throw new Exception("新增失败");
        }else {
            return newTrack;
        }
    }

    public List<TrackEntity> selectSonList(TrackEntity deleteEntity) {
        return trackMapper.selectSonList(deleteEntity.getTrackBranch(),deleteEntity.getTrackLeftValue(),deleteEntity.getTrackRightValue());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTrackAndUpdateOthers(TrackEntity deleteEntity) {
        trackMapper.deleteSonList(deleteEntity.getTrackBranch(),deleteEntity.getTrackLeftValue(),deleteEntity.getTrackRightValue());
        trackMapper.updateLeftWhenDelete(deleteEntity.getTrackBranch(),deleteEntity.getTrackLeftValue(),deleteEntity.getTrackRightValue());
        trackMapper.updateRightWhenDelete(deleteEntity.getTrackBranch(),deleteEntity.getTrackLeftValue(),deleteEntity.getTrackRightValue());
    }

    public List<TrackEntity> selectByParent(Integer trackParentId) {
        return trackMapper.selectByParent(trackParentId);
    }

    public List<TrackEntity> selectInIds(String[] ids) {
        List<String> list = Arrays.asList(ids);
        return trackMapper.selectInIds(list);
    }

    public void deleteAllByBranch(Integer id) {
        trackMapper.deleteAllByBranch(id);
    }
}
