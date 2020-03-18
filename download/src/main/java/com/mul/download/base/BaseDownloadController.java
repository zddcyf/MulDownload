package com.mul.download.base;

import com.mul.download.bean.DownloadBean;
import com.mul.download.click.OnProgressListener;

import java.util.ArrayList;
import java.util.List;

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
    protected volatile List<DownloadBean> downloadBeans = new ArrayList<>();

    /**
     * 初始化数据
     */
    public abstract void init();

    /**
     * 注册下载服务
     */
    public abstract void registerDownload();

    /**
     * 退出时注销下载服务
     */
    public abstract void unRegisterDownload();

    /**
     * 开启下载(单个文件下载)
     *
     * @param downloadPath 下载路径
     * @param fileName     文件名称
     */
    public abstract void download(String downloadPath, String fileName);

    /**
     * 开启下载(存在列表下载时)
     *
     * @param downloadPath 下载路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public abstract void download(String downloadPath, String fileName, int position);

    /**
     * 开启下载(单个文件下载)
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     */
    public abstract void download(String downloadPath, String filePath, String fileName);

    /**
     * 开启下载(存在列表下载时)
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public abstract void download(String downloadPath, String filePath, String fileName, int position);

    /**
     * 删除下载or取消下载
     *
     * @param id
     */
    public abstract void remoe(long... id);

    /**
     * 设置回调监听
     * @param onProgressListener
     */
    public abstract void setOnProgressListener(OnProgressListener onProgressListener);
}
