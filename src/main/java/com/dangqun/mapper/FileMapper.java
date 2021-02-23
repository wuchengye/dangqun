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
}
