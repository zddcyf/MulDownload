package com.mul.download.observer;

import android.database.ContentObserver;
import android.os.Handler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.observer
 * @ClassName: DownloadChangeObserver
 * @Author: zdd
 * @CreateDate: 2019/9/3 16:57
 * @Description: 类的作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/3 16:57
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class DownloadChangeObserver extends ContentObserver {

    private final Runnable progressRunnable;
    private ScheduledExecutorService mScheduledExecutorService;

    public DownloadChangeObserver(Handler mHandler, Runnable progressRunnable) {
        super(mHandler);
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.progressRunnable = progressRunnable;
    }

    /**
     * 当所监听的Uri发生改变时，就会回调此方法
     *
     * @param selfChange 此值意义不大, 一般情况下该回调值false
     */
    @Override
    public void onChange(boolean selfChange) {
        mScheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 1, TimeUnit.SECONDS); //在子线程中查询
    }
}
