package com.example.agrosmart.presentation.ui.fragment;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ClickMode;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.core.utils.classes.LoaderDialog;
import com.example.agrosmart.core.utils.classes.NetworkChecker;
import com.example.agrosmart.databinding.FragmentDetectionBinding;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.presentation.ui.adapter.DiagnosisHistoryAdapter;
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView;
import com.example.agrosmart.presentation.viewmodels.DetectionFragmentViewModel;
import com.example.agrosmart.presentation.viewmodels.ProfileViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private LoaderDialog loaderDialog;

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

        loaderDialog = new LoaderDialog(requireContext());

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
                showLoader();
            }

            bundle.clear();
        }


        //obtener la respuesta mediante el observador(recommendationResponse) del viewmodel
        dfViewModel.getRecommendationResponse().observe(getViewLifecycleOwner(), respuesta -> {
            if(respuesta!=null){

                if(respuesta.getRespuesta().equals("error")){
                    hideLoader();
                    mostrarDialogo("Error",
                            "Error de conexión intente más tarde\nGenere la recomendación dentro del diagnostico mas reciente",
                            ClickMode.DISMISS);
                    dfViewModel.cleanRecommendation();
                    return;
                }

                if(lastDiagnosis != null){
                    String _id = lastDiagnosis.getId();

                    try{
                        adapter.updateItemById(_id, respuesta.getRespuesta());
                        dfViewModel.saveRecommendationInDiagnosis(_id, respuesta.getRespuesta());
                    } catch (Exception e){
                        Log.w(TAG, "Error al actualizar: " + e.getMessage());
                    }
                }

                hideLoader();
                mostrarDialogo("Exito",
                        "Ingrese al ultimo diagnostico para ver su recomendación",
                        ClickMode.NAVIGATE);
                dfViewModel.cleanRecommendation();
            }
        });
    }

    public void setDiagnosisData(DiagnosisHistory diagnosisHistory){
        dfViewModel.addNewHistory(diagnosisHistory);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        hideLoader();
        super.onDestroy();
    }

    private void loadHistory(){
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
                List<DiagnosisHistoryListView> historyListViews = new ArrayList<>();
                for (DiagnosisHistory history : histories) {
                    historyListViews.add(DiagnosisToHistoryLV(history));
                }
                adapter.updateData(historyListViews);
            }
        });
    }

    private void onDeleteListener(String _id){
        adapter.removeItemById(_id);

        dfViewModel.deleteHistory(_id);
    }

    public void navigateToLastDiagnosis(){

    }


    private void setDiagnosisCard(){
        dfViewModel.getLastDiagnosis().observe(getViewLifecycleOwner(), diagnosis -> {
            if(diagnosis != null){

                lastDiagnosis = DiagnosisToHistoryLV(diagnosis);

                binding.cropImageView.setImageResource(lastDiagnosis.getCropIcon());
                binding.deficiencyImageView.setImageResource(lastDiagnosis.getDeficiencyIcon());

                binding.textDateDiagnosis.setText(lastDiagnosis.getTxtDate());
                binding.textDiagnosis.setText(lastDiagnosis.getDeficiency());

                binding.lastDiagnosisCard.setOnClickListener( v -> {
                    try{
                        NavDirections action = DetectionFragmentDirections.
                                actionDetectionFragmentToDiagnosisInfoFragment(
                                        diagnosis.get_id(),
                                        ImageCacheManager.saveImageToCache(getContext(), lastDiagnosis.getImage()),
                                        lastDiagnosis.getDeficiency().split(" ")[0],
                                        lastDiagnosis.getTxtDate(),
                                        lastDiagnosis.getDeficiency(),
                                        lastDiagnosis.getRecommendation()
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

    private void mostrarDialogo(String title, String message, ClickMode mode) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mode.equals(ClickMode.NAVIGATE)){
                            dialog.dismiss();
                        }

                    }
                }).show();
    }

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
                NavHostFragment.findNavController(this)
                .navigate(R.id.profileFragment);
            }
        });
    }

    private int[] setHistoryIcons(String nameCrop, String nameDeficiency){
        int[] resources = new int[2];
        switch (nameCrop){
            case "Maiz":
                resources[0] = R.drawable.maiz;
                break;
            case "Frijol":
                resources[0] = R.drawable.frijoles_rojos;
                break;
            default:
                resources[0] = R.drawable.wheat;
                break;
        }

        int lastSpace = nameDeficiency.lastIndexOf(" ");
        String deficiency = (lastSpace == -1) ? nameDeficiency : nameDeficiency.substring(lastSpace + 1);

        switch (deficiency){
            case "nitrogeno":
                resources[1] = R.drawable.nitrogeno;
                break;
            case "fosforo":
                resources[1] = R.drawable.fosforo;
                break;
            case "magnesio":
                resources[1] = R.drawable.magnesio;
                break;
            case "potasio":
                resources[1] = R.drawable.potasio;
                break;
            default:
                resources[1] = R.drawable.cultivo_sano;
                break;
        }

        return resources;
    }

    private DiagnosisHistoryListView DiagnosisToHistoryLV(DiagnosisHistory dh){
        int [] resources = setHistoryIcons(dh.getCrop().getCropName(), dh.getDeficiency());
        Date date = dh.getDiagnosisDate();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateFormated = format.format(date);

        DiagnosisHistoryListView history = new DiagnosisHistoryListView();

        history.setCropIcon(resources[0]);
        history.setDeficiencyIcon(resources[1]);
        history.setId(dh.get_id());
        history.setTxtDate(dateFormated);
        history.setDeficiency(dh.getDeficiency());
        history.setImage(dh.getImage());
        history.setRecommendation(dh.getRecommendation());

        return history;
    }

    // Métodos auxiliares limpios para manejar el estado
    private void showLoader() {
        if (loaderDialog != null && !loaderDialog.isShowing()) {
            loaderDialog.show();
        }
    }

    private void hideLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }

}
