package com.dangqun.utils;

import com.dangqun.constant.Constants;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public static int compress(List<String> filePaths, String zipFilePath, Boolean keepDirStructure) throws IOException {
        byte[] buf = new byte[1024];
        File zipFile = new File(zipFilePath);
        if(!zipFile.exists()){
            zipFile.createNewFile();
        }
        int fileCount = 0;
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for(int i = 0; i < filePaths.size(); i++){
                String relativePath = filePaths.get(i);
                if(StringUtils.isEmpty(relativePath)){
                    continue;
                }
                File sourceFile = new File(relativePath);
                if(sourceFile == null || !sourceFile.exists()){
                    continue;
                }

                FileInputStream fis = new FileInputStream(sourceFile);
                if(keepDirStructure!=null && keepDirStructure){
                    //保持目录结构
                    zos.putNextEntry(new ZipEntry(relativePath));
                }else{
                    //直接放到压缩包的根目录
                    zos.putNextEntry(new ZipEntry(sourceFile.getName()));
                }
                int len;
                while((len = fis.read(buf)) > 0){
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
                fileCount++;
            }
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileCount;
    }
}
