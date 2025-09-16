package com.example.agrosmart.presentation.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.MutableObjectList;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.ItemHistoryDetectionBinding;
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView;
import com.example.agrosmart.presentation.ui.fragment.DetectionFragmentDirections;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class DiagnosisHistoryAdapter extends RecyclerView.Adapter<DiagnosisHistoryAdapter.ListViewHolder>{
    private MutableObjectList<DiagnosisHistoryListView> listaModelo;
    private NavController navController;
    Consumer<Integer> onDeleteListener;

    public DiagnosisHistoryAdapter(MutableObjectList<DiagnosisHistoryListView> listaModelo, NavController navController,
                                   Consumer<Integer> _onDeleteListener){
        this.listaModelo = listaModelo;
        this.navController = navController;
        this.onDeleteListener = _onDeleteListener;
    }

    public void updateData(List<DiagnosisHistoryListView> histories){
        listaModelo.clear();
        listaModelo.addAll(histories);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.item_history_detection, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        DiagnosisHistoryListView listModel = listaModelo.get(position);

        holder.render(listModel, navController, onDeleteListener, position);
    }

    @Override
    public int getItemCount() {
        return listaModelo.count();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryDetectionBinding binding;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHistoryDetectionBinding.bind(itemView);
        }

        public void render(DiagnosisHistoryListView listView, NavController navController,
                           Consumer<Integer> onDeleteListener, int index){
            binding.iconCropImage.setImageResource(listView.getCropIcon());
            binding.iconDeficiencyImage.setImageResource(listView.getDeficiencyIcon());
            binding.textDateDiagnosisHistory.setText(listView.getTxtDate());
            binding.textDateDiagnosisHistory.setTextColor(Color.BLACK);

            //modificar el listener a el item view completo
            binding.listCardView.setOnClickListener( v -> {
                try {
                    NavDirections action = DetectionFragmentDirections.
                            actionDetectionFragmentToDiagnosisInfoFragment(
                                    ImageCacheManager.saveImageToCache(
                                            binding.listCardView.getContext(),
                                            listView.getImage()),
                                    listView.getDeficiency().split(" ")[0],
                                    listView.getTxtDate(),
                                    listView.getDeficiency(),
                                    listView.getRecommendation()
                            );

                    navController.navigate(action);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            binding.imvBotonBorrar.setOnClickListener( v -> {
                onDeleteListener.accept(index);
            });

        }

        //crear el listener del fututo boton de borrar

    }
}
