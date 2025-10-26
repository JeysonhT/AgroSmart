package com.example.agrosmart.presentation.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosmart.R;
import com.example.agrosmart.databinding.ItemCropImageBinding;
import com.example.agrosmart.domain.designModels.CropCarouselData;
import com.example.agrosmart.presentation.ui.fragment.Home_FragmentDirections;

import java.util.List;
import java.util.Objects;

public class CropInfoAdapter extends RecyclerView.Adapter<CropInfoAdapter.ImageViewHolder> {
    private static final String TAG = "CROP_INFO_ADAPTER";

    private Context context;
    private List<CropCarouselData> cropDataList;
    private NavController controller;

    public CropInfoAdapter(Context context, List<CropCarouselData> cropDataList, NavController _controller){
        this.context = context;
        this.cropDataList = cropDataList;
        this.controller = _controller;
    }

    public void updateData(List<CropCarouselData> data){
        this.cropDataList.clear();
        this.cropDataList.addAll(data);
        notifyDataSetChanged(); // este ya es mi ultimo recurso
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crop_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        CropCarouselData cropCarouselData = cropDataList.get(position);

        holder.render(cropCarouselData, controller);
    }

    @Override
    public int getItemCount() {
        return cropDataList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ItemCropImageBinding binding;
        private NavController navController;
        private CropCarouselData currentData;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemCropImageBinding.bind(itemView);

            binding.imageView.setOnClickListener(v -> {
                if(currentData != null && navController != null){
                    try{

                        NavDirections action = Home_FragmentDirections.
                                actionHomeFragmentToCropInfoFragment(
                                        currentData.getImageResource(),
                                        currentData.getTitle(),
                                        currentData.getDescription(),
                                        currentData.getHarvestTime(),
                                        currentData.getType()
                                );

                        navController.navigate(action);

                    } catch(NullPointerException e){
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    }
                }
            });
        }

        public void render(CropCarouselData data, NavController controller){

            this.navController = controller;
            this.currentData = data;

            binding.imageView.setImageResource(data.getImageResource());
            binding.textImageTitle.setText(data.getTitle());
            binding.textImageDescription.setText(data.getDescription());
        }
    }
}
