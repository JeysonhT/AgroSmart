package com.example.agrosmart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosmart.R;
import com.example.agrosmart.designModels.ListViewModel;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder>{
    private Context context;
    private List<ListViewModel> listaModelo;

    public ListViewAdapter(Context context, List<ListViewModel> listaModelo){
        this.context = context;
        this.listaModelo = listaModelo;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_detection, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListViewModel listModel = listaModelo.get(position);

        holder.imageView.setImageResource(listModel.getIcono());
        holder.textView.setText(listModel.getTexto());
        holder.textView.setTextColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return listaModelo.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewDeficiencyList);
            imageView = itemView.findViewById(R.id.iconImageView);
        }
    }
}
