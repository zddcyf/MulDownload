package com.mul.download.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mul.download.R;
import com.mul.download.bean.LanguageBean;
import com.mul.download.config.FileConfig;
import com.mul.download.download.weight.ProgressView;

import java.util.List;

/**
 * @ProjectName: TO_Text
 * @Package: com.iguan.text
 * @ClassName: LanguageDownloadAdapter
 * @Author: zdd
 * @CreateDate: 2019/9/2 12:46
 * @Description: 类的作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/2 12:46
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class LanguageDownloadAdapter extends RecyclerView.Adapter<LanguageDownloadAdapter.RvViewHolder> {
    private static int ISEMPTY = 100;
    private static int TITLE = 101;

    private List<LanguageBean> languageSparse;
    private Context mContext;
    private ItemClick itemClick;

    public void setDatas(List<LanguageBean> languageSparse) {
        this.languageSparse = languageSparse;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == mContext) {
            mContext = parent.getContext();
        }
        View view = null;
        if (viewType == ISEMPTY) {
            view = View.inflate(mContext, R.layout.adapter_language_download_isempty, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
        } else if (viewType == TITLE) {
            view = View.inflate(mContext, R.layout.adapter_language_download_title, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        } else {
            view = View.inflate(mContext, R.layout.adapter_language_download_content, null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-1, -2);
            layoutParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_10);
            layoutParams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_6);
            layoutParams.rightMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_6);
            view.setLayoutParams(layoutParams);
        }
        return new RvViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ISEMPTY) {
        } else if (viewType == TITLE) {
            holder.title.setText(languageSparse.get(position).getName());
        } else {
            final LanguageBean languageBean = languageSparse.get(position);
            holder.content.setText(languageBean.getName());
            holder.fileStatus.setText(languageBean.getFileSize());
            if (!languageBean.isDownload()) {
                holder.languageSelect.setVisibility(View.VISIBLE);
                holder.progressView.setVisibility(View.GONE);
                holder.download.setVisibility(View.GONE);
                holder.languageSelect.setSelected(languageBean.isSelect());
            } else {
                holder.languageSelect.setVisibility(View.GONE);
                if (languageBean.getProgress() <= 0) {
                    holder.progressView.setVisibility(View.INVISIBLE);
                    holder.download.setVisibility(View.VISIBLE);
                } else {
                    holder.download.setVisibility(View.INVISIBLE);
                    holder.progressView.setVisibility(View.VISIBLE);
                    holder.progressView.setProgress(languageBean.getProgress());
                }
            }
            holder.contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != itemClick) {
                        if (languageBean.isDownload()) { // 需要下载的
                            itemClick.itemDownloadClick(languageBean);
                        } else {
                            itemClick.itemSwitchClick(languageBean);
                        }
                    }
                }
            });
            holder.contentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (languageBean.isNoDelete() && !languageBean.isDownload() && null != itemClick) {
                        itemClick.itemLongClick(languageBean);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null != languageSparse && languageSparse.size() > 0 ? languageSparse.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if ((null == languageSparse || languageSparse.size() <= 0) && position == 0) {
            return ISEMPTY;
        }
        if (FileConfig.DOWNLOADED.equals(languageSparse.get(position).getName())
                || FileConfig.NOT_DOWNLOAD.equals(languageSparse.get(position).getName())) {
            return TITLE;
        }
        return super.getItemViewType(position);
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout contentLayout;
        private AppCompatTextView title, content, fileStatus;
        private AppCompatImageView languageSelect, download;
        private ProgressView progressView;

        public RvViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == ISEMPTY) {
            } else if (viewType == TITLE) {
                title = itemView.findViewById(R.id.title);
            } else {
                contentLayout = itemView.findViewById(R.id.contentLayout);
                content = itemView.findViewById(R.id.content);
                fileStatus = itemView.findViewById(R.id.fileStatus);
                languageSelect = itemView.findViewById(R.id.languageSelect);
                progressView = itemView.findViewById(R.id.progressView);
                download = itemView.findViewById(R.id.download);
            }
        }
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public interface ItemClick {
        void itemSwitchClick(LanguageBean languageBean);

        void itemDownloadClick(LanguageBean languageBean);

        void itemLongClick(LanguageBean languageBean);
    }
}
