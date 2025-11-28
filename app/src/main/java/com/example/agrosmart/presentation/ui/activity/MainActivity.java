package com.example.agrosmart.presentation.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.NavController;

import com.example.agrosmart.R;
import com.example.agrosmart.data.local.realmsetup.realm.RealmSetup;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

        } else {
            Toast.makeText(this, "NavHostFragment no encontrado", Toast.LENGTH_LONG).show();
        }

        //inicialización de realm
        RealmSetup.setRealConfig(this);

        Objects.requireNonNull(getSupportActionBar()).hide(); // Para AppCompatActivity

        setupSplashScreen(splashScreen);
    }

    private void setupSplashScreen(SplashScreen splashScreen){
        final boolean[] isDataReady = {false};

        new Handler(getMainLooper()).postDelayed(() -> {
            isDataReady[0] = true;
        }, 2000);

        splashScreen.setKeepOnScreenCondition(() -> !isDataReady[0]);
    }
}