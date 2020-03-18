package com.mul.download.bean;

import android.content.Context;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.bean
 * @ClassName: DownloadConfigBean
 * @Author: zdd
 * @CreateDate: 2020/3/18 11:37
 * @Description: java类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/3/18 11:37
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
public class DownloadConfigBean {
    private Context mContext; // 上下文
    private String mFilePath; // 文件路径
    private boolean isReset = false; // 是否需要重新下载。不可以重新下载则会提示下载信息
    private boolean isVisibleInDownloadsUi = false; // 显示下载界面
    private boolean isNotificationVisibility = false; // 是否显示后台下载
    private String submit = "正在下载中"; // 不可重新下载的提示信息

    public Context getContext() {
        return mContext;
    }

    public DownloadConfigBean setContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public DownloadConfigBean setFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
        return this;
    }

    public boolean isReset() {
        return isReset;
    }

    public DownloadConfigBean setReset(boolean reset) {
        isReset = reset;
        return this;
    }

    public boolean isNotificationVisibility() {
        return isNotificationVisibility;
    }

    public DownloadConfigBean setNotificationVisibility(boolean notificationVisibility) {
        isNotificationVisibility = notificationVisibility;
        return this;
    }

    public boolean isVisibleInDownloadsUi() {
        return isVisibleInDownloadsUi;
    }

    public DownloadConfigBean setVisibleInDownloadsUi(boolean isVisibleInDownloadsUi) {
        this.isVisibleInDownloadsUi = isVisibleInDownloadsUi;
        return this;
    }

    public String getSubmit() {
        return submit;
    }

    public DownloadConfigBean setSubmit(String submit) {
        this.submit = submit;
        return this;
    }
}
