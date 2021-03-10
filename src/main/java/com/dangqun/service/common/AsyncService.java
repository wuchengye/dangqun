package com.dangqun.service.common;

import com.dangqun.constant.Constants;
import com.dangqun.entity.BranchEntity;
import com.dangqun.entity.FileEntity;
import com.dangqun.entity.MessageEntity;
import com.dangqun.entity.TrackEntity;
import com.dangqun.service.BranchService;
import com.dangqun.websocket.WebSocketServer;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wcy
 */
@Service
public class AsyncService {
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private BranchService branchService;

    @Async("createDownloadPath")
    public void unZipFile(List<FileEntity> fileList, String parentPath, String userName, TrackEntity track) {
        BranchEntity branchEntity = branchService.selectOneById(track.getTrackBranch());
        for (FileEntity f : fileList){
            File file = new File(f.getFilePath());
            if (file.exists()){
                File temp = new File(parentPath + Constants.BRANCH_AND_FILE_PATH_SPLIT +
                        branchEntity.getBranchName() + "_" + track.getTrackName() + "_" +f.getFileName());
                try {
                    FileUtils.copyFile(file,temp);
                    MessageEntity m = new MessageEntity();
                    m.setMesName("文件下载");
                    m.setMesContent(temp.getPath().replace(Constants.TEMP_DOWNLOAD_PATH + Constants.BRANCH_AND_FILE_PATH_SPLIT,""));
                    webSocketServer.sendMessage(m.toString(),userName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Async("createDownloadPath")
    public void zipFile(List<FileEntity> fileList, String parentPath, String userName, TrackEntity track) {
        BranchEntity branchEntity = branchService.selectOneById(track.getTrackBranch());
        List<String> filePaths = new ArrayList<>();
        for (FileEntity f : fileList){
            filePaths.add(f.getFilePath());
        }
        String zipPath = parentPath + Constants.BRANCH_AND_FILE_PATH_SPLIT +
                branchEntity.getBranchName() + "_" + track.getTrackName() + "_" + filePaths.size() + ".zip";
        System.out.println(zipPath);
        File folder = new File(parentPath);
        if (!folder.exists()){
            folder.mkdirs();
        }
        try {
            com.dangqun.utils.FileUtils.compress(filePaths,zipPath,false);
            MessageEntity m = new MessageEntity();
            m.setMesName("文件下载");
            m.setMesContent(zipPath.replace(Constants.TEMP_DOWNLOAD_PATH + Constants.BRANCH_AND_FILE_PATH_SPLIT,""));
            webSocketServer.sendMessage(m.toString(),userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
