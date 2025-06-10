package com.example.agrosmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agrosmart.R;
import com.example.agrosmart.designModels.ImageCarouselData;

import java.util.List;

public class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.ImageViewHolder> {

    private Context context;
    private List<ImageCarouselData> imageDataList;

    public ImageCarouselAdapter(Context context, List<ImageCarouselData> imageDataList){
        this.context = context;
        this.imageDataList = imageDataList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageCarouselData imageCarouselData = imageDataList.get(position);
        holder.imageView.setImageResource(imageCarouselData.getImageResource());
        holder.textImageTitle.setText(imageCarouselData.getTitle());
        holder.textImageDescription.setText(imageCarouselData.getDescription());
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textImageTitle;
        TextView textImageDescription;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textImageTitle = itemView.findViewById(R.id.textImageTitle);
            textImageDescription = itemView.findViewById(R.id.textImageDescription);
        }
    }
}
