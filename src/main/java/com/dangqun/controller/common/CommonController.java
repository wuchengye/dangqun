package com.dangqun.controller.common;

import com.dangqun.annotation.FileExceptionRollBack;
import com.dangqun.constant.Constants;
import com.dangqun.entity.*;
import com.dangqun.service.*;
import com.dangqun.service.common.RedisService;
import com.dangqun.utils.FileMd5Util;
import com.dangqun.vo.IdBody;
import com.dangqun.vo.MultipartFileParam;
import com.dangqun.vo.restful.Result;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wcy
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private AuthService authService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @PostMapping("/getBranchAndTrack")
    public Result getBranchAndTrack(){
        UserEntity userEntity = redisService.getUserData();
        AuthEntity authEntity = authService.selectOneById(userEntity.getUserAuth());
        if(authEntity == null){
            return Result.failure("权限检测失败");
        }
        switch (authEntity.getAuthLevel()){
            case Constants.AUTH_LEVEL_ADMIN:
                List<Map> adminList = new ArrayList<>();
                List<BranchEntity> allBranch = branchService.selectAll();
                for (BranchEntity b : allBranch){
                    Map<String,Object> map = new HashMap<>(3);
                    map.put("branch",b);
                    map.put("track",trackService.selectAllByBranch(b.getBranchId()));
                    map.put("isOwn",true);
                    adminList.add(map);
                }
                return Result.success(adminList);
            case Constants.AUTH_LEVEL_COMMON:
                BranchEntity branch = branchService.selectOneById(userEntity.getUserBranch());
                Map<String,Object> map = new HashMap<>(3);
                map.put("branch",branch);
                map.put("track",trackService.selectAllByBranch(branch.getBranchId()));
                map.put("isOwn",true);
                return Result.success(map);
            case Constants.AUTH_LEVEL_SECONDARY:
                BranchEntity ownBranch = branchService.selectOneById(userEntity.getUserBranch());
                List<Map> commList = new ArrayList<>();
                String[] branches = authEntity.getAuthBranch().split(";");
                String[] tracks = authEntity.getAuthBranchPath().split(";");
                for(int index = 0; index < branches.length; index++){
                    Map<String ,Object> map1 = new HashMap<>(3);
                    int b = Integer.parseInt(branches[index]);
                    if(b == ownBranch.getBranchId()){
                        map1.put("branch",ownBranch);
                        map1.put("track",trackService.selectAllByBranch(b));
                        map1.put("isOwn",true);
                        commList.add(map1);
                    }else {
                        String[] ts = tracks[index].split(",");
                        map1.put("branch",branchService.selectOneById(b));
                        map1.put("track",trackService.selectInIds(ts));
                        map1.put("isOwn",false);
                        commList.add(map1);
                    }
                }
                return Result.success(commList);
            default:
                return Result.failure();
        }
    }

    @PostMapping("/getFiles")
    public Result getFiles(@RequestBody @Valid IdBody body){
        UserEntity userEntity = redisService.getUserData();
        AuthEntity authEntity = authService.selectOneById(userEntity.getUserAuth());
        switch (authEntity.getAuthLevel()){
            case Constants.AUTH_LEVEL_ADMIN:
                return Result.success(fileService.selectAllByTrackId(body.getId()));
            case Constants.AUTH_LEVEL_COMMON:
                TrackEntity track = trackService.selectOneById(body.getId());
                if (track == null || track.getTrackBranch().intValue() != userEntity.getUserBranch()){
                    return Result.failure("无权查看文件");
                }
                return Result.success(fileService.selectAllByTrackId(body.getId()));
            case Constants.AUTH_LEVEL_SECONDARY:
                TrackEntity track1 = trackService.selectOneById(body.getId());
                if(track1 != null && track1.getTrackBranch().intValue() == userEntity.getUserBranch()){
                    return Result.success(fileService.selectAllByTrackId(body.getId()));
                }
                String branchPath = authEntity.getAuthBranchPath();
                String[] trackIds = branchPath.split(";|,");
                String id = body.getId().toString();
                for (String trackId : trackIds){
                    if(trackId.equals(id)){
                        return Result.success(fileService.selectAllByTrackId(body.getId()));
                    }
                }
                return Result.failure("无权查看文件");
            default:
                return Result.failure();
        }
    }

    @PostMapping("/uploadFile")
    @FileExceptionRollBack
    public Result uploadFile(@Valid MultipartFileParam param, MultipartFile file, HttpServletRequest request){
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        boolean flag = false;
        if(isMultipart){
            UserEntity userEntity = redisService.getUserData();
            AuthEntity authEntity = authService.selectOneById(userEntity.getUserAuth());
            TrackEntity track = trackService.selectOneById(param.getTrackId());
            try{
                switch (authEntity.getAuthLevel()){
                    case Constants.AUTH_LEVEL_ADMIN:
                        if(track.getTrackStatus() == Constants.TRACK_STATUS_SAVE_FOLDER){
                            return Result.failure("不能存放文件");
                        }
                        flag = doUpload(userEntity,param,file,track);
                        break;
                    default:
                        if(userEntity.getUserBranch().intValue() != track.getTrackBranch() ||
                                track.getTrackStatus() == Constants.TRACK_STATUS_SAVE_FOLDER){
                            return Result.failure("无权限或不能存放文件");
                        }
                        flag = doUpload(userEntity,param,file,track);
                        break;
                }
            }catch (FileExistsException f){
                return Result.failure(f.getMessage());
            }
            catch (IOException e) {
                String tempFileName = track.getTrackFullPath() + Constants.BRANCH_AND_FILE_PATH_SPLIT +
                        userEntity.getUserId() + "_" +  param.getName() + "_temp";
                File temp = new File(tempFileName);
                File conf = new File(tempFileName + ".conf");
                if(temp.exists()){
                    temp.delete();
                }
                if (conf.exists()){
                    conf.delete();
                }
                return Result.failure();
            }
            if (flag){
                track.setTrackStatus(Constants.TRACK_STATUS_SAVE_FILE);
                trackService.updateTrack(track);
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(param.getName());
                fileEntity.setFilePath(track.getTrackFullPath() + Constants.BRANCH_AND_FILE_PATH_SPLIT + param.getName());
                fileEntity.setFileTrack(track.getTrackId());
                fileEntity.setFileBranch(track.getTrackBranch());
                fileService.insertFile(fileEntity);
                return Result.success("上传完成");
            }
            return Result.success("分片：" + param.getChunk() + " 上传完成，总分片：" + param.getChunks());
        }
        return Result.failure();
    }

    private boolean doUpload(UserEntity userEntity, MultipartFileParam param, MultipartFile file, TrackEntity track) throws IOException {
        List<FileEntity> fileList = fileService.selectAllByTrackId(track.getTrackId());
        for (FileEntity f : fileList){
            if(f.getFileName().equals(param.getName())){
                throw new FileExistsException("文件存在");
            }
        }
        String tempFileName = track.getTrackFullPath() + Constants.BRANCH_AND_FILE_PATH_SPLIT +
                                userEntity.getUserId() + "_" +  param.getName() + "_temp";
        File tempFile = new File(tempFileName);
        //在第一次传的时候，检查文件夹下是否有同名临时文件缓存，有则删除
        if()
        RandomAccessFile tempRaf = new RandomAccessFile(tempFile,"rw");
        FileChannel fileChannel = tempRaf.getChannel();
        //写入分片
        long offset = Constants.FILE_CHUNK_SIZE * param.getChunk();
        byte[] fileData = file.getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        //释放
        FileMd5Util.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        boolean isOk = checkAndSetUploadProgress(param,file,tempFileName);
        //重命名文件
        if(isOk){
            File folder = new File(track.getTrackFullPath());
            File[] fs = folder.listFiles();
            for (File f : fs){
                if (f.getName().equals(param.getName())){
                    tempFile.delete();
                    throw new FileExistsException("文件存在");
                }
            }
            File newFile = new File(track.getTrackFullPath() + Constants.BRANCH_AND_FILE_PATH_SPLIT + param.getName());
            tempFile.renameTo(newFile);
            return true;
        }
        return false;
    }

    private boolean checkAndSetUploadProgress(MultipartFileParam param, MultipartFile file, String tempFileName) throws IOException {
        File conf = new File(tempFileName + ".conf");
        RandomAccessFile accessConfFile = new RandomAccessFile(conf, "rw");
        accessConfFile.setLength(param.getChunks());
        accessConfFile.seek(param.getChunk());
        accessConfFile.write(Byte.MAX_VALUE);
        //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = FileUtils.readFileToByteArray(conf);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
        }
        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            conf.delete();
            return true;
        }else {
            return false;
        }
    }

}
