package com.mul.download.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mul.download.R;
import com.mul.download.adapter.LanguageDownloadAdapter;
import com.mul.download.bean.LanguageBean;
import com.mul.download.config.EventConfig;
import com.mul.download.config.HttpUrlConfig;
import com.mul.download.download.bean.DownloadBean;
import com.mul.download.download.click.OnProgressListener;
import com.mul.download.download.controller.DownloadManagerController;
import com.mul.download.event.EventBusMessage;
import com.mul.download.util.DataUtils;
import com.mul.download.util.FileAccessor;
import com.mul.download.util.SpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "com.iguan.text.ui.MainActivity";

    private ConstraintLayout title;
    private RecyclerView rv;
    private LanguageDownloadAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        FileAccessor.initFileAccess();
        DataUtils.getInstance();
        DownloadManagerController.getInstance().init(MainActivity.this);
        initView();
        initRv();
        initClick();
    }

    private void initView() {
        title = findViewById(R.id.title);
        rv = findViewById(R.id.rv);
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new LanguageDownloadAdapter();
        rv.setAdapter(rvAdapter);
        rvAdapter.setDatas(DataUtils.getInstance().getDatas());
        rvAdapter.setItemClick(new LanguageDownloadAdapter.ItemClick() {

            @Override
            public void itemSwitchClick(LanguageBean languageBean) {
                Log.i(TAG, "itemSwitchClick::::::切换语言项");
                DownloadManagerController.getInstance().remoe(SpUtil.getInstance().getValue("ceshi", 0L));
                if (!languageBean.isSelect()) {
                    rvAdapter.setDatas(DataUtils.getInstance().updateData(languageBean));
                }
            }

            @Override
            public void itemDownloadClick(LanguageBean languageBean) {
                Log.i(TAG, "itemDownloadClick::::::下载语言项"
                        + ":::::下载的路径:::::" + HttpUrlConfig.DOWNLOAD_LANGUAGE_URL
                        + languageBean.getFileName());
//                DialogActivity.launch(MainActivity.this, false, languageBean);
                DownloadManagerController.getInstance()
                        .download(HttpUrlConfig.DOWNLOAD_LANGUAGE_URL
                                        + languageBean.getFileName()
                                , FileAccessor.TRANSLATE_MICROSOFT_DOWNLOAD_PATH
                                , languageBean.getFileName()
                                , languageBean.getPosition())
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(DownloadBean downloadBean) {
                                Log.i(TAG, "当前id为" + downloadBean.getDownloadId()
                                        + "::::下载进度为::::::" + downloadBean.getProgress()
                                        + "::::第几条::::::" + downloadBean.getPosition()
                                        + "::::语言项列表的长度::::::" + DataUtils.getInstance().getDatas().size());
                                DataUtils.getInstance().getDatas().get(downloadBean.getPosition()).setProgress(downloadBean.getProgress() * 360);
                                DataUtils.getInstance().getDatas().set(downloadBean.getPosition(), DataUtils.getInstance().getDatas().get(downloadBean.getPosition()));
                                rvAdapter.setDatas(DataUtils.getInstance().getDatas());
                            }

                            @Override
                            public void onSuccess(DownloadBean downloadBean) {
                                DataUtils.getInstance().setData();
                                rvAdapter.setDatas(DataUtils.getInstance().getDatas());
                            }
                        });
            }

            @Override
            public void itemLongClick(LanguageBean languageBean) {
                Log.i(TAG, "itemLongClick::::::长按");
                DialogActivity.launch(MainActivity.this, true, languageBean);
            }
        });
    }

    /**
     * 表示事件处理函数的线程在主线程(UI)线程，因此在这里不能进行耗时操作。
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainMessageEvent(EventBusMessage event) {
        if (null != event && EventConfig.FILE_DELETE_SUCCESS.equals(event.getStatusStr())) {
            DataUtils.getInstance().setData();
            rvAdapter.setDatas(DataUtils.getInstance().getDatas());
        }
    }

    private void initClick() {
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DownloadManagerController.getInstance().registerDownload();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DownloadManagerController.getInstance().unRegisterDownload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
