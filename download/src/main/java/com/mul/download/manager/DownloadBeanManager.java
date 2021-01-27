package com.mul.download.manager;

import com.mul.download.bean.DownloadBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.manager
 * @ClassName: DownloadBeanManager
 * @Author: liys
 * @CreateDate: 2021/1/27 13:58:15
 * @Description: java类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/27 13:58:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DownloadBeanManager {
    private volatile Map<String, DownloadBean> mMap = new HashMap<>();

    private DownloadBeanManager() {

    }

    private static class DownloadBeanManagerSington {
        private static final DownloadBeanManager DOWNLOAD_BEAN_MANAGER = new DownloadBeanManager();
    }

    public static DownloadBeanManager getInstance() {
        return DownloadBeanManagerSington.DOWNLOAD_BEAN_MANAGER;
    }

    public Map<String, DownloadBean> getMap() {
        return mMap;
    }

    public void save(DownloadBean mDownloadBean) {
        mMap.put(mDownloadBean.getFileName(), mDownloadBean);
        DownloadStatusManager.getInstance().createDownloadStatusService();
    }

    public DownloadBean get(String mFileName) {
        if (mMap.containsKey(mFileName)) {
            return mMap.get(mFileName);
        } else {
            return null;
        }
    }

    public void remove(String mFileName) {
        mMap.remove(mFileName);
    }

    public void clear() {
        mMap.clear();
    }
}
