package com.dangqun.controller;

import com.dangqun.annotation.CheckIsManager;
import com.dangqun.constant.Constants;
import com.dangqun.entity.BranchEntity;
import com.dangqun.entity.TrackEntity;
import com.dangqun.service.BranchService;
import com.dangqun.service.TrackService;
import com.dangqun.vo.AddTrackMethodBody;
import com.dangqun.vo.restful.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @PostMapping("/addTrack")
    @CheckIsManager
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
            if(parentEntity == null || parentEntity.getTrackBranch() != body.getBranchId()){
                return Result.failure("父路径ID错误或支部信息不匹配");
            }
            if(parentEntity.getTrackStatus() == Constants.TRACK_STATUS_SAVE_FILE){
                return Result.failure("父路径下不允许创建路径");
            }
            trackService.insertTrackAndUpdateOthers(parentEntity,newTrack);
        }
    }
}
