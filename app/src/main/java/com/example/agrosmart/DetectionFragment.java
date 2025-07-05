package com.example.agrosmart;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.agrosmart.adapters.ImageCarouselAdapter;
import com.example.agrosmart.adapters.ListViewAdapter;
import com.example.agrosmart.designModels.ImageCarouselData;
import com.example.agrosmart.designModels.ListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class
DetectionFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private RecyclerView recyclerView;
    private RecyclerView listView;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int IMAGE_CAPTURE_CODE = 101;

    private FloatingActionButton fabCamera;

    private Uri photoUri;
    private File photoFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detection, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDetectionImage);

        List<ImageCarouselData> imageDataList = new ArrayList<>();
        imageDataList.add(new ImageCarouselData(R.drawable.imagen_2, "Deficiencia de potasio", "Maíz con deficiencia de potasio"));
        imageDataList.add(new ImageCarouselData(R.drawable.imagen_3, "Deficiencia de magnesio", "Sorgo con deficiencia de magnesio"));
        imageDataList.add(new ImageCarouselData(R.drawable.imagen_4, "Deficiencia de zinc", "Maíz con deficiencia de zinc"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ImageCarouselAdapter(getContext(), imageDataList));

        listView = view.findViewById(R.id.recyclerViewDetectionList);

        List<ListViewModel> listModel = new ArrayList<>();

        listModel.add(new ListViewModel(R.drawable.cactus_24, "Nitrogeno"));
        listModel.add(new ListViewModel(R.drawable.cactus_24, "Fosforo"));
        listModel.add(new ListViewModel(R.drawable.cactus_24, "Magnesio"));

        listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(new ListViewAdapter(getContext(), listModel));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabCamera = view.findViewById(R.id.fabCamera);

        fabCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            } else {
                abrirCamara();
            }
        });
    }

    public void abrirCamara() {
        NavDirections action = DetectionFragmentDirections.actionDetectionFragmentToCameraLayout2();

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(action);
    }

    private File crearArchivoImagen() throws IOException {
        String nombreArchivo = "IMG_" + System.currentTimeMillis();
        File directorio = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(nombreArchivo, ".jpg", directorio);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(getContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (photoFile != null && photoFile.exists()) {
                Toast.makeText(getContext(), "Imagen guardada en:\n" + photoFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                System.out.println("Imagen guardada en:\n" + photoFile.getAbsolutePath());
            } else {
                Toast.makeText(getContext(), "Error: archivo no encontrado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // Solo navega si NO estamos ya en profileFragment
            if (Objects.requireNonNull(NavHostFragment.findNavController(this).getCurrentDestination()).getId() == R.id.detectionFragment) {
                NavHostFragment.findNavController(this).navigate(R.id.profileFragment);
            }
        }
    }
}