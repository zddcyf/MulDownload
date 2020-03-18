package com.mul.download.enums;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.enums
 * @ClassName: DownloadTypeEnum
 * @Author: zdd
 * @CreateDate: 2020/3/18 11:27
 * @Description: java类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/3/18 11:27
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
public enum DownloadTypeEnum {
    DOWNLOAD(1); // 系统下载

    private int type;

    DownloadTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
