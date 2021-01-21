package com.dangqun.service;

import com.dangqun.entity.TrackEntity;
import com.dangqun.mapper.TrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
