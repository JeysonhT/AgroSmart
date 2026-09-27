package com.example.agrosmart.presentation.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.agrosmart.presentation.navigation.AgroSmartNavHost
import com.example.agrosmart.presentation.theme.AgroSmartTheme
import com.example.agrosmart.presentation.ui.components.main.AgroSmartBottomBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupSplashScreen(splashScreen)

        setContent {
            AgroSmartTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AgroSmartBottomBar(navController = navController)
                    }
                ) { innerPadding ->
                    AgroSmartNavHost(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun setupSplashScreen(splashScreen: SplashScreen) {
        var isDataReady = false

        Handler(Looper.getMainLooper()).postDelayed({
            isDataReady = true
        }, 2000)

        splashScreen.setKeepOnScreenCondition { !isDataReady }
    }
}