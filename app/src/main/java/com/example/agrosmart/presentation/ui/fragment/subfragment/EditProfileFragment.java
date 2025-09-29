package com.example.agrosmart.presentation.ui.fragment.subfragment;

import android.content.DialogInterface;
import android.os.Bundle;

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
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.presentation.viewmodels.ProfileDetailViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.RealmList;

public class EditProfileFragment extends Fragment {

    private final String TAG = "EDIT_PROFILE_FRAGMENT";

    private String userEmail;

    FirebaseUser user;

    private ProfileDetailViewModel profileViewModel;

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
        userEmail = args.getUsername();

        user = FirebaseAuth.getInstance().getCurrentUser();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        // inflo los editText del formulario de datos
        usernametxt = view.findViewById(R.id.etNombre);
        phoneNumbertxt = view.findViewById(R.id.etTelefono);
        municipalitytxt = view.findViewById(R.id.etMunicipio);
        soilTypestxt = view.findViewById(R.id.etSuelo);

        profileViewModel = new ViewModelProvider(this).get(ProfileDetailViewModel.class);

        UserDetails userDetails = new UserDetails();

        // se procede a observar el view model para obtener los datos
       profileViewModel.getUserDetailsLiveData(userEmail).observe(getViewLifecycleOwner(), detail -> {

           if(detail==null){
               // si no hay detalles todavía, el formulario quedara vacío
               usernametxt.setText("");
               phoneNumbertxt.setText("");
               municipalitytxt.setText("");
               soilTypestxt.setText("");
               return;
           }


           userDetails.setUsername(detail.getUsername());
           userDetails.setPhoneNumber(detail.getPhoneNumber());
           userDetails.setMunicipality(detail.getMunicipality());
           userDetails.setRole(detail.getRole());

           usernametxt.setText(detail.getUsername());
           usernametxt.setEnabled(false);
           // se inhabilita el campo de username para futuras actualizaciones de usuario personalizado

           phoneNumbertxt.setText(detail.getPhoneNumber());
           municipalitytxt.setText(detail.getMunicipality());

           // se obtiene la lista de tipos de suelo y
           // se transforma a los string correspondientes
           // haciendo uso de text utils de android
           List<String> soils = detail.getSoilTypes();
           if (soils != null && !soils.isEmpty()) {
               String joined = TextUtils.join(",", soils);
               soilTypestxt.setText(joined);
           }
       });

        //inflar el boton de guardar datos y definir la funcionalidad de su listener
        Button saveButton = view.findViewById(R.id.btnGuardarDetails);

        saveButton.setOnClickListener(v -> {
            try {
                obtenerDetalles(userEmail, user.getDisplayName(), userDetails);
            } catch (Exception e){
                mostrarDialogo("Error",e.getMessage());
                Log.println(Log.ERROR, TAG, e.getMessage());
            }
        });

        // se retorna la vista
        return view;
    }

    private void obtenerDetalles(String fBSuserEmail, String username, UserDetails userDetails){
        // se obtienen los textos de los editText
        userDetails.setUsername(username);

        if(phoneNumbertxt.getText().toString().strip().length() < 8){
            throw new IllegalArgumentException("numero muy corto, ingreselo correctamente");
        } else if(!phoneNumbertxt.getText().toString().matches("^\\d+$")){
            throw new IllegalArgumentException("El numero telefonico solo debe incluir numeros");
        }
        userDetails.setPhoneNumber(phoneNumbertxt.getText().toString());

        if(municipalitytxt.getText().toString().strip().length() < 4){
            throw new IllegalArgumentException("Nombre de municipio muy corto");
        } else if(!municipalitytxt.getText().toString().matches("^[a-zA-Z]+$")){
            throw new IllegalArgumentException("El nombre del municipio solo debe de contener letras");
        }
        userDetails.setMunicipality(municipalitytxt.getText().toString());

        /* se obtiene el  texto de los tipos de suelo, luego se divide
        *  en las partes que ingreso el usuario separadas por comas
        *  para especificar los tipos de suelo,
        *  siguiente a eso se hace una lista de strings desde la funcion asList
        *  de la clase Arrays para cumplir con la api minima de compatibilidad a Android 7.0
        * */
        String soilText = soilTypestxt.getText().toString();

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

}

