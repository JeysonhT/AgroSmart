package com.example.agrosmart.presentation.ui.adapter;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.ItemDeficienciesImageBinding;
import com.example.agrosmart.domain.models.Deficiency;
import com.example.agrosmart.presentation.ui.fragment.subfragment.home.DeficienciesFragmentDirections;

import java.util.List;

public class DeficienciesAdapter extends RecyclerView.Adapter<DeficienciesAdapter.DeficiencyViewHolder> {

    private static final String TAG = "DEFICIENCIES_ADAPTER";
    private NavController controller;
    private List<Deficiency> deficiencyList;

    public DeficienciesAdapter(NavController _controller, List<Deficiency> _data){
        this.controller = _controller;
        this.deficiencyList = _data;
    }

    public void updateData(List<Deficiency> _data){
        this.deficiencyList.clear();
        this.deficiencyList.addAll(_data);
        notifyItemRangeChanged(0, _data.size());
    }

    @NonNull
    @Override
    public DeficiencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeficiencyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deficiencies_image,
                        parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DeficiencyViewHolder holder, int position) {
        Deficiency data = deficiencyList.get(position);

        holder.render(controller, data);
    }

    @Override
    public int getItemCount() {
        return this.deficiencyList.size();
    }

    static class DeficiencyViewHolder extends RecyclerView.ViewHolder {

        ItemDeficienciesImageBinding binding;

        private NavController controller;
        private Deficiency currentData;
        public DeficiencyViewHolder(@NonNull View view){
            super(view);
            binding = ItemDeficienciesImageBinding.bind(view);

            binding.deficiencyCardButton.setOnClickListener(v -> {
                if(currentData!=null && controller != null){

                    try{

                        NavDirections action = DeficienciesFragmentDirections.
                                actionDeficienciesFragmentToDefiencyInfoFragment(
                                    currentData.getName(),
                                        currentData.getDescription(),
                                        currentData.getSymptoms(),
                                        currentData.getSolutions()
                                );

                        controller.navigate(action);
                    } catch(NullPointerException e){
                        Log.e(TAG, String.
                                format("Error al navegar al elemento seleccionado: %s", e.getMessage()));
                    }
                }
            });
        }

        public void render(NavController _controller, Deficiency _data){
            this.controller = _controller;
            this.currentData = _data;

            if(_data.getImageResource().length == 0){
                binding.deficiencyCardImage.setImageResource(R.drawable.gemini_deficiendy_image);
            } else {
                binding.deficiencyCardImage.setImageBitmap(BitmapFactory.decodeByteArray(
                        _data.getImageResource(),
                        0,
                        _data.getImageResource().length));
            }
            binding.defiencyCardTitle.setText(_data.getName());
        }
    }
}
