package com.example.agrosmart;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agrosmart.models.User;
import com.example.agrosmart.services.auth.GoogleAuthService;
import com.example.agrosmart.services.viewModels.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class Profile_fragment extends Fragment {
    private View imageProfileView;
    private View loginOrAccountView;

    private String nameUser;
    private String emailuser;
    private Uri imageUser;

    private GoogleAuthService googleAuthService;

    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_profile, container, false);

        LinearLayout containerLayout = mainView.findViewById(R.id.layout_container);

        imageProfileView = inflater.inflate(R.layout.item_image_profile, containerLayout, false);

        ImageView imageView = imageProfileView.findViewById(R.id.imageProfile);
        TextView textNameView = imageProfileView.findViewById(R.id.textNameProfile);
        TextView textEmailView = imageProfileView.findViewById(R.id.textEmailProfile);

        containerLayout.addView(imageProfileView);

        // se crea la instancia del view model de este fragmento
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // creo la instancia de google auth
        googleAuthService = new GoogleAuthService(
                this,
                user -> {
                    System.out.println("Usuario Iniciado: " + user.getEmail());
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        profileViewModel.refreshData(); // Pide al ViewModel que actualice el LiveData
                    }, 100);
                },
                error -> System.out.println("Error de inicio de sesión: " + Arrays.toString(error.getStackTrace()))
        );

        // el view model manejara los datos de inicio de sesión para mantener los datos aunque el fragmento se destruya
        profileViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            //se limpia la zona debajo de la imagen para evitar sobre posiciones inesperadas
            containerLayout.removeViews(1, containerLayout.getChildCount() - 1);

            // si hay un usuario iniciado sesión inflaremos la vista con el diseño correspondiente
            if(user!=null){
                nameUser = user.getUsername();
                emailuser = user.getEmail();
                imageUser = user.getImageUser();

                if(imageUser!=null){
                    Glide.with(this)
                            .load(imageUser)
                            .circleCrop()
                            .into(imageView);
                }

                textNameView.setText(nameUser);
                textEmailView.setText(emailuser);

                // inflamos la vista con el elemento de botones que corresponden a un usuario logueado
                loginOrAccountView = inflater.inflate(R.layout.item_account_layout, containerLayout, false);
                // se añade a la vista
                containerLayout.addView(loginOrAccountView);

                // asignamos el listener al boton de editar perfil para que navegue al fragmento correspondiente
                loginOrAccountView.findViewById(R.id.btnEditarPerfil).setOnClickListener(v -> {
                    navigateToEdit(user);
                });

                // se crea y asigna el listener al boton de cerrar sesión
                View btnSignOut = loginOrAccountView.findViewById(R.id.btnCerrarSesion);
                if (btnSignOut != null) {
                    btnSignOut.setOnClickListener(v -> {
                        FirebaseAuth.getInstance().signOut();
                        googleAuthService.getGoogleSignInClient().signOut().addOnCompleteListener(task -> {
                            profileViewModel.refreshData();
                        });
                    });
                }

            } else {
                // proceso correspondiente a cuando el usuario no esta logueado y solo es invitado
                loginOrAccountView = inflater.inflate(R.layout.item_login_layout, containerLayout, false);
                containerLayout.addView(loginOrAccountView);

                imageView.setImageResource(R.drawable.invitado_holi);
                textNameView.setText("Invitado");
                textEmailView.setText("@invitado");

                // se asigna el listener al boton de iniciar sesión
                loginOrAccountView.findViewById(R.id.btn_auth_google).setOnClickListener(v -> {
                            googleAuthService.starSignIn();
                        }
                );
            }
        });

        return mainView;
    }

    private void reloadFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.home_Fragment);

    }

    private void navigateToEdit(User firebaseUser){

        NavDirections action = Profile_fragmentDirections
                .actionProfileFragmentToEditProfileFragment().
                setUsername(firebaseUser.getEmail());

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(action);
    }
}