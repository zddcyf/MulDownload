package com.mul.download.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mul.download.R;
import com.mul.download.bean.LanguageBean;
import com.mul.download.config.EventConfig;
import com.mul.download.event.EventBusMessage;
import com.mul.download.util.DataUtils;
import com.mul.download.util.FileAccessor;
import com.mul.download.util.LanguageNameUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @ProjectName: TO_Text
 * @Package: com.iguan.text.ui
 * @ClassName: DialogActivity
 * @Author: zdd
 * @CreateDate: 2019/9/3 9:45
 * @Description: 弹框ui。下载和删除的确认弹框
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/3 9:45
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class DialogActivity extends AppCompatActivity implements View.OnClickListener {
    private static String DELETE = "delete";
    private static String LANGUAGEBEAN = "languageBean";

    private ConstraintLayout title;
    private AppCompatTextView content;
    private AppCompatImageView ok, cancel;
    private boolean delete; // 是否删除
    private LanguageBean languageBean; // 语言项信息

    public static void launch(Context mContext, boolean delete, LanguageBean languageBean) {
        Intent intent = new Intent(mContext, DialogActivity.class);
        intent.putExtra(DELETE, delete);
        intent.putExtra(LANGUAGEBEAN, languageBean);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Intent intent = getIntent();
        if (null != intent) {
            delete = intent.getBooleanExtra(DELETE, true);
            languageBean = intent.getParcelableExtra(LANGUAGEBEAN);
        }
        initView();
        initClick();
    }

    private void initView() {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        ok = findViewById(R.id.ok);
        cancel = findViewById(R.id.cancel);
        content.setText(delete ? String.format("删除离线版本%s%s", languageBean.getName(), languageBean.getFileSize())
                : String.format("该语言包为%s,离线语音包可以在没有网络连接时翻译。", languageBean.getFileSize()));
    }

    private void initClick() {
        title.setOnClickListener(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title:
                finish();
                break;
            case R.id.ok:
                languageBean.setDownload(true);
                DataUtils.getInstance().put(LanguageNameUtils.getLanguageName(languageBean.getName()), languageBean);
                FileAccessor.deleteFile(String.format("%s/%s", FileAccessor.TRANSLATE_MICROSOFT_PATH, languageBean.getFileName()));
                EventBus.getDefault().post(new EventBusMessage(EventConfig.FILE_DELETE_SUCCESS));
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
