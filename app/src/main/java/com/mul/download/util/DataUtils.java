package com.mul.download.util;

import android.text.TextUtils;

import com.mul.download.bean.LanguageBean;
import com.mul.download.config.FileConfig;
import com.mul.download.config.LanguageCodeConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.util
 * @ClassName: DataUtils
 * @Author: zdd
 * @CreateDate: 2019/9/2 9:32
 * @Description: 需要下载的文件的数据
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/2 9:32
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class DataUtils {
    private List<LanguageBean> languageBeans = new ArrayList<>();
    private Map<String, LanguageBean> downloadLanguageBeans = new HashMap();

    private DataUtils() {
        initData();
        setData();
    }

    private static class DataUtilsSingleton {
        private static final DataUtils DATA_UTILS = new DataUtils();
    }

    public static DataUtils getInstance() {
        return DataUtilsSingleton.DATA_UTILS;
    }

    private void initData() {
        // 未下载的数据
        /**
         * 德语
         */
        downloadLanguageBeans.put(LanguageNameUtils.getLanguageName(FileConfig.DE), new LanguageBean(LanguageCodeConfig.DE
                , LanguageNameUtils.getLanguageName(FileConfig.DE), FileConfig.DE, "", true, true, false));
        downloadLanguageBeans.put(LanguageNameUtils.getLanguageName(FileConfig.AR), new LanguageBean(LanguageCodeConfig.AR
                , LanguageNameUtils.getLanguageName(FileConfig.AR), FileConfig.AR, "", true, true, false));
    }

    public void setData() {
        languageBeans.clear();
        languageBeans.add(new LanguageBean(0, LanguageNameUtils.getLanguageName(FileConfig.DOWNLOADED), "", "", false, false, false));
        List<File> files = FileAccessor.getFiles(FileAccessor.TRANSLATE_MICROSOFT_PATH);
        for (File file : files) {
            if (!TextUtils.isEmpty(file.getName())) {
                if (FileConfig.CMN_HANS_CN.equals(file.getName())
                        || FileConfig.CMN_HANT_TW.equals(file.getName())
                        || FileConfig.JA_JP.equals(file.getName())
                        || FileConfig.KO_KR.equals(file.getName())
                        || FileConfig.ES_US.equals(file.getName())) {
                    languageBeans.add(new LanguageBean(3
                            , LanguageNameUtils.getLanguageName(file.getName())
                            , file.getName()
                            , FileUtils.getFileSize(FileAccessor.TRANSLATE_MICROSOFT_PATH + "/" + file.getName())
                            , false
                            , false
                            , languageBeans.size() == 1 ? true : false));
                } else {
//                    if (SpUtil.getInstance().getValue(LanguageNameUtils.getLanguageName(file.getName()), 0L) == 0L) {
                        languageBeans.add(new LanguageBean(3
                                , LanguageNameUtils.getLanguageName(file.getName())
                                , file.getName()
                                , FileUtils.getFileSize(FileAccessor.TRANSLATE_MICROSOFT_PATH + "/" + file.getName())
                                , false
                                , true
                                , false));
                        downloadLanguageBeans.remove(LanguageNameUtils.getLanguageName(file.getName()));
//                    }
                }
            }
        }
        /**
         * 未下载的数据
         */
        if (downloadLanguageBeans.size() > 0) {
            languageBeans.add(new LanguageBean(0, LanguageNameUtils.getLanguageName(FileConfig.NOT_DOWNLOAD)
                    , "", "", false, false, false));
            for (String key : downloadLanguageBeans.keySet()) {
                LanguageBean languageBean = downloadLanguageBeans.get(key);
                languageBean.setPosition(languageBeans.size());
                languageBeans.add(languageBean);
            }
        }
    }

    /**
     * 切换时的数据更新
     *
     * @param languageBean
     * @return
     */
    public List<LanguageBean> updateData(LanguageBean languageBean) {
        int temp = 0;
        for (int i = 0; i < languageBeans.size(); i++) {
            if (languageBeans.get(i).getName().equals(languageBean.getName())) {
                temp = i;
            }
            languageBeans.get(i).setSelect(false);
        }
        if (temp != 1) {
            languageBeans.remove(temp);
            languageBean.setSelect(true);
            languageBeans.add(1, languageBean);
        }
        return languageBeans;
    }

    public List<LanguageBean> getDatas() {
        return languageBeans;
    }

    public void put(String key, LanguageBean value) {
        downloadLanguageBeans.put(key, value);
    }
}
