package com.example.agrosmart.presentation.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.presentation.ui.fragment.Home_FragmentDirections;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{
    private Context context;
    private List<News> noticias;
    private NavController navController;

    public NewsAdapter(Context context, List<News> noticias, NavController navController) {
        this.context = context;
        this.noticias = noticias;
        this.navController = navController;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<News> newsData){
        this.noticias.clear();
        this.noticias.addAll(newsData);
        notifyDataSetChanged();
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
        holder.imageView.setImageBitmap(BitmapFactory.
                decodeByteArray(news.getImage(), 0, news.getImage().length));
        holder.textView.setText(news.getDescription());

        holder.itemView.setOnClickListener(v -> {
            try{
                NavDirections action = Home_FragmentDirections.actionHomeFragmentToNewsInfoFragment(
                        (news.get_id()!=null) ? news.get_id() : "",
                        ImageCacheManager.saveImageToCache(v.getContext(), news.getImage()),
                        news.getNewsName(),
                        news.getDescription(),
                        news.getPublicationDate(),
                        news.getAuthor(),
                        news.getInformation(),
                        news.isLocal()
                );
                navController.navigate(action);
            } catch (IllegalStateException | IOException e){
                Log.println(Log.ERROR, "NEWS_ADAPTER", Objects.requireNonNull(e.getMessage()));
            }
        });
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
