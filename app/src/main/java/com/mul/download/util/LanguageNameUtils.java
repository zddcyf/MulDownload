package com.mul.download.util;

import com.mul.download.config.FileConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.util
 * @ClassName: LanguageNameUtils
 * @Author: zdd
 * @CreateDate: 2019/9/4 16:19
 * @Description: 类的作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/4 16:19
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class LanguageNameUtils {
    private static Map<String, String> languageNameUtils = new HashMap();

    static {
        languageNameUtils.put(FileConfig.DOWNLOADED, "已下载的语音");
        languageNameUtils.put(FileConfig.NOT_DOWNLOAD, "未下载");

        languageNameUtils.put(FileConfig.CMN_HANS_CN, "中文-简体");
//        languageNameUtils.put(FileConfig.CMN_HANT_TW, "中文-繁体");

        languageNameUtils.put(FileConfig.EN_US, "英语-美国");

        languageNameUtils.put(FileConfig.JA_JP, "日语");
        languageNameUtils.put(FileConfig.KO_KR, "韩语");
        languageNameUtils.put(FileConfig.ES_US, "西班牙语-美国");

        /**
         * 动态添加
         */
        languageNameUtils.put(FileConfig.DE, "德语");
        languageNameUtils.put(FileConfig.AR, "阿拉伯语");
    }

    public static String getLanguageName(String key) {
        return languageNameUtils.get(key);
    }
}
