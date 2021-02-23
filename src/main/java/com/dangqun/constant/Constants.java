package com.dangqun.constant;

/**
 * @author wcy
 */
public class Constants {
    /**
     * @date 2021-01-18 11:29
     * 用户相关常量
     */
    public static final String USER_DEFAULT_PASSWORD = "06127a02d497eecc6f17c3ec94b6c79d";

    /**
     * @date 2021-01-18 11:30
     * 权限相关常量
     */
    public static final int AUTH_LEVEL_ADMIN = 0;
    public static final int AUTH_LEVEL_SECONDARY = 2;
    public static final int AUTH_LEVEL_COMMON = 1;
    public static final int AUTH_DEFAULT = 1;
    public static final int AUTH_NOT_DEFAULT = 0;

    /**
     * @date 2021-01-19 11:57
     * 文件及文件夹路径相关常量：路径末尾没有分隔符
     */
    public static final String BRANCH_AND_FILE_PARENT_PATH = "D:\\zzzzz\\dangqun";
    public static final String BRANCH_AND_FILE_PATH_SPLIT = "\\";
    public static final int TRACK_STATUS_SAVE_ALL = 2;
    public static final int TRACK_STATUS_SAVE_FOLDER = 1;
    public static final int TRACK_STATUS_SAVE_FILE = 0;

    /**
     * @date 2021-02-03 19:52
     * 上传文件时，与前端配合的参数
     */
    public static final long FILE_CHUNK_SIZE = 5242880;
}
