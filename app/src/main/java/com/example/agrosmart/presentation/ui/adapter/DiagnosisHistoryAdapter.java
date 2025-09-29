package com.example.agrosmart.presentation.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.ItemHistoryDetectionBinding;
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView;
import com.example.agrosmart.presentation.ui.fragment.DetectionFragmentDirections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DiagnosisHistoryAdapter extends RecyclerView.Adapter<DiagnosisHistoryAdapter.ListViewHolder>{
    private List<DiagnosisHistoryListView> listaModelo;
    private NavController navController;
    Consumer<String> onDeleteListener;

    public DiagnosisHistoryAdapter(List<DiagnosisHistoryListView> listaModelo, NavController navController,
                                   Consumer<String> _onDeleteListener){
        this.listaModelo = listaModelo != null ? new ArrayList<>(listaModelo) : new ArrayList<>();
        this.navController = navController;
        this.onDeleteListener = _onDeleteListener;
    }

    public void updateData(List<DiagnosisHistoryListView> histories){
        int oldSize = listaModelo.size();

        listaModelo.clear();
        if (oldSize > 0) {
            notifyItemRangeRemoved(0, oldSize);
        }

        if (histories != null) {
            listaModelo.addAll(histories);
            notifyItemRangeInserted(0, histories.size());
        }
    }

    public void removeItemById(String id) {
        for (int i = 0; i < listaModelo.size(); i++) {
            if (listaModelo.get(i).getId().equals(id)) {
                listaModelo.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_detection, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        if (position < listaModelo.size()) {
            DiagnosisHistoryListView listModel = listaModelo.get(position);
            holder.render(listModel, navController, onDeleteListener);
        }
    }

    @Override
    public int getItemCount() {
        return listaModelo.size();
    }

    @Override
    public void onViewRecycled(@NonNull ListViewHolder holder) {
        super.onViewRecycled(holder);

         holder.onDeleteListener = null;
         holder.currentListView = null;
         holder.navController = null;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        ItemHistoryDetectionBinding binding;
        private DiagnosisHistoryListView currentListView;
        private NavController navController;
        private Consumer<String> onDeleteListener;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHistoryDetectionBinding.bind(itemView);

            binding.imvBotonBorrar.setOnClickListener( v -> {
                if(onDeleteListener != null && currentListView != null){
                    onDeleteListener.accept(currentListView.getId());
                }
            });

            binding.listCardView.setOnClickListener(v -> {
                if (currentListView != null && navController != null) {
                    try {
                        NavDirections action = DetectionFragmentDirections
                                .actionDetectionFragmentToDiagnosisInfoFragment(
                                        ImageCacheManager.saveImageToCache(
                                                binding.listCardView.getContext(),
                                                currentListView.getImage()),
                                        currentListView.getDeficiency().split(" ")[0],
                                        currentListView.getTxtDate(),
                                        currentListView.getDeficiency(),
                                        currentListView.getRecommendation()
                                );
                        navController.navigate(action);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void render(DiagnosisHistoryListView _listView, NavController _navController,
                           Consumer<String> _onDeleteListener){
            if (_listView == null) return;

            this.currentListView = _listView;
            this.navController = _navController;
            this.onDeleteListener = _onDeleteListener;

            binding.iconCropImage.setImageResource(_listView.getCropIcon());
            binding.iconDeficiencyImage.setImageResource(_listView.getDeficiencyIcon());
            binding.textDateDiagnosisHistory.setText(_listView.getTxtDate());
            binding.textDateDiagnosisHistory.setTextColor(Color.BLACK);

        }

    }
}
