package com.mul.download.controller;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mul.download.bean.DownloadBean;
import com.mul.download.click.OnProgressListener;
import com.mul.download.observer.DownloadChangeObserver;
import com.mul.download.receiver.DownloadManagerReceiver;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * @ProjectName: download
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
public class DownloadManagerController {
    private static String TAG = "com.iguan.text.download.controller.DownloadManagerController";
    private Context mContext;
    private DownloadManager manager;

    private DownloadManagerReceiver completeReceiver;
    private DownloadChangeObserver downloadObserver;
    private OnProgressListener onProgressListener;
    private static final int HANDLE_DOWNLOAD = 0x001;
    private volatile List<DownloadBean> downloadBeans = new ArrayList<>();
    private boolean isReset; // 是否需要重新下载。不可以重新下载则会提示下载信息
    private String submit; // 不可重新下载的提示信息

    private DownloadManagerController() {

    }

    public static DownloadManagerController getInstance() {
        return DownloadManagerUtilsSington.DOWNLOAD_MANAGER_UTILS;
    }

    private static class DownloadManagerUtilsSington {
        private static final DownloadManagerController DOWNLOAD_MANAGER_UTILS = new DownloadManagerController();
    }

    public DownloadManagerController init(Context mContext) {
        return init(mContext, false, "正在下载中");
    }

    public DownloadManagerController init(Context mContext, boolean isReset, String submit) {
        this.mContext = mContext;
        this.isReset = isReset;
        this.submit = submit;
        manager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        return this;
    }

    public void registerDownload() {
        registerReceiver();
        registerContentObserver();
    }

    public void unRegisterDownload() {
        for (DownloadBean downloadBean : downloadBeans) {
            remoe(downloadBean.getDownloadId());
//            SpUtil.getInstance().getValue(downloadBean.getFileName(), downloadBean.getDownloadId());
        }
        unRegisterReceiver();
        unregisterContentObserver();
    }

    /**
     * 开启下载(单个文件下载)
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     */
    public DownloadManagerController download(String downloadPath, String filePath, String fileName) {
        return download(downloadPath, filePath, fileName, 0);
    }

    /**
     * 开启下载(存在列表下载时)
     *
     * @param downloadPath 下载路径
     * @param filePath     文件存放路径
     * @param fileName     文件名称
     * @param position     第几个在下载
     */
    public DownloadManagerController download(String downloadPath, String filePath, String fileName, int position) {
        int index = -1;
        for (DownloadBean downloadBean : downloadBeans) {
            if (downloadBean.getFileName().equals(fileName)) {
                if (isReset) {
                    index = downloadBeans.indexOf(downloadBean);
                    remoe(downloadBean.getDownloadId());
                } else {
                    Toast.makeText(mContext, submit, Toast.LENGTH_SHORT).show();
                    return this;
                }
            }
        }

        if (index != -1) {
            downloadBeans.remove(index);
        }

        downloadObserver = new DownloadChangeObserver(downLoadHandler, progressRunnable);
        registerContentObserver();

        Log.i(TAG, downloadPath);
        //创建下载请求
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downloadPath));
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //后台下载
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //显示下载界面
        down.setVisibleInDownloadsUi(true);
        //设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(filePath, fileName);
        //将下载请求放入队列
        downloadBeans.add(new DownloadBean(fileName, manager.enqueue(down), position));
        return this;
    }

    public void remoe(long... id) {
        manager.remove(id);
    }

    @SuppressLint("HandlerLeak")
    public Handler downLoadHandler = new Handler() { //主线程的handler
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (onProgressListener != null && HANDLE_DOWNLOAD == msg.what) {
                //被除数可以为0，除数必须大于0
                if (msg.arg1 >= 0 && msg.arg2 > 0 && downloadBeans.size() > 0) {
                    Log.i(TAG, "数组的长度::::::" + downloadBeans.size() + "::::传入的数组坐标::::" + (int) msg.obj);
                    int position = (int) msg.obj;
                    if (position == downloadBeans.size()) { // 此处是为了防止数据保持同步
                        --position;
                    }
                    if (position > downloadBeans.size()) {
                        position = downloadBeans.size() - 1;
                    }
                    DownloadBean downloadBean = downloadBeans.get(position);
                    float progress = msg.arg1 / (float) msg.arg2;
                    if (progress == 1) {
                        if (downloadBeans.size() == (int) msg.obj) {
                            downloadBeans.remove(downloadBeans.size() - 1);
                        } else {
                            downloadBeans.remove((int) msg.obj);
                        }
//                        SpUtil.getInstance().getValue(downloadBean.getFileName(), 0L);
                        onProgressListener.onSuccess(downloadBean);
                    } else if (progress != downloadBean.getProgress()) {
                        downloadBean.setProgress(progress);
                        onProgressListener.onProgress(downloadBean);
                    }
                }
            }
        }
    };

    /**
     * 对外开发的方法
     *
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    /**
     * 发送Handler消息更新进度和状态
     * 将查询结果从子线程中发往主线程（handler方式），以防止ANR
     */
    private void updateProgress() {
        for (int i = 0; i < downloadBeans.size(); i++) {
            int[] bytesAndStatus = getBytesAndStatus(downloadBeans.get(i).getDownloadId());
//        downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
            downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD, bytesAndStatus[0], bytesAndStatus[1], i));
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
        mContext.registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 注销Receiver
     */
    private void unRegisterReceiver() {
        if (null != completeReceiver) {
            mContext.unregisterReceiver(completeReceiver);
        }
    }

    /**
     * 注册ContentObserver
     */
    private void registerContentObserver() {
        /** observer download change **/
        if (downloadObserver != null) {
            mContext.getContentResolver().registerContentObserver(
                    Uri.parse("content://downloads/my_downloads"), false, downloadObserver);
        }
    }

    /**
     * 注销ContentObserver
     */
    private void unregisterContentObserver() {
        if (downloadObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }
}
