package com.mul.download.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.bean
 * @ClassName: LanguageBean
 * @Author: zdd
 * @CreateDate: 2019/9/2 9:44
 * @Description: 已经有的语言项
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/2 9:44
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class LanguageBean implements Parcelable {
    private int code;
    private String name;
    private String fileName;
    private String fileSize;
    private boolean download;
    private boolean noDelete;
    private boolean isSelect;
    private float progress; // 下载进度
    private int position; // 所在所有列表的位置

    public LanguageBean() {
    }

    public LanguageBean(int code, String name, String fileName, String fileSize, boolean download, boolean noDelete, boolean isSelect) {
        this.code = code;
        this.name = name;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.download = download;
        this.noDelete = noDelete;
        this.isSelect = isSelect;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean isNoDelete() {
        return noDelete;
    }

    public void setNoDelete(boolean noDelete) {
        this.noDelete = noDelete;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.name);
        dest.writeString(this.fileName);
        dest.writeString(this.fileSize);
        dest.writeByte(this.download ? (byte) 1 : (byte) 0);
        dest.writeByte(this.noDelete ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.progress);
        dest.writeInt(this.position);
    }

    protected LanguageBean(Parcel in) {
        this.code = in.readInt();
        this.name = in.readString();
        this.fileName = in.readString();
        this.fileSize = in.readString();
        this.download = in.readByte() != 0;
        this.noDelete = in.readByte() != 0;
        this.isSelect = in.readByte() != 0;
        this.progress = in.readFloat();
        this.position = in.readInt();
    }

    public static final Creator<LanguageBean> CREATOR = new Creator<LanguageBean>() {
        @Override
        public LanguageBean createFromParcel(Parcel source) {
            return new LanguageBean(source);
        }

        @Override
        public LanguageBean[] newArray(int size) {
            return new LanguageBean[size];
        }
    };
}
