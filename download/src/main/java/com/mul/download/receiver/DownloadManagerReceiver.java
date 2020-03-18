package com.mul.download.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.receiver
 * @ClassName: DownloadManagerReceiver
 * @Author: zdd
 * @CreateDate: 2019/9/3 11:03
 * @Description: 类的作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/3 11:03
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class DownloadManagerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            // 下载成功
            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            Toast.makeText(context, intent.getAction() + "id : " + downId, Toast.LENGTH_SHORT).show();
        }
    }
}
