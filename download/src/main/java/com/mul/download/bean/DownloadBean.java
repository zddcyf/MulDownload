package com.mul.download.bean;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.bean
 * @ClassName: DownloadBean
 * @Author: zdd
 * @CreateDate: 2019/9/4 15:07
 * @Description: 下载数据信息
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/4 15:07
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class DownloadBean {
    private String fileName;
    private long downloadId;
    private int position;
    private float progress;
    private Object mData;

    public DownloadBean(String fileName, long downloadId, int position, Object mData) {
        this.fileName = fileName;
        this.downloadId = downloadId;
        this.position = position;
        this.mData = mData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object mData) {
        this.mData = mData;
    }
}
