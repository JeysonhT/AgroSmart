package com.example.agrosmart.screens.profileScreens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.agrosmart.R;
import com.example.agrosmart.models.UserDetails;
import com.example.agrosmart.services.repository.UserDetailsRepository;
import com.example.agrosmart.services.utils.interfaces.OnUserDetailsLoaded;
import com.example.agrosmart.services.viewModels.ProfileViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditProfileFragment extends Fragment {

    private final String TAG = "AgroSmartFirestore";

    private String username;

    private ProfileViewModel profileViewModel;

    private EditText usernametxt;
    private EditText phoneNumbertxt;
    private EditText municipalitytxt;
    private EditText soilTypestxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //recibe los argumentos que envia fragmento que lo invoca
        EditProfileFragmentArgs args =  EditProfileFragmentArgs.fromBundle(getArguments());
        username = args.getUsername();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        // inflo los editText del formulario de datos
        usernametxt = view.findViewById(R.id.etNombre);
        phoneNumbertxt = view.findViewById(R.id.etTelefono);
        municipalitytxt = view.findViewById(R.id.etMunicipio);
        soilTypestxt = view.findViewById(R.id.etSuelo);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // se procede a observar el view model para obtener los datos
       profileViewModel.getUserDetailsLiveData(username).observe(getViewLifecycleOwner(), detail -> {

           if(detail==null){
               // si no hay detalles todavía, el formulario quedara vacío
               usernametxt.setText("");
               phoneNumbertxt.setText("");
               municipalitytxt.setText("");
               soilTypestxt.setText("");
               return;
           }
           usernametxt.setText(detail.getUsername());
           usernametxt.setEnabled(false);
           // se inhabilita el campo de username para futuras actualizaciones de usuario personalizado

           phoneNumbertxt.setText(detail.getPhoneNumber());
           municipalitytxt.setText(detail.getMunicipality());

           // se obtiene la lista de tipos de suelo y
           // se transforma a los string correspondientes
           // haciendo uso de text utils de android
           List<String> soils = (List<String>) detail.getSoilTypes();
           if (soils != null && !soils.isEmpty()) {
               String joined = TextUtils.join(",", soils);
               soilTypestxt.setText(joined);
           }
       });

        //inflar el boton de guardar datos y definir la funcionalidad de su listener
        Button saveButton = view.findViewById(R.id.btnGuardarDetails);

        saveButton.setOnClickListener(v -> {
            obtenerDetalles(username);
        });

        // se retorna la vista
        return view;
    }

    private void obtenerDetalles(String fBSusername){
        // se obtienen los textos de los editText
        //declaro el objeto userDetail
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(fBSusername);
        userDetails.setPhoneNumber(phoneNumbertxt.getText().toString());
        userDetails.setMunicipality(municipalitytxt.getText().toString());

        /* se obtiene el  texto de los tipos de suelo, luego se divide
        *  en las partes que ingreso el usuario separadas por comas
        *  para especificar los tipos de suelo,
        *  siguiente a eso se hace una lista de strings desde la funcion asList
        *  de la clase Arrays para cumplir con la api minima de compatibilidad a Android 7.0
        * */
        String soilText = soilTypestxt.getText().toString();

        String[] parts = soilText.split(",");
        userDetails.setSoilTypes(new ArrayList<>(Arrays.asList(parts)));

        // se guarda el userdetails con el metodo del repositorio correspondiente
        profileViewModel.postDetails(userDetails);;
    }

}

