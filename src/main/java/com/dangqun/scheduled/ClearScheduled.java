package com.dangqun.scheduled;

import com.dangqun.constant.Constants;
import com.dangqun.utils.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author wcy
 */

@Component
public class ClearScheduled {

    public static final int ONE_DAY = 24 * 60 * 60 * 1000;

    @Scheduled(cron = "00 00 00 * * ?")
    public void clearRun(){
        Long now = System.currentTimeMillis();
        File tempPath = new File(Constants.TEMP_DOWNLOAD_PATH);
        if(tempPath.exists()){
            File[] tempFiles = tempPath.listFiles();
            for (File temp : tempFiles){
                if(now - temp.lastModified() > ONE_DAY){
                    FileUtils.delDir(temp.getPath());
                }
            }
        }
    }

}
