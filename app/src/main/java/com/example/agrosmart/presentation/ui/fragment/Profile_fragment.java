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
import android.util.Log;
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
import com.example.agrosmart.databinding.FragmentProfileBinding;
import com.example.agrosmart.databinding.ItemAccountLayoutBinding;
import com.example.agrosmart.databinding.ItemImageProfileBinding;
import com.example.agrosmart.databinding.ItemLoginLayoutBinding;
import com.example.agrosmart.domain.models.User;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.AuthRepository;
import com.example.agrosmart.domain.usecase.LoginWithGoogleUseCase;
import com.example.agrosmart.presentation.viewmodels.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

public class Profile_fragment extends Fragment implements AuthResultListener {

    private final String TAG = "PROFILE_FRAGMENT";

    private FragmentProfileBinding binding;

    //bindeo de los objetos
    private ItemImageProfileBinding imageProfileBinding;
    private ItemAccountLayoutBinding accountLayoutBinding;
    private ItemLoginLayoutBinding loginLayoutBinding;

    //navegacion
    private NavController navController;

    private String nameUser;
    private String emailuser;
    private Uri imageUser;

    //declaración del viewModel
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
                    onAuthFailure(new Exception(String.format("Google Sign-In cancelled or failed: %s", result)));
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        navController = NavHostFragment.findNavController(this);

        imageProfileBinding = ItemImageProfileBinding.inflate(LayoutInflater.from(getContext()),
                binding.topLayoutContainer, true);

        // el view model manejara los datos de inicio de sesión para mantener los datos aunque el fragmento se destruya
        profileViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            // si hay un usuario iniciado sesión inflaremos la vista con el diseño correspondiente
            if(user!=null){
                renderRegisterUser(user);
            } else {
                // proceso correspondiente a cuando el usuario no esta logueado y solo es invitado
                renderUnregisterUser();
            }
        });
    }

    private void renderUnregisterUser(){
        loginLayoutBinding = ItemLoginLayoutBinding.inflate(LayoutInflater.from(getContext()),
                binding.bottomLayoutContainer, true);

        imageProfileBinding.imageProfile.setImageResource(R.drawable.invitado_holi);
        imageProfileBinding.textNameProfile.setText("Invitado");
        imageProfileBinding.textEmailProfile.setText("@invitado");

        //asignación de listeners
        loginLayoutBinding.btnAuthGoogle.setOnClickListener(v -> {
            AuthRepository authRepository = new AuthRepositoryImpl();
            LoginWithGoogleUseCase loginWithGoogleUseCase = new LoginWithGoogleUseCase(authRepository);
            Intent signInIntent = loginWithGoogleUseCase.execute(Profile_fragment.this);
            googleSignInLauncher.launch(signInIntent);
        });
    }

    private void renderRegisterUser(User user){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            profileViewModel.getUserDetails(Objects.
                            requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())
                    .observe(getViewLifecycleOwner(), userDetails1 -> {
                        if(userDetails1.getStatus().contentEquals("Suspendido")){
                            FirebaseAuth.getInstance().signOut();
                            profileViewModel.refreshData();
                        }
                    });
        }

        accountLayoutBinding = ItemAccountLayoutBinding.inflate(LayoutInflater.from(getContext()),
                binding.bottomLayoutContainer, true);

        //obtencion de los datos del usuario
        nameUser = user.getUsername();
        emailuser = user.getEmail();
        imageUser = user.getImageUser();

        if(imageUser!=null){
            Glide.with(this)
                    .load(imageUser)
                    .circleCrop()
                    .into(imageProfileBinding.imageProfile);
        }

        imageProfileBinding.textNameProfile.setText(nameUser);
        imageProfileBinding.textEmailProfile.setText(emailuser);

        //asignación de listeners
        accountLayoutBinding.btnEditarPerfil.setOnClickListener(v -> {
            navigateToEdit(user);
        });

        accountLayoutBinding.btnConfiguracion.setOnClickListener(v -> {
            navigateToConfig();
        });

        accountLayoutBinding.btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            profileViewModel.refreshData();
            binding.bottomLayoutContainer.removeView(accountLayoutBinding.getRoot());
        });

        accountLayoutBinding.btnVerDatos.setOnClickListener(v -> {
            navigateToProfileData();
        });

    }

    private void navigateToEdit(User firebaseUser){
        NavDirections action = Profile_fragmentDirections
                .actionProfileFragmentToEditProfileFragment().
                setUsername(firebaseUser.getEmail());

        navController.navigate(action);
    }

    private void navigateToConfig(){
        NavDirections action = Profile_fragmentDirections
                .actionProfileFragmentToConfigFragment();

        navController.navigate(action);
    }

    private void navigateToProfileData(){
        NavDirections action = Profile_fragmentDirections
                .actionProfileFragmentToPersonalDataFragment();

        navController.navigate(action);
    }

    @Override
    public void onAuthSuccess(FirebaseUser user) {
        System.out.println("Usuario Iniciado: " + user.getEmail());
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            profileViewModel.refreshData(); // Pide al ViewModel que actualice el LiveData
        }, 100);

        navController.popBackStack(R.id.profileFragment, true);
    }

    @Override
    public void onAuthFailure(Exception e) {
        Log.e(TAG, String.format("Error de inicio de sesión %s", e.getMessage()));
    }
}