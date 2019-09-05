package com.mul.download.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.mul.download.app.DownloadApp;

/**
 * @ProjectName: X6l_Offline
 * @Package: com.gykj.offline.util
 * @ClassName: SpUtil
 * @Author: zdd
 * @CreateDate: 2019/7/27 8:53
 * @Description: 保存数据的工具类
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/27 8:53
 * @UpdateRemark: 更新说明
 * @Version: 版本号
 */
public class SpUtil {
    public static final String name = "X6L_SP";
    private SharedPreferences sp;

    private SpUtil() {
        sp = DownloadApp.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private static class SpUtilHolder {
        private static final SpUtil SP_UTIL = new SpUtil();
    }

    public static SpUtil getInstance() {
        return SpUtilHolder.SP_UTIL;
    }

    /**
     * 保存数据，修改数据
     *
     * @param key
     * @param value
     * @param <V>
     */
    public <V> void putValue(@NonNull String key, V value) {
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.commit();
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @param <V>
     * @return
     */
    public <V> V getValue(@NonNull String key, V defaultValue) {
        Object value = defaultValue;
        if (defaultValue instanceof String) {
            value = sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            value = sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            value = sp.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            value = sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            value = sp.getFloat(key, (Float) defaultValue);
        }
        return (V) value;
    }

    /**
     * 清空数据
     */
    public void clearData() {
        SharedPreferences.Editor editor = DownloadApp.getInstance().
                getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
