package com.example.agrosmart;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import com.example.agrosmart.services.GoogleAuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.atomic.AtomicReference;

public class Profile_fragment extends Fragment {
    private View imageProfileView;
    private View loginOrAccountView;

    private String nameUser;
    private String emailuser;
    private Uri imageUser;

    private GoogleAuthService googleAuthService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_profile, container, false);

        LinearLayout containerLayout = mainView.findViewById(R.id.layout_container);

        imageProfileView = inflater.inflate(R.layout.item_image_profile, containerLayout, false);

        ImageView imageView = imageProfileView.findViewById(R.id.imageProfile);
        TextView textNameView = imageProfileView.findViewById(R.id.textNameProfile);
        TextView textEmailView = imageProfileView.findViewById(R.id.textEmailProfile);

        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if(userAuth!=null){
            nameUser = userAuth.getDisplayName();
            emailuser = userAuth.getEmail();
            imageUser = userAuth.getPhotoUrl();

            if(imageUser!=null){
                Glide.with(this)
                        .load(imageUser)
                        .circleCrop()
                        .into(imageView);
            }

            textNameView.setText(nameUser);
            textEmailView.setText(emailuser);

            loginOrAccountView = inflater.inflate(R.layout.item_account_layout, containerLayout, false);
        } else {
            loginOrAccountView = inflater.inflate(R.layout.item_login_layout, containerLayout, false);

            imageView.setImageResource(R.drawable.invitado_holi);
            textNameView.setText("Invitado");
            textEmailView.setText("@invitado");
        }

        containerLayout.addView(imageProfileView);
        containerLayout.addView(loginOrAccountView);

        googleAuthService = new GoogleAuthService(this,
                user -> {
                    showToast("Google OK: " + user.getEmail());

                    new Handler(Looper.getMainLooper()).postDelayed(this::reloadFragment, 100);
                },
                error -> showToast("Google Error: " + error.toString())
        );

        if (userAuth == null) {
            loginOrAccountView.findViewById(R.id.btn_auth_google).setOnClickListener(v -> {
                        googleAuthService.starSignIn();
                }
            );
        }

        if(userAuth!=null){
            loginOrAccountView.findViewById(R.id.btnEditarPerfil).setOnClickListener(v -> {
                    navigateToEdit(userAuth);
                    });
        }

        View btnSignOut = loginOrAccountView.findViewById(R.id.btnCerrarSesion);
        if (btnSignOut != null) {
            btnSignOut.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                googleAuthService.getGoogleSignInClient().signOut().addOnCompleteListener(task -> {
                    showToast("Sesión cerrada");
                    reloadFragment();
                });
            });
        }

        return mainView;
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void reloadFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.home_Fragment);

    }

    private void navigateToEdit(FirebaseUser firebaseUser){


        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.edit_profile_fragment);
    }
}