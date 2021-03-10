package com.dangqun.mapper;

import com.dangqun.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wcy
 */
@Mapper
public interface FileMapper {
    List<FileEntity> selectAllByTrackId(Integer trackId);

    void insertFile(FileEntity fileEntity);

    FileEntity selectOneById(int id);

    List<FileEntity> selectAllByIdAndTrackId(List<Integer> fileIds, Integer trackId);

    void deleteAllByBranch(Integer id);

    void deleteAllByTracks(List<Integer> ids);

    void deleteByIdsAndTrackId(List<Integer> fileIds, Integer trackId);
}
