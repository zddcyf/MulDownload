package com.mul.download.manager;

import com.mul.download.bean.DownloadBean;
import com.mul.download.proxy.DownloadProxy;

import java.util.Map;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.manager
 * @ClassName: DownloadManager
 * @Author: liys
 * @CreateDate: 2021/1/27 15:00:30
 * @Description: java类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/27 15:00:30
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DownloadManager {
    private android.app.DownloadManager manager;

    private DownloadManager() {
        manager = (android.app.DownloadManager) DownloadProxy.obtain().getConfigBean().getContext().getSystemService(DOWNLOAD_SERVICE);
    }

    private static class DownloadManagerSington {
        private static final DownloadManager DOWNLOAD_MANAGER = new DownloadManager();
    }

    public static DownloadManager getInstance() {
        return DownloadManagerSington.DOWNLOAD_MANAGER;
    }

    public android.app.DownloadManager getManager() {
        return manager;
    }

    public void release() {
        for (Map.Entry<String, DownloadBean> mEntry : DownloadBeanManager.getInstance().getMap().entrySet()) {
            manager.remove(mEntry.getValue().getDownloadId());
        }
        DownloadBeanManager.getInstance().clear();
    }

    public void remove(long id) {
        if (null != manager) {
            manager.remove(id);
        }
    }
}
