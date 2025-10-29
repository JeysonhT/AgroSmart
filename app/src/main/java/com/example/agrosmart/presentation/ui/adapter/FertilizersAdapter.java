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
import com.example.agrosmart.databinding.ItemFertilizerImageBinding;
import com.example.agrosmart.domain.models.Fertilizer;
import com.example.agrosmart.presentation.ui.fragment.subfragment.home.FertilizersFragmentDirections;

import java.util.List;

public class FertilizersAdapter extends RecyclerView.Adapter<FertilizersAdapter.FertilizerViewHolder> {

    private static final String TAG = "FERTILIZER_ADAPTER";
    private NavController controller;
    private List<Fertilizer> fertilizersData;

    public FertilizersAdapter(NavController _navController, List<Fertilizer> _data){
        this.controller = _navController;
        this.fertilizersData = _data;
    }

    public void updateData(List<Fertilizer> _data){
        this.fertilizersData.clear();
        this.fertilizersData.addAll(_data);
        notifyItemRangeChanged(0, _data.size());
    }

    @NonNull
    @Override
    public FertilizerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FertilizerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fertilizer_image,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FertilizerViewHolder holder, int position) {
        Fertilizer data = fertilizersData.get(position);

        holder.render(data, controller);
    }

    @Override
    public int getItemCount() {
        return this.fertilizersData.size();
    }

    static class FertilizerViewHolder extends RecyclerView.ViewHolder{
        ItemFertilizerImageBinding binding;

        private NavController controller;
        private Fertilizer currentData;
        public FertilizerViewHolder(@NonNull View view){
            super(view);

            binding = ItemFertilizerImageBinding.bind(view);

            binding.fertilizerCardButton.setOnClickListener(v -> {
                if(currentData != null && controller != null){
                    try{

                        NavDirections actions = FertilizersFragmentDirections.
                                actionFertilizersFragmentToFertilizerInfoFragment(
                                        currentData.getName(),
                                        currentData.getDescription(),
                                        currentData.getType(),
                                        currentData.getSupplier(),
                                        currentData.getApplicationMethod(),
                                        currentData.getRecommendedDose()
                                );

                        controller.navigate(actions);

                    } catch (NullPointerException e){
                        Log.e(TAG, String.format("Error al navegar: %s", e.getMessage()));
                    }
                }
            });
        }

        public void render(Fertilizer _data, NavController _controller){
            this.controller = _controller;
            this.currentData = _data;

            binding.fertilizerCardName.setText(_data.getName());
            binding.fertilizerCardImage.setImageResource(R.drawable.gemini_fertilizer_placeholder);
        }
    }

}
