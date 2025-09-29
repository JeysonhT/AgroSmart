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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.core.utils.classes.NetworkChecker;
import com.example.agrosmart.databinding.FragmentDetectionBinding;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.presentation.ui.adapter.DiagnosisHistoryAdapter;
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView;
import com.example.agrosmart.presentation.viewmodels.DetectionFragmentViewModel;
import com.example.agrosmart.presentation.viewmodels.ProfileViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class
DetectionFragment extends Fragment {

    private final String TAG = "DETECTION_FRAGMENT";

    private DetectionFragmentViewModel dfViewModel ;

    private ProfileViewModel profileViewModel;

    private static final int CAMERA_REQUEST_CODE = 100;

    private FragmentDetectionBinding binding;

    private NavController navController;

    private DiagnosisHistoryAdapter adapter;

    private DiagnosisHistoryListView lastDiagnosis;

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

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        navController = NavHostFragment.findNavController(this);

        dfViewModel.gethistoriesFromUseCase();

        loadHistory();
        setDiagnosisCard();

        binding.fabCamera.setOnClickListener(v -> {
            fabCameraListener();
        });

        Bundle bundle = getArguments();

        if(bundle != null && !bundle.isEmpty()){
            String resultado = bundle.getString("result");

            byte[] imgBytes = ImageCacheManager.getArrayFromFile(requireContext(),
                    bundle.getString("imgPath"));

            dfViewModel.saveDiagnosis(resultado, imgBytes, this::setDiagnosisData);

            ImageCacheManager.cleanupCache(requireContext());

            if(NetworkChecker.isInternetAvailable(requireContext())){
                dfViewModel.obtenerRecomendacion(resultado);
            }

            bundle.clear();
        }


        //obtener la respuesta mediante el observador(recommendationResponse) del viewmodel
        dfViewModel.getRecommendationResponse().observe(getViewLifecycleOwner(), respuesta -> {
            if(respuesta!=null){

                if(lastDiagnosis != null){
                    String _id = lastDiagnosis.getId();

                    Log.println(Log.DEBUG, TAG, "Valor obtenido: " + lastDiagnosis.getTxtDate());

                    try{
                        dfViewModel.updateDiagnosis(_id, respuesta.getRespuesta());
                    } catch (Exception e){
                        Log.w(TAG, "Error al actualizar: " + e.getMessage());
                    }
                }

                mostrarDialogo("Recomendación", respuesta.getRespuesta());

                Log.println(Log.DEBUG, TAG, "msg: " + respuesta.getRespuesta());

                dfViewModel.cleanRecommendation();
            }
        });
    }

    public void setDiagnosisData(DiagnosisHistoryListView listView){
        dfViewModel.addNewHistory(listView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadHistory(){
        //llenado del historial 1. adaptador, 2. layoutManager, 3. datos del observador
        adapter = new DiagnosisHistoryAdapter(
                new ArrayList<>(),
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
            }
        });
    }

    private void onDeleteListener(String _id){
        adapter.removeItemById(_id);

        dfViewModel.deleteHistory(_id);
    }


    //llena la card del ultimo diagnostico
    private void setDiagnosisCard(){
        //llenar la card del ultimo diagnostico en caso de que haya uno
        dfViewModel.getLastDiagnosis().observe(getViewLifecycleOwner(), diagnosis -> {
            if(diagnosis != null){

                lastDiagnosis = diagnosis;

                binding.cropImageView.setImageResource(diagnosis.getCropIcon());
                binding.deficiencyImageView.setImageResource(diagnosis.getDeficiencyIcon());

                binding.textDateDiagnosis.setText(diagnosis.getTxtDate());
                binding.textDiagnosis.setText(diagnosis.getDeficiency());

                binding.lastDiagnosisCard.setOnClickListener( v -> {
                    try{
                        NavDirections action = DetectionFragmentDirections.
                                actionDetectionFragmentToDiagnosisInfoFragment(
                                        ImageCacheManager.saveImageToCache(getContext(), diagnosis.getImage()),
                                        diagnosis.getDeficiency().split(" ")[0],
                                        diagnosis.getTxtDate(),
                                        diagnosis.getDeficiency(),
                                        diagnosis.getRecommendation()
                                );

                        navController.navigate(action);
                    } catch(IOException e){
                        Log.e(TAG, "Error: " + e.getMessage());
                    }
                });

            } else {
                binding.textDiagnosisCard.setText(R.string.lastDeficiencyCardTextInNullCase);
            }
        });
    }

    private void mostrarDialogo(String title, String message) {
        new MaterialAlertDialogBuilder(requireContext())
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
    public void onResume() {
        super.onResume();
        dfViewModel.refreshData();
    }

    @Override
    public void onStart() {
        super.onStart();

        profileViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                //if ((NavHostFragment.findNavController(this).getCurrentDestination() != null)) {
                    NavHostFragment.findNavController(this).navigate(R.id.profileFragment);
                //}
            }
        });
    }
}