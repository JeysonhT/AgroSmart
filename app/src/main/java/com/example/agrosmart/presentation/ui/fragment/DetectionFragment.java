package com.example.agrosmart.presentation.ui.fragment;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.MutableObjectList;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrosmart.R;
import com.example.agrosmart.databinding.FragmentDetectionBinding;
import com.example.agrosmart.presentation.ui.adapter.DiagnosisHistoryAdapter;
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView;
import com.example.agrosmart.presentation.viewmodels.DetectionFragmentViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class
DetectionFragment extends Fragment {

    private DetectionFragmentViewModel dfViewModel ;

    private static final int CAMERA_REQUEST_CODE = 100;

    private FragmentDetectionBinding binding;

    private NavController navController;

    private MutableObjectList<DiagnosisHistoryListView> histories;

    private DiagnosisHistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dfViewModel = new ViewModelProvider(this).get(DetectionFragmentViewModel.class);

        navController = NavHostFragment.findNavController(this);

        histories = new MutableObjectList<>();

        dfViewModel.gethistoriesFromUseCase();

        loadHistory();
        setDiagnosisCard();

        binding.fabCamera.setOnClickListener(v -> {
            fabCameraListener();
        });

        //usar network checker para verificar que hay internet para realizar las recomendaciones
        getParentFragmentManager().setFragmentResultListener(
                "resultado_camara", this,
                (requestKey, bundle) -> {
                    String texto = bundle.getString("resultado");
                    mostrarDialogo("Resultado detección", texto);
                    if(texto!=null){
                        dfViewModel.obtenerRecomendacion(texto);
                    }
                }
        );

        //obtener la respuesta mediante el observador(recommendationResponse) del viewmodel
        dfViewModel.getRecommendationResponse().observe(getViewLifecycleOwner(), respuesta -> {
            if(respuesta!=null){
                mostrarDialogo("Recomendación", respuesta.getRespuesta());
                dfViewModel.cleanRecommendation();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadHistory(){
        //llenado del historial 1. adaptador, 2. layoutManager, 3. datos del observador
        adapter = new DiagnosisHistoryAdapter(
                histories,
                navController, this::onDeleteListener
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        binding.recyclerViewDetectionList.
                setLayoutManager(layoutManager);

        binding.recyclerViewDetectionList.setAdapter(adapter);

        dfViewModel.getHistory().observe(getViewLifecycleOwner(), histories -> {
            if(histories != null && !histories.isEmpty()){
                adapter.updateData(histories);
                layoutManager.scrollToPositionWithOffset(0,20);
            }
        });
    }

    private void onDeleteListener(int index){
        String _id = histories.get(index).getId();
        dfViewModel.deleteHistory(_id);
        histories.removeAt(index);
        adapter.notifyItemChanged(index);
    }


    //llena la card del ultimo diagnostico
    private void setDiagnosisCard(){
        //llenar la card del ultimo diagnostico en caso de que haya uno
        dfViewModel.getLastDiagnosis().observe(getViewLifecycleOwner(), diagnosis -> {
            if(diagnosis != null){
                binding.cropImageView.setImageResource(diagnosis.getCropIcon());
                binding.deficiencyImageView.setImageResource(diagnosis.getDeficiencyIcon());

                binding.textDateDiagnosis.setText(diagnosis.getTxtDate());
                binding.textDiagnosis.setText(diagnosis.getDeficiency());
            } else {
                binding.textDiagnosisCard.setText(R.string.lastDeficiencyCardTextInNullCase);
            }
        });
    }

    private void mostrarDialogo(String title, String message) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //listener del boton flotante de la camara
    public void fabCameraListener(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    public void openCamera() {
        NavDirections action = DetectionFragmentDirections.actionDetectionFragmentToCameraLayout2();

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(action);
    }

    //sin uso por el momento
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
    public void onStart() {
        super.onStart();

        dfViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                if ((NavHostFragment.findNavController(this).getCurrentDestination() != null
                        && NavHostFragment.findNavController(this).getCurrentDestination().getId() == R.id.detectionFragment)) {
                    NavHostFragment.findNavController(this).navigate(R.id.profileFragment);
                }
            }
        });
    }
}