package com.example.agrosmart.presentation.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.agrosmart.R;
import com.example.agrosmart.data.network.auth.AuthResultListener;
import com.example.agrosmart.data.repository.impl.AuthRepositoryImpl;
import com.example.agrosmart.domain.models.User;
import com.example.agrosmart.domain.repository.AuthRepository;
import com.example.agrosmart.domain.usecase.LoginWithGoogleUseCase;
import com.example.agrosmart.presentation.viewmodels.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class Profile_fragment extends Fragment implements AuthResultListener {
    private View imageProfileView;
    private View loginOrAccountView;

    private String nameUser;
    private String emailuser;
    private Uri imageUser;

    private ProfileViewModel profileViewModel;

    //metodo para lanzar el intent(accion) de inicio de seson y recibir su resultado
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    AuthRepository authRepository = new AuthRepositoryImpl();
                    LoginWithGoogleUseCase loginWithGoogleUseCase = new LoginWithGoogleUseCase(authRepository);
                    loginWithGoogleUseCase.processResult(this, data, this);
                } else {
                    // Handle cancellation or other non-OK results
                    onAuthFailure(new Exception("Google Sign-In cancelled or failed."));
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_profile, container, false);

        FrameLayout topLayoutContainer = mainView.findViewById(R.id.topLayoutContainer);
        FrameLayout bottomLayoutContainer = mainView.findViewById(R.id.bottomLayoutContainer);

        imageProfileView = inflater.inflate(R.layout.item_image_profile, topLayoutContainer, false);

        ImageView imageView = imageProfileView.findViewById(R.id.imageProfile);
        TextView textNameView = imageProfileView.findViewById(R.id.textNameProfile);
        TextView textEmailView = imageProfileView.findViewById(R.id.textEmailProfile);

        topLayoutContainer.addView(imageProfileView);

        // se crea la instancia del view model de este fragmento
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // el view model manejara los datos de inicio de sesión para mantener los datos aunque el fragmento se destruya
        profileViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            //se limpia la zona debajo de la imagen para evitar sobre posiciones inesperadas
            topLayoutContainer.removeViews(1, topLayoutContainer.getChildCount() - 1);

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
                loginOrAccountView = inflater.inflate(R.layout.item_account_layout, bottomLayoutContainer, false);
                // se añade a la vista
                bottomLayoutContainer.addView(loginOrAccountView);

                // asignamos el listener al boton de editar perfil para que navegue al fragmento correspondiente
                loginOrAccountView.findViewById(R.id.btnEditarPerfil).setOnClickListener(v -> {
                    navigateToEdit(user);
                });

                // se crea y asigna el listener al boton de cerrar sesión
                View btnSignOut = loginOrAccountView.findViewById(R.id.btnCerrarSesion);
                if (btnSignOut != null) {
                    btnSignOut.setOnClickListener(v -> {
                        FirebaseAuth.getInstance().signOut();
                        profileViewModel.refreshData();
                    });
                }

            } else {
                // proceso correspondiente a cuando el usuario no esta logueado y solo es invitado
                loginOrAccountView = inflater.inflate(R.layout.item_login_layout, bottomLayoutContainer, false);
                bottomLayoutContainer.addView(loginOrAccountView);

                imageView.setImageResource(R.drawable.invitado_holi);
                textNameView.setText("Invitado");
                textEmailView.setText("@invitado");

                // se asigna el listener al boton de iniciar sesión
                loginOrAccountView.findViewById(R.id.btn_auth_google).setOnClickListener(v -> {
                            AuthRepository authRepository = new AuthRepositoryImpl();
                            LoginWithGoogleUseCase loginWithGoogleUseCase = new LoginWithGoogleUseCase(authRepository);
                            Intent signInIntent = loginWithGoogleUseCase.execute(Profile_fragment.this);
                            googleSignInLauncher.launch(signInIntent);
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

    @Override
    public void onAuthSuccess(FirebaseUser user) {
        System.out.println("Usuario Iniciado: " + user.getEmail());
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            profileViewModel.refreshData(); // Pide al ViewModel que actualice el LiveData
        }, 100);
    }

    @Override
    public void onAuthFailure(Exception e) {
        System.out.println("Error de inicio de sesión: " + Arrays.toString(e.getStackTrace()));
    }
}