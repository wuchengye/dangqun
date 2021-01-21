package com.dangqun.mapper;

import com.dangqun.entity.TrackEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wcy
 */
@Mapper
public interface TrackMapper {

    List<TrackEntity> selectAllByBranch(Integer trackBranch);

    int insertTrackReturnId(TrackEntity trackEntity);

    int updateTrack(TrackEntity trackEntity);

    TrackEntity selectOneById(Integer trackId);
}
