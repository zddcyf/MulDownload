package com.mul.download.download.click;

import com.mul.download.download.bean.DownloadBean;

/**
 * @ProjectName: TO_Text
 * @Package: com.iguan.text.download.click
 * @ClassName: OnProgressListener
 * @Author: zdd
 * @CreateDate: 2019/9/3 17:09
 * @Description: 下载进度回调类
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/3 17:09
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public interface OnProgressListener {
    /**
     * 下载进度
     */
    void onProgress(DownloadBean downloadBean);

    /**
     * 下载成功
     */
    void onSuccess(DownloadBean downloadBean);
}
