package com.dangqun.utils;

import com.dangqun.constant.Constants;

import java.io.File;

/**
 * @author wcy
 */
public class FileUtils {

    public static void delDir(String path){
        File dir=new File(path);
        if(dir.exists()){
            if(!path.endsWith(Constants.BRANCH_AND_FILE_PATH_SPLIT)){
                //判断路径最后是否含有“/”或“\”，如果没有则补充。
                path += Constants.BRANCH_AND_FILE_PATH_SPLIT;
            }
            File[] tmp=dir.listFiles();
            //如果该目录下还有文件，则递归删除文件，最后删除文件夹
            for(int i=0;i<tmp.length;i++){
                if(tmp[i].isDirectory()){
                    delDir(path+tmp[i].getName());
                }
                else{
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }
}
