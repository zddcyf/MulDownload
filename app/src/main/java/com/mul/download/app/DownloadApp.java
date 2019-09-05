package com.mul.download.app;

import android.app.Application;

/**
 * @ProjectName: TO_Text
 * @Package: com.iguan.text
 * @ClassName: DownloadApp
 * @Author: zdd
 * @CreateDate: 2019/9/2 12:30
 * @Description: 类的作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/2 12:30
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class DownloadApp extends Application {
    private static DownloadApp to_app;

    public static DownloadApp getInstance() {
        return to_app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        to_app = this;
    }
}
