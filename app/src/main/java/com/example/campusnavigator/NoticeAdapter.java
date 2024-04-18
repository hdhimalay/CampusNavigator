package com.example.campusnavigator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private List<NoticeItem> noticeList;
    private OnNoticeClickListener listener;

    public NoticeAdapter(List<NoticeItem> noticeList, OnNoticeClickListener listener) {
        this.noticeList = noticeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new NoticeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        NoticeItem notice = noticeList.get(position);
        holder.bind(notice);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNoticeClick(notice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        private TextView noticeTitle;
        private TextView dateView;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTitle = itemView.findViewById(R.id.noticeTitle);
            dateView = itemView.findViewById(R.id.dateView);
        }

        public void bind(NoticeItem notice) {
            noticeTitle.setText(notice.getHeading());
            Log.d("NoticeAdapter", "Date fetched: " + notice.getDate());
            dateView.setText(notice.getDate());
        }
    }

    // Interface to handle clicks
    public interface OnNoticeClickListener {
        void onNoticeClick(NoticeItem notice);
    }
}
