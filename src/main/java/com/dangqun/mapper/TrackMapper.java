package com.dangqun.mapper;

import com.dangqun.entity.TrackEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    void updateLeftWhenInsert(int trackBranch, int trackRightValue);

    void updateRightWhenInsert(int trackBranch, int trackRightValue);

    List<TrackEntity> selectSonList(Integer trackBranch,Integer trackLeftValue, Integer trackRightValue);

    void deleteSonList(Integer trackBranch, Integer trackLeftValue, Integer trackRightValue);

    void updateLeftWhenDelete(Integer trackBranch, Integer trackLeftValue, Integer trackRightValue);

    void updateRightWhenDelete(Integer trackBranch, Integer trackLeftValue, Integer trackRightValue);

    List<TrackEntity> selectByParent(Integer trackParentId);

    List<TrackEntity> selectInIds(@Param("trackIds") List<String> trackIds);

    void deleteAllByBranch(Integer id);
}
