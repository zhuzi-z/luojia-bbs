package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.bbs.History;
import com.wuda.bbs.ui.article.DetailArticleActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends FooterAdapter<History> {

    private AdapterItemListener<History> mItemListener;

    public HistoryAdapter(Context mContext, List<History> mContents) {
        super(mContext, mContents);
    }

    @Override
    protected ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent) {
        TextView name_tv = new TextView(parent.getContext());
        name_tv.setTextSize(18);
        name_tv.setPadding(16, 32, 16, 32);
        name_tv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        HistoryViewHolder holder = new HistoryViewHolder(name_tv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListener != null) {
                    int pos = holder.getAbsoluteAdapterPosition();
                    History history = mContents.get(pos);
                    mItemListener.onItemClick(history, pos);
                }
            }
        });

        return holder;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onBindContentViewHolder(ContentViewHolder holder, History content) {
        if (holder instanceof HistoryViewHolder) {

            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(content.getTitle());
            builder.append("\n");

            String time = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(content.getReadTime());
            String info = content.getAuthor() + " | " + time;
            builder.append(info, new StyleSpan(Typeface.ITALIC), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ((HistoryViewHolder) holder).history_tv.setText(builder);
        }
    }

    public void setItemListener(AdapterItemListener<History> itemListener) {
        this.mItemListener = itemListener;
    }

    static class HistoryViewHolder extends ContentViewHolder {
        TextView history_tv;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            history_tv = (TextView) itemView;
        }
    }
}
