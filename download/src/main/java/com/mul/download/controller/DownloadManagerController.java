package com.mul.download.controller;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mul.download.base.BaseDownloadController;
import com.mul.download.bean.DownloadBean;
import com.mul.download.click.OnProgressListener;
import com.mul.download.observer.DownloadChangeObserver;
import com.mul.download.proxy.DownloadProxy;
import com.mul.download.receiver.DownloadManagerReceiver;

import java.util.Iterator;

import static android.content.Context.DOWNLOAD_SERVICE;

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
    private static String TAG = "com.mul.download.controller.DownloadManagerController";
    private DownloadManager manager;

    private DownloadManagerReceiver completeReceiver;
    private DownloadChangeObserver downloadObserver;
    private OnProgressListener onProgressListener;
    private static final int HANDLE_DOWNLOAD_SUCCESS = 0x001;
    private static final int HANDLE_DOWNLOAD_PROGRESS = 0x002;
    private static final int HANDLE_DOWNLOAD_FAILED = 0x003;

    @Override
    public void init() {
        manager = (DownloadManager) DownloadProxy.obtain().getConfigBean().getContext().getSystemService(DOWNLOAD_SERVICE);
    }

    @Override
    public void registerDownload() {
        registerReceiver();
        registerContentObserver();
    }

    @Override
    public void unRegisterDownload() {
        for (DownloadBean downloadBean : downloadBeans) {
            remove(downloadBean.getDownloadId());
//            SpUtil.getInstance().getValue(downloadBean.getFileName(), downloadBean.getDownloadId());
        }
        downloadBeans.clear();
        unRegisterReceiver();
        unregisterContentObserver();
    }

    /**
     * 开启下载(单个文件下载)
     *
     * @param downloadPath 下载路径
     * @param fileName     文件名称
     */
    @Override
    public void download(String downloadPath, String fileName) {
        download(downloadPath, DownloadProxy.obtain().getConfigBean().getFilePath(), fileName, 0);
    }

    /**
     * 开启下载(存在列表下载时)
     *
     * @param downloadPath 下载路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    @Override
    public void download(String downloadPath, String fileName, int position) {
        download(downloadPath, DownloadProxy.obtain().getConfigBean().getFilePath(), fileName, position);
    }

    /**
     * 开启下载(单个文件下载)
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     */
    @Override
    public void download(String downloadPath, String filePath, String fileName) {
        download(downloadPath, filePath, fileName, 0);
    }

    /**
     * 开启下载(存在列表下载时)
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    @Override
    public void download(String downloadPath, String filePath, String fileName, int position) {
        if (TextUtils.isEmpty(filePath)) {
            Log.d(TAG, "pleas inti filepath");
            return;
        }

        int index = -1;
        for (DownloadBean downloadBean : downloadBeans) {
            if (downloadBean.getFileName().equals(fileName)) {
                if (DownloadProxy.obtain().getConfigBean().isReset()) {
                    index = downloadBeans.indexOf(downloadBean);
                    remove(downloadBean.getDownloadId());
                } else {
                    Toast.makeText(DownloadProxy.obtain().getConfigBean().getContext()
                            , DownloadProxy.obtain().getConfigBean().getSubmit(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (index != -1) {
            downloadBeans.remove(index);
        }

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
        downloadBeans.add(new DownloadBean(fileName, manager.enqueue(down), position, DownloadProxy.obtain().getData()));
    }

    /**
     * 删除正在下载
     *
     * @param id
     */
    @Override
    public void remove(long... id) {
        manager.remove(id);
    }

    @SuppressLint("HandlerLeak")
    public Handler downLoadHandler = new Handler() { //主线程的handler
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (onProgressListener != null && HANDLE_DOWNLOAD_SUCCESS == msg.what) {
                //被除数可以为0，除数必须大于0
                onProgressListener.onSuccess((DownloadBean) msg.obj);
//                if (msg.arg1 >= 0 && msg.arg2 > 0 && downloadBeans.size() > 0) {
//                    DownloadBean downloadBean = (DownloadBean) msg.obj;
//                    float progress = (float) msg.arg1 / (float) msg.arg2;
//                    Log.i(TAG, "下载中" + "progress=" + progress);
//                    if (progress >= 1.0f) {
//                        Log.i(TAG, "下载成功" + "progress=" + progress + "  下载状态" + (progress >= 1.0f));
//                        downloadBean.setProgress(progress);
//                        remove(downloadBean.getDownloadId());
//                        downloadBeans.remove(downloadBean);
//                        onProgressListener.onSuccess(downloadBean);
//                    } else if (progress != downloadBean.getProgress()) {
//                        Log.i(TAG, "下载中");
//                        downloadBean.setProgress(progress);
//                        onProgressListener.onProgress(downloadBean);
//                    }
//                }
            } else if (onProgressListener != null && HANDLE_DOWNLOAD_PROGRESS == msg.what) {
                onProgressListener.onProgress((DownloadBean) msg.obj);
            } else if (onProgressListener != null && HANDLE_DOWNLOAD_FAILED == msg.what) {
                Log.i(TAG, "下载失败");
//                DownloadBean downloadBean = (DownloadBean) msg.obj;
//                remove(downloadBean.getDownloadId());
//                downloadBeans.remove(downloadBean);
                onProgressListener.onFailed((DownloadBean) msg.obj);
            }
        }
    };

    /**
     * 对外开发的方法
     *
     * @param onProgressListener
     */
    @Override
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                progressRunnable.wait();
                updateProgress();
                progressRunnable.notify();
            } catch (Exception mE) {
                mE.printStackTrace();
            }
        }
    };

    /**
     * 发送Handler消息更新进度和状态
     * 将查询结果从子线程中发往主线程（handler方式），以防止ANR
     */
    private void updateProgress() {
        Iterator<DownloadBean> mIterator = downloadBeans.iterator();
        while (mIterator.hasNext()) {
            DownloadBean mDownloadBean = mIterator.next();
            int[] bytesAndStatus = getBytesAndStatus(mDownloadBean.getDownloadId());
            if (bytesAndStatus[2] == DownloadManager.STATUS_FAILED) {
                remove(mDownloadBean.getDownloadId());
                downloadBeans.remove(mDownloadBean);
                downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD_FAILED, mDownloadBean));
            } else {
                float progress = (float) bytesAndStatus[0] / (float) bytesAndStatus[1];
                if (progress >= 1.0f) {
                    Log.i(TAG, "下载成功" + "progress=" + progress + "  下载状态" + (progress >= 1.0f));
                    remove(mDownloadBean.getDownloadId());
                    downloadBeans.remove(mDownloadBean);
                    downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD_SUCCESS, mDownloadBean));
                } else {
                    Log.i(TAG, "下载中");
                    mDownloadBean.setProgress(progress);
                    downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD_PROGRESS, mDownloadBean));
                }
            }
        }
    }

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId
     * @return
     */
    private int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = manager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }

    /**
     * 注册Receiver
     */
    private void registerReceiver() {
        completeReceiver = new DownloadManagerReceiver();
        /** register download success broadcast **/
        DownloadProxy.obtain().getConfigBean().getContext().registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 注销Receiver
     */
    private void unRegisterReceiver() {
        if (null != completeReceiver) {
            DownloadProxy.obtain().getConfigBean().getContext().unregisterReceiver(completeReceiver);
        }
    }

    /**
     * 注册ContentObserver
     */
    private void registerContentObserver() {
        downloadObserver = new DownloadChangeObserver(downLoadHandler, progressRunnable);
        /** observer download change **/
        if (downloadObserver != null) {
            DownloadProxy.obtain().getConfigBean().getContext().getContentResolver().registerContentObserver(
                    Uri.parse("content://downloads/my_downloads"), false, downloadObserver);
        }
    }

    /**
     * 注销ContentObserver
     */
    private void unregisterContentObserver() {
        if (downloadObserver != null) {
            DownloadProxy.obtain().getConfigBean().getContext().getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }
}
