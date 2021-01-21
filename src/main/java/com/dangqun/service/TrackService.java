package com.dangqun.service;

import com.dangqun.entity.TrackEntity;
import com.dangqun.mapper.TrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TrackEntity insertTrackAndUpdateOthers(TrackEntity parentTrack, TrackEntity newTrack) {
        newTrack.setTrackLeftValue(parentTrack.getTrackRightValue());
        newTrack.setTrackRightValue(parentTrack.getTrackRightValue() + 1);

    }
}
