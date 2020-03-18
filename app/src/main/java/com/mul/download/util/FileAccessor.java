package com.mul.download.util;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.util
 * @ClassName: FileAccessor
 * @Author: zdd
 * @CreateDate: 2019/9/2 9:33
 * @Description: 文件创建的工具类
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/2 9:33
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class FileAccessor {
    public static final String TRANSLATE_MICROSOFT_PATH = "/sdcard/Android/data/com.microsoft.translator/files/Download";
    public static final String TRANSLATE_MICROSOFT_DOWNLOAD_PATH = "/Android/data/com.microsoft.translator/files/Download";

    /**
     * 初始化应用文件夹目录
     */

    public static void initFileAccess() {
        File translateTextDir = new File(TRANSLATE_MICROSOFT_PATH);
        if (!translateTextDir.exists()) {
            translateTextDir.mkdir();
        }
    }

    public static List<File> getFiles(String path) {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            return new ArrayList<>();
        }
        File[] files = fileDir.listFiles();
        if (null == files || files.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.asList(files);
    }

    public static boolean deleteFile(String path) {
        File fileDir = new File(path);
        if (fileDir.exists()) {
            fileDir.delete();
            return true;
        } else {
            return true;
        }
    }


    public static long getFileSize(String data) {
        File f = new File(data);
        if (f.exists() && f.isFile()) {
            Log.i("文件大小", f.length() + "");
            return f.length();
        } else {
            Log.i("文件大小", "读取失败");
            return 0;
        }
    }
}
