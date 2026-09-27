package com.example.agrosmart.presentation.ui.components.profile

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agrosmart.data.network.auth.AuthResultListener
import com.example.agrosmart.data.repository.impl.AuthRepositoryImpl
import com.example.agrosmart.domain.models.User
import com.example.agrosmart.domain.repository.AuthRepository
import com.example.agrosmart.domain.usecase.LoginWithGoogleUseCase
import com.example.agrosmart.presentation.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "ProfileRouteScreen"

@Composable
fun ProfileRouteScreen(
    onNavigateToEditProfile: (String) -> Unit,
    onNavigateToPersonalData: () -> Unit,
    onNavigateToConfig: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentUser by viewModel.userData.observeAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val authRepository: AuthRepository = AuthRepositoryImpl()
            val loginWithGoogleUseCase = LoginWithGoogleUseCase(authRepository)
            loginWithGoogleUseCase.processResult(context, data, object : AuthResultListener {
                override fun onAuthSuccess(user: FirebaseUser?) {
                    viewModel.refreshData()
                    Toast.makeText(context, "Sesión iniciada con éxito", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthFailure(e: Exception?) {
                    Log.e(TAG, "Error autenticación Google", e)
                    Toast.makeText(context, "Error al iniciar sesión: ${e?.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun launchGoogleSignIn() {
        try {
            val authRepository: AuthRepository = AuthRepositoryImpl()
            val loginWithGoogleUseCase = LoginWithGoogleUseCase(authRepository)
            val signInIntent = loginWithGoogleUseCase.execute(context)
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error iniciando Google Sign-In", e)
        }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        viewModel.refreshData()
    }

    LaunchedEffect(currentUser) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        if (!email.isNullOrEmpty()) {
            viewModel.getUserDetails(email).observeForever { userDetails ->
                if (userDetails != null && userDetails.status == "Suspendido") {
                    FirebaseAuth.getInstance().signOut()
                    viewModel.refreshData()
                }
            }
        }
    }

    ProfileScreen(
        user = currentUser,
        onGoogleSignInClick = { launchGoogleSignIn() },
        onEditProfileClick = {
            val username = currentUser?.getUsername() ?: "invitado"
            onNavigateToEditProfile(username)
        },
        onViewDataClick = onNavigateToPersonalData,
        onConfigClick = onNavigateToConfig,
        onSignOutClick = { signOut() },
        modifier = modifier
    )
}
