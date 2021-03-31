package com.mul.download.controller;

import android.app.DownloadManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mul.download.base.BaseDownloadController;
import com.mul.download.bean.DownloadBean;
import com.mul.download.listener.OnProgressListener;
import com.mul.download.manager.DownloadBeanManager;
import com.mul.download.manager.DownloadStatusManager;
import com.mul.download.proxy.DownloadProxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mul.download.proxy.DownloadProxy.TAG;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.controller
 * @ClassName: DownloadManagerController
 * @Author: zdd
 * @CreateDate: 2019/9/3 10:56
 * @Description: DownloadManager 下载功能
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/3 10:56
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class DownloadManagerController extends BaseDownloadController {
    private ExecutorService mExecutorService;

    @Override
    public void init() {
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 开启下载(存在列表下载时)
     *
     * @param mData 数据源
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public void download(Object mData, String downloadPath, String filePath, String fileName, int position, OnProgressListener onProgressListener) {
        if (TextUtils.isEmpty(filePath)) {
            Log.d(TAG, "please inti filepath");
            return;
        }

        DownloadBean mDownloadBean = DownloadBeanManager.getInstance().get(fileName);
        if (null != mDownloadBean) {
            // 同一个文件是否可以重复下载
            if (DownloadProxy.obtain().getConfigBean().isReset()) {
                com.mul.download.manager.DownloadManager.getInstance().getManager().remove(mDownloadBean.getDownloadId());
                DownloadBeanManager.getInstance().remove(fileName);
            } else {
                if (!TextUtils.isEmpty(DownloadProxy.obtain().getConfigBean().getSubmit())) {
                    Toast.makeText(DownloadProxy.obtain().getConfigBean().getContext()
                            , DownloadProxy.obtain().getConfigBean().getSubmit(), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

        DownloadStatusManager.getInstance().createDownloadStatusService();
        mExecutorService.execute(new DownloadRunnable(mData, downloadPath, filePath, fileName, position, onProgressListener));
    }

    private class DownloadRunnable implements Runnable {
        private Object mData;
        private String downloadPath;
        private String filePath;
        private String fileName;
        private int position;
        private OnProgressListener onProgressListener;

        public DownloadRunnable(Object mData, String downloadPath, String filePath, String fileName, int position, OnProgressListener onProgressListener) {
            this.mData = mData;
            this.downloadPath = downloadPath;
            this.filePath = filePath;
            this.fileName = fileName;
            this.position = position;
            this.onProgressListener = onProgressListener;
        }

        @Override
        public void run() {
            Log.i(TAG, downloadPath);
            //创建下载请求
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downloadPath));
            //设置允许使用的网络类型，这里是移动网络和wifi都可以
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            //后台下载
            down.setNotificationVisibility(DownloadProxy.obtain().getConfigBean().isNotificationVisibility()
                    ? DownloadManager.Request.VISIBILITY_VISIBLE : DownloadManager.Request.VISIBILITY_HIDDEN);
            //显示下载界面
            down.setVisibleInDownloadsUi(DownloadProxy.obtain().getConfigBean().isVisibleInDownloadsUi());
            //设置下载后文件存放的位置
            down.setDestinationInExternalPublicDir(filePath, fileName);
            //将下载请求放入队列
            DownloadBeanManager.getInstance().save(new DownloadBean(onProgressListener, fileName, com.mul.download.manager.DownloadManager.getInstance().getManager().enqueue(down), position, mData));
        }
    }
}
