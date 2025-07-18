package com.example.agrosmart.presentation.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agrosmart.R;
import com.example.agrosmart.presentation.ui.adapter.ImageCarouselAdapter;
import com.example.agrosmart.presentation.ui.adapter.NoticeAdapter;
import com.example.agrosmart.domain.designModels.ImageCarouselData;
import com.example.agrosmart.domain.models.Notice;

import java.util.ArrayList;
import java.util.List;


public class Home_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView noticeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        List<ImageCarouselData> imageDataList = new ArrayList<>();
        imageDataList.add(new ImageCarouselData(R.drawable.imagen_1, "Planta de Maíz", "Maíz, el componente basico del desayuno nica"));
        imageDataList.add(new ImageCarouselData(R.drawable.sorgo, "Mata de Sorgo", "El sorgo es ideal para nuestras vacas"));
        imageDataList.add(new ImageCarouselData(R.drawable.frijol, "Frijoles", "El alimento lider de los nicaraguenses"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ImageCarouselAdapter(getContext(), imageDataList));

        noticeView = view.findViewById(R.id.noticeRecyclerView);

        List<Notice> noticeList = new ArrayList<>();
        noticeList.add(new Notice(R.drawable.frijoles,
                "Avanza siembra de frijol rojo en apante en Nicaragua durante el ciclo 2024/2025",
                "Nicaragua produce más frijoles, pero es casi imposible llevarlos a la mesa por elevados precios",
                "2024-04-04",
                "por aqui va la informacion"));
        noticeView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        noticeView.setAdapter(new NoticeAdapter(getContext(), noticeList));

        return view;
    }

}