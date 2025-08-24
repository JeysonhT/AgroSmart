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
import com.example.agrosmart.domain.designModels.CropCarouselData;

import java.util.List;

public class CropInfoAdapter extends RecyclerView.Adapter<CropInfoAdapter.ImageViewHolder> {

    private Context context;
    private List<CropCarouselData> cropDataList;

    public CropInfoAdapter(Context context, List<CropCarouselData> cropDataList){
        this.context = context;
        this.cropDataList = cropDataList;
    }

    public void updateData(List<CropCarouselData> data){
        this.cropDataList.clear();
        this.cropDataList.addAll(data);
        notifyDataSetChanged(); // este ya es mi ultimo recurso
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        CropCarouselData cropCarouselData = cropDataList.get(position);
        holder.imageView.setImageResource(cropCarouselData.getImageResource());
        holder.textImageTitle.setText(cropCarouselData.getTitle());
        holder.textImageDescription.setText(cropCarouselData.getDescription());
    }

    @Override
    public int getItemCount() {
        return cropDataList.size();
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
