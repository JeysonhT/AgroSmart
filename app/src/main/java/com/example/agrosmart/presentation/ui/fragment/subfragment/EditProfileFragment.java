package com.example.agrosmart.presentation.ui.fragment.subfragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.agrosmart.R;
import com.example.agrosmart.databinding.FragmentEditprofileBinding;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.presentation.viewmodels.ProfileDetailViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.integrity.internal.c;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.RealmList;

public class EditProfileFragment extends Fragment {

    private final String TAG = "EDIT_PROFILE_FRAGMENT";
    private FragmentEditprofileBinding binding;
    private String userEmail;
    FirebaseUser user;
    private ProfileDetailViewModel profileViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditprofileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recibe los argumentos que envia fragmento que lo invoca
        EditProfileFragmentArgs args =  EditProfileFragmentArgs.fromBundle(getArguments());
        userEmail = args.getUsername();

        user = FirebaseAuth.getInstance().getCurrentUser();

        profileViewModel = new ViewModelProvider(this).get(ProfileDetailViewModel.class);

        UserDetails userDetails = new UserDetails();

        String[] municipios = getResources().getStringArray(R.array.list_municipality);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.list_item_dropdown,
                municipios
        );

        String[] soilTypes = getResources().getStringArray(R.array.list_soil_types);

        ArrayAdapter<String> soilAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.list_item_dropdown,
                soilTypes
        );

        binding.tvMunicipioAuto.setAdapter(adapter);
        binding.tvSoilTypes.setAdapter(soilAdapter);

        // se procede a observar el view model para obtener los datos
        profileViewModel.getUserDetailsLiveData(userEmail, requireContext()).observe(getViewLifecycleOwner(), detail -> {

            if(detail==null){
                // si no hay detalles todavía, el formulario quedara vacío
                binding.etNombre.setText("");
                binding.etTelefono.setText("");
                return;
            }

            userDetails.setUsername(detail.getUsername());
            userDetails.setPhoneNumber(detail.getPhoneNumber());
            userDetails.setMunicipality(detail.getMunicipality());
            userDetails.setRole(detail.getRole());

            binding.etNombre.setText(detail.getUsername());
            binding.etNombre.setEnabled(false);
            // se inhabilita el campo de username para futuras actualizaciones de usuario personalizado

            binding.etTelefono.setText(detail.getPhoneNumber());
            binding.tvMunicipioAuto.setListSelection(getIndexOfMunicipality(detail.getMunicipality(), municipios));
        });

        binding.btnGuardarDetails.setOnClickListener(v -> {
            try {
                obtenerDetalles(userEmail, user.getDisplayName(), userDetails);
            } catch (Exception e){
                mostrarDialogo("Error",e.getMessage());
                Log.println(Log.ERROR, TAG, e.getMessage());
            }
        });
    }

    private void obtenerDetalles(String fBSuserEmail, String username, UserDetails userDetails){
        // se obtienen los textos de los editText
        userDetails.setUsername(username);

        if(binding.etTelefono.getText().toString().strip().length() < 8){
            throw new IllegalArgumentException("numero muy corto, ingreselo correctamente");
        } else if(!binding.etTelefono.getText().toString().matches("^\\d+$")){
            throw new IllegalArgumentException("El numero telefonico solo debe incluir numeros");
        }
        userDetails.setPhoneNumber(binding.etTelefono.getText().toString());

        if(binding.tvMunicipioAuto.getText().toString().strip().length() < 4){
            throw new IllegalArgumentException("Nombre de municipio muy corto");
        }
        userDetails.setMunicipality(binding.tvMunicipioAuto.getText().toString());

        /* se obtiene el  texto de los tipos de suelo, luego se divide
        *  en las partes que ingreso el usuario separadas por comas
        *  para especificar los tipos de suelo,
        *  siguiente a eso se hace una lista de strings desde la funcion asList
        *  de la clase Arrays para cumplir con la api minima de compatibilidad a Android 7.0
        * */
        String soilText = binding.tvSoilTypes.getText().toString();

        RealmList<String> lista = new RealmList<>();

        String[] parts = soilText.split(",");
        lista.addAll(List.of(parts));
        userDetails.setSoilTypes(lista);

        if(userDetails.getRole()==null){
            userDetails.setRole("Agricultor");
        }

        if(userDetails.getStatus() == null){
            userDetails.setStatus("Activo");
        }

        // se guarda el userdetails con el metodo del repositorio correspondiente
        profileViewModel.postDetails(userDetails, fBSuserEmail);

        mostrarDialogo("Mensaje", "Datos guardados");
    }

    //metodos auxiliares
    private void mostrarDialogo(String titulo,String mensaje) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private int getIndexOfMunicipality(String municipality, String[] municipalities){
        for(int i = 0; i<=municipalities.length; i++){
            if(municipality.equalsIgnoreCase(municipalities[i])){
                return i;
            }
        }
        return 0;
    }



}

