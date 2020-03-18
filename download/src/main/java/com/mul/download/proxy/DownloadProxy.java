package com.mul.download.proxy;

import android.util.Log;

import com.mul.download.base.BaseDownloadController;
import com.mul.download.bean.DownloadConfigBean;
import com.mul.download.click.OnProgressListener;
import com.mul.download.controller.DownloadManagerController;
import com.mul.download.enums.DownloadTypeEnum;

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
    private static String TAG = "com.mul.download.proxy.DownloadProxy";

    private BaseDownloadController downloadController;
    private DownloadConfigBean downloadConfigBean;

    private DownloadProxy() {
    }

    public static DownloadProxy obtain() {
        return DownloadProxyObtain.DOWNLOAD_PROXY;
    }

    private static class DownloadProxyObtain {
        private static final DownloadProxy DOWNLOAD_PROXY = new DownloadProxy();
    }

    /**
     * 注册服务
     */
    public void registerDownload() {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return;
        }
        downloadController.registerDownload();
    }

    /**
     * 注销服务
     */
    public void unRegisterDownload() {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return;
        }
        downloadController.unRegisterDownload();
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
     * 开启下载(单个文件下载)
     *
     * @param downloadPath 下载路径
     * @param fileName     文件名称
     */
    public DownloadProxy download(String downloadPath, String fileName) {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return this;
        }
        downloadController.download(downloadPath, fileName);
        return this;
    }

    /**
     * 开启下载(存在列表下载时)
     *
     * @param downloadPath 下载路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public DownloadProxy download(String downloadPath, String fileName, int position) {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return this;
        }
        downloadController.download(downloadPath, fileName, position);
        return this;
    }

    /**
     * 开启下载(单个文件下载)
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     */
    public DownloadProxy download(String downloadPath, String filePath, String fileName) {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return this;
        }
        downloadController.download(downloadPath, filePath, fileName);
        return this;
    }

    /**
     * 开启下载(存在列表下载时)
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public DownloadProxy download(String downloadPath, String filePath, String fileName, int position) {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return this;
        }
        downloadController.download(downloadPath, filePath, fileName, position);
        return this;
    }

    /**
     * 删除下载or取消下载
     *
     * @param id
     */
    public void remoe(long... id) {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return;
        }
        downloadController.remoe(id);
    }

    /**
     * 设置回调监听
     *
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        if (null == downloadController) {
            Log.d(TAG, "please call the init()");
            return;
        }
        downloadController.setOnProgressListener(onProgressListener);
    }

    /**
     * 获取配置信息
     *
     * @return
     */
    public DownloadConfigBean getConfigBean() {
        return downloadConfigBean;
    }
}
