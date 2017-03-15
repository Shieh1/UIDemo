package com.zysm.curtain.mwidgetgroup.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Time:2017/3/15 13:38
 * Created by Curtain.
 */

public class SDCardUtils {
    //判断sd卡是否可用
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //获取sd卡路径
    public static String getSDCardPath() {
        File file = Environment.getExternalStorageDirectory();
        String path = file.getAbsolutePath() + File.separator;
        return path;
    }

    //获取系统存储路径
    public static String getRootDirectoryPath() {
        File rootDirectory = Environment.getRootDirectory();
        String path = rootDirectory.getAbsolutePath();
        return path;
    }

    //获取sd卡的剩余存储空间  单位byte
    public static long getSDCardAllSize() {
        StatFs statFs = new StatFs(getSDCardPath());
        //获取空闲的数据块的数量
        long availableBlocksLong = statFs.getAvailableBlocks();
        //获取单个数据块的大小
        long blockSize = statFs.getBlockSize();
        return availableBlocksLong * blockSize;
    }

}
