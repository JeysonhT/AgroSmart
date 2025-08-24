package com.example.agrosmart.presentation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosmart.R;
import com.example.agrosmart.domain.models.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{
    private Context context;
    private List<News> noticias;

    public NewsAdapter(Context context, List<News> noticias) {
        this.context = context;
        this.noticias = noticias;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notice_card, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News news = noticias.get(position);
        holder.imageView.setImageResource(news.getImageResource());
        holder.textView.setText(news.getDescription());
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    static class NewsHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public NewsHolder(@NonNull View view){
            super(view);

            imageView = view.findViewById(R.id.notice_imageView);
            textView = view.findViewById(R.id.noticeTitle);
        }
    }
}
