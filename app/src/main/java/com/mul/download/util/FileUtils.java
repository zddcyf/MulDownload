package com.mul.download.util;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.util
 * @ClassName: FileUtils
 * @Author: zdd
 * @CreateDate: 2019/9/2 15:48
 * @Description: 类的作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/2 15:48
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class FileUtils {
    public static String getFileSize(String data) {
        long fileSize = FileAccessor.getFileSize(data);
        return String.format("%sm", ((int) fileSize/1024/1024));
    }
}
