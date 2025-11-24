package com.example.agrosmart.presentation.ui.fragment.subfragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.FragmentNewsInfoBinding;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.NewsUseCase;
import com.example.agrosmart.presentation.viewmodels.HomeViewModel;
import com.example.agrosmart.presentation.viewmodels.factory.HomeViewModelFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NewsInfoFragment extends Fragment {

    private final String TAG = "NEWS_INFO_FRAGMENT";

    private final int SAVE_CODE = 1;

    private final int DELETE_CODE = 0;

    private FragmentNewsInfoBinding binding;

    private HomeViewModel viewModel;

    private NavController controller;

    public NewsInfoFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNewsInfoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CropsUseCase cropsUseCase = new CropsUseCase();
        NewsUseCase newsUseCase = new NewsUseCase();
        HomeViewModelFactory factory = new HomeViewModelFactory(newsUseCase, cropsUseCase);
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        controller = Navigation.findNavController(view);

        viewModel.getSaveNewResult().observe(getViewLifecycleOwner(), isSave -> {
            if(isSave){
                mostrarDialogo("Mensaje de confirmación",
                        "La noticia fue guardada con exito", SAVE_CODE);
            } else {
                mostrarDialogo("Mensaje de error", "No se pudo guardar la noticia, \nintente luego!!", SAVE_CODE);
            }
        });

        viewModel.getDeletedNewResult().observe(getViewLifecycleOwner(), isDeleted -> {
            if(isDeleted){
                mostrarDialogo("Eliminación", "La noticia fue eliminada de su almacenamiento", DELETE_CODE);
            }
        });

        Bundle bundle = getArguments();

        if(bundle!=null){
            String _id = bundle.getString("_id");
            Bitmap image = ImageCacheManager.loadImageFromCache(getContext(),
                    bundle.getString("image"));
            String author = bundle.getString("author");
            String title = bundle.getString("title");
            String description = bundle.getString("description");
            String publicationDate = bundle.getString("publicationDate");
            String content = bundle.getString("content");
            boolean isLocal = bundle.getBoolean("isLocal");

            try{
                binding.newImage.
                        setImageBitmap(image);
                binding.newTitle.setText(title);
                binding.newDate.setText(publicationDate);
                binding.newAuthor.setText(author);
                binding.newContent.setText(content);

                binding.saveNewsButton.setOnClickListener( v -> {
                    viewModel.saveNewOnLocal(new News(
                            ImageCacheManager.
                                    getArrayFromFile(requireContext(),
                                            bundle.getString("image")),
                            author,
                            title,
                            description,
                            publicationDate,
                            content
                    ));

                });

                if(isLocal){
                    binding.saveNewsButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mid_orange));
                    binding.saveNewsButton.setText("Eliminar Noticia");

                    binding.saveNewsButton.setOnClickListener(v->{
                        viewModel.deleteFromLocal(_id);
                    });
                }

            } catch(NullPointerException e){
                Log.println(Log.ERROR, TAG, "Error: " + e.getMessage());
            }

        }
    }

    private void mostrarDialogo(String title, String message, int code) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    dialog.dismiss();
                    if(code == DELETE_CODE){
                        controller.navigateUp();
                    }
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ImageCacheManager.cleanupCache(getContext());
    }
}