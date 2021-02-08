package com.mul.download.manager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mul.download.bean.DownloadBean;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.mul.download.proxy.DownloadProxy.TAG;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.controller
 * @ClassName: DownloadStatusManager
 * @Author: liys
 * @CreateDate: 2021/1/27 14:31:40
 * @Description: java类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/27 14:31:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DownloadStatusManager {
    private static final int HANDLE_DOWNLOAD_SUCCESS = 0x001;
    private static final int HANDLE_DOWNLOAD_PROGRESS = 0x002;
    private static final int HANDLE_DOWNLOAD_FAILED = 0x003;

    private ScheduledExecutorService mExecutorService;

    private DownloadStatusManager() {

    }

    private static class DownloadStatusManagerSington {
        private static final DownloadStatusManager DOWNLOAD_STATUS_MANAGER = new DownloadStatusManager();
    }

    public static DownloadStatusManager getInstance() {
        return DownloadStatusManagerSington.DOWNLOAD_STATUS_MANAGER;
    }

    public void createDownloadStatusService() {
        if (null == mExecutorService) {
            mExecutorService = Executors.newScheduledThreadPool(1);
        }
        mExecutorService.scheduleAtFixedRate(mDownloadStatusRunnable
                , 0
                , 1, TimeUnit.SECONDS);
    }

    @SuppressLint("HandlerLeak")
    public Handler downLoadHandler = new Handler(Looper.getMainLooper()) { //主线程的handler
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DownloadBean mObj = (DownloadBean) msg.obj;
            if (HANDLE_DOWNLOAD_SUCCESS == msg.what) {
                //被除数可以为0，除数必须大于0
                mObj.getOnProgressListener().onSuccess(mObj);
            } else if (HANDLE_DOWNLOAD_PROGRESS == msg.what) {
                mObj.getOnProgressListener().onProgress(mObj);
            } else if (HANDLE_DOWNLOAD_FAILED == msg.what) {
                mObj.getOnProgressListener().onFailed(mObj);
            }
        }
    };

    Runnable mDownloadStatusRunnable = new Runnable() {
        @Override
        public void run() {
            if (DownloadBeanManager.getInstance().getMap().size() != 0) {
                for (Iterator<Map.Entry<String, DownloadBean>> iterator = DownloadBeanManager.getInstance().getMap().entrySet().iterator(); iterator.hasNext(); ) {
                    Map.Entry<String, DownloadBean> mEntry = iterator.next();
                    DownloadBean mDownloadBean = mEntry.getValue();
                    int[] bytesAndStatus = getBytesAndStatus(mDownloadBean.getDownloadId());
                    if (bytesAndStatus[2] == android.app.DownloadManager.STATUS_FAILED) { // 下载失败
                        Log.i(TAG, "下载失败");
                        DownloadManager.getInstance().remove(mDownloadBean.getDownloadId());
                        iterator.remove();
                        downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD_FAILED, mDownloadBean));
                    } else {
                        float progress = (float) bytesAndStatus[0] / (float) bytesAndStatus[1];
                        if (progress >= 1.0f) {
                            iterator.remove();
                            mDownloadBean.setProgress(100);
                            Log.i(TAG, "下载成功" + "progress=" + progress + "  下载状态" + (progress >= 1.0f));
                            downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD_SUCCESS, mDownloadBean));
                        } else {
                            Log.i(TAG, "下载中");
                            mDownloadBean.setProgress(progress);
                            downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD_PROGRESS, mDownloadBean));
                        }
                    }
                }
            }
        }
    };

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
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = DownloadManager.getInstance().getManager().query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }

    public void release() {
        if (null != mExecutorService) {
            mExecutorService.shutdown();
        }
    }
}
