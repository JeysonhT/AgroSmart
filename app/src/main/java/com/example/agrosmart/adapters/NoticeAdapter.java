package com.example.agrosmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosmart.R;
import com.example.agrosmart.designModels.Notice;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeHolder>{
    private Context context;
    private List<Notice> noticias;

    public NoticeAdapter(Context context, List<Notice> noticias) {
        this.context = context;
        this.noticias = noticias;
    }

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notice_card, parent, false);
        return new NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {
        Notice notice = noticias.get(position);
        holder.imageView.setImageResource(notice.getImageResource());
        holder.textView.setText(notice.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    static class NoticeHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public NoticeHolder(@NonNull View view){
            super(view);

            imageView = view.findViewById(R.id.notice_imageView);
            textView = view.findViewById(R.id.noticeTitle);
        }
    }
}
