package com.mul.download.base;

import com.mul.download.listener.OnProgressListener;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.base
 * @ClassName: BaseDownloadController
 * @Author: zdd
 * @CreateDate: 2020/3/18 11:25
 * @Description: 下载类的父控制器
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/3/18 11:25
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
public abstract class BaseDownloadController {
    /**
     * 初始化数据
     */
    public abstract void init();

    /**
     * 开启下载
     *
     * @param mData        数据源
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public abstract void download(Object mData, String downloadPath, String filePath, String fileName, int position, OnProgressListener onProgressListener);
}
