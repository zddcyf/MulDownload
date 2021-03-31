package com.mul.download.proxy;

import android.util.Log;

import com.mul.download.base.BaseDownloadController;
import com.mul.download.bean.DownloadConfigBean;
import com.mul.download.listener.OnProgressListener;
import com.mul.download.controller.DownloadManagerController;
import com.mul.download.enums.DownloadTypeEnum;
import com.mul.download.manager.DownloadManager;
import com.mul.download.manager.DownloadStatusManager;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.proxy
 * @ClassName: DownloadProxy
 * @Author: zdd
 * @CreateDate: 2020/3/18 11:29
 * @Description: 下载类对外提供的调用类
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/3/18 11:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
public class DownloadProxy {
    public static String TAG = "DownloadProxy:::";

    private BaseDownloadController downloadController;
    private DownloadConfigBean downloadConfigBean;
    private Object mData;

    private DownloadProxy() {
    }

    public static DownloadProxy obtain() {
        return DownloadProxyObtain.DOWNLOAD_PROXY;
    }

    private static class DownloadProxyObtain {
        private static final DownloadProxy DOWNLOAD_PROXY = new DownloadProxy();
    }

    /**
     * 初始化下载器
     *
     * @param downloadConfigBean
     */
    public void init(DownloadConfigBean downloadConfigBean) {
        init(DownloadTypeEnum.DOWNLOAD.getType(), downloadConfigBean);
    }

    public void init(int type, DownloadConfigBean downloadConfigBean) {
        this.downloadConfigBean = downloadConfigBean;
        if (type == DownloadTypeEnum.DOWNLOAD.getType()) {
            downloadController = new DownloadManagerController();
        } else {
            downloadController = new DownloadManagerController();
        }
        downloadController.init();
    }

    /**
     * 开启下载
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     */
    public DownloadProxy download(String downloadPath, String filePath, String fileName, OnProgressListener onProgressListener) {
        download(null, downloadPath, filePath, fileName, 0, onProgressListener);
        return this;
    }

    /**
     * 开启下载
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public DownloadProxy download(String downloadPath, String filePath, String fileName, int position, OnProgressListener onProgressListener) {
        download(null, downloadPath, filePath, fileName, position, onProgressListener);
        return this;
    }

    /**
     * 开启下载
     *
     * @param mData        数据源
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     */
    public DownloadProxy download(Object mData, String downloadPath, String filePath, String fileName, OnProgressListener onProgressListener) {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return this;
        }
        downloadController.download(mData, downloadPath, filePath, fileName, 0, onProgressListener);
        return this;
    }

    /**
     * 开启下载
     *
     * @param mData        数据源
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public DownloadProxy download(Object mData, String downloadPath, String filePath, String fileName, int position, OnProgressListener onProgressListener) {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return this;
        }
        downloadController.download(mData, downloadPath, filePath, fileName, position, onProgressListener);
        return this;
    }

    /**
     * 获取配置信息
     *
     * @return
     */
    public DownloadConfigBean getConfigBean() {
        return downloadConfigBean;
    }

    public void remove(long id) {
        DownloadManager.getInstance().remove(id);
    }

    public void release() {
        DownloadManager.getInstance().release();
        DownloadStatusManager.getInstance().release();
    }
}
