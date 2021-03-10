package com.dangqun.controller;

import com.dangqun.annotation.CheckIsManager;
import com.dangqun.constant.Constants;
import com.dangqun.entity.BranchEntity;
import com.dangqun.entity.TrackEntity;
import com.dangqun.service.BranchService;
import com.dangqun.service.FileService;
import com.dangqun.service.TrackService;
import com.dangqun.utils.FileUtils;
import com.dangqun.vo.AddTrackMethodBody;
import com.dangqun.vo.DeleteTrackMethodBody;
import com.dangqun.vo.restful.Result;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.io.File;
import java.util.List;

/**
 * @author wcy
 */
@RequestMapping("/track")
@RestController
public class TrackController {
    @Autowired
    private TrackService trackService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private FileService fileService;

    @PostMapping("/addTrack")
    @CheckIsManager
    @Synchronized
    public Result addTrack(@RequestBody @Valid AddTrackMethodBody body){
        BranchEntity branchEntity = branchService.selectOneById(body.getBranchId());
        if (branchEntity == null){
            return Result.failure("支部不存在");
        }
        TrackEntity newTrack = new TrackEntity();
        newTrack.setTrackName(body.getTrackName());
        newTrack.setTrackBranch(body.getBranchId());
        newTrack.setTrackStatus(Constants.TRACK_STATUS_SAVE_ALL);
        if(body.getTrackParentId() == null){
            //文件夹创建在最顶层
            List<TrackEntity> list = trackService.selectAllByBranch(body.getBranchId());
            if(list.size() == 0){
                //当前支部没有文件夹
                newTrack.setTrackLeftValue(1);
                newTrack.setTrackRightValue(2);
            }else {
                int maxRight = trackService.findMaxRightValue(list);
                newTrack.setTrackLeftValue(maxRight + 1);
                newTrack.setTrackRightValue(maxRight + 2);
            }
            trackService.insertTrackReturnId(newTrack);
            newTrack.setTrackInnerPath(Constants.BRANCH_AND_FILE_PATH_SPLIT + newTrack.getTrackId());
            newTrack.setTrackFullPath(branchEntity.getBranchRootPath() + newTrack.getTrackInnerPath());
            int update = trackService.updateTrack(newTrack);
            if(update == 0){
                return Result.failure("新增失败");
            }
        }else {
            TrackEntity parentEntity = trackService.selectOneById(body.getTrackParentId());
            if(parentEntity == null || parentEntity.getTrackBranch() != body.getBranchId().intValue()){
                return Result.failure("父路径ID错误或支部信息不匹配");
            }
            if(parentEntity.getTrackStatus() == Constants.TRACK_STATUS_SAVE_FILE){
                return Result.failure("父路径下不允许创建路径");
            }
            try {
                newTrack = trackService.insertTrackAndUpdateOthers(parentEntity,newTrack);
            } catch (Exception e) {
                return Result.failure(e.getMessage());
            }
        }
        File file = new File(newTrack.getTrackFullPath());
        file.mkdirs();
        return Result.success();
    }

    @PostMapping("/deleteTrack")
    @CheckIsManager
    @Synchronized
    public Result deleteTrack(@RequestBody @Valid DeleteTrackMethodBody body){
        TrackEntity deleteEntity = trackService.selectOneById(body.getTrackId());
        if(deleteEntity == null || deleteEntity.getTrackBranch() != body.getTrackBranch().intValue()){
            return Result.failure("参数有误");
        }
        List<TrackEntity> deleteSonList = trackService.selectSonList(deleteEntity);
        //在路径表中删除
        trackService.deleteTrackAndUpdateOthers(deleteEntity);
        if(deleteEntity.getTrackParentId() != null){
            //更新父路径状态
            List<TrackEntity> parentSonList = trackService.selectByParent(deleteEntity.getTrackParentId());
            if(parentSonList.size() == 0){
                TrackEntity parent = trackService.selectOneById(deleteEntity.getTrackParentId());
                parent.setTrackStatus(Constants.TRACK_STATUS_SAVE_ALL);
                trackService.updateTrack(parent);
            }
        }
        //删除路径及其子路径中对应文件表
        fileService.deleteAllByTracks(deleteSonList);
        //递归删除文件夹
        FileUtils.delDir(deleteEntity.getTrackFullPath());
        return Result.success();
    }
}
