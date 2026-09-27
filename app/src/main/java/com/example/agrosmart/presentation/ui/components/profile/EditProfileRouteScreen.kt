package com.example.agrosmart.presentation.ui.components.profile

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agrosmart.R
import com.example.agrosmart.domain.models.UserDetails
import com.example.agrosmart.presentation.viewmodels.ProfileDetailViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EditProfileRouteScreen(
    usernameArg: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileDetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val municipalities = remember { context.resources.getStringArray(R.array.list_municipality).toList() }
    val soilTypes = remember { context.resources.getStringArray(R.array.list_soil_types).toList() }

    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedMunicipality by remember { mutableStateOf("") }
    var selectedSoilType by remember { mutableStateOf("") }

    var dialogTitle by remember { mutableStateOf<String?>(null) }
    var dialogMessage by remember { mutableStateOf<String?>(null) }

    val userDetails = remember { UserDetails() }

    LaunchedEffect(usernameArg) {
        val email = FirebaseAuth.getInstance().currentUser?.email ?: usernameArg
        viewModel.getUserDetailsLiveData(email, context).observeForever { details ->
            if (details != null) {
                username = details.username.orEmpty()
                phoneNumber = details.phoneNumber.orEmpty()
                selectedMunicipality = details.municipality.orEmpty()
                selectedSoilType = details.soilTypes?.firstOrNull().orEmpty()
            }
        }
    }

    EditProfileScreen(
        username = username,
        phoneNumber = phoneNumber,
        onPhoneNumberChange = { phoneNumber = it },
        selectedMunicipality = selectedMunicipality,
        municipalities = municipalities,
        onMunicipalityChange = { selectedMunicipality = it },
        selectedSoilType = selectedSoilType,
        soilTypes = soilTypes,
        onSoilTypeChange = { selectedSoilType = it },
        onSaveClick = {
            if (phoneNumber.length < 10) {
                dialogTitle = "Teléfono Inválido"
                dialogMessage = "El número telefónico debe tener al menos 10 dígitos."
                return@EditProfileScreen
            }

            userDetails.username = username
            userDetails.phoneNumber = phoneNumber
            userDetails.municipality = selectedMunicipality
            userDetails.soilTypes = if (selectedSoilType.isNotBlank()) listOf(selectedSoilType) else emptyList()

            val email = FirebaseAuth.getInstance().currentUser?.email ?: usernameArg
            viewModel.postDetails(userDetails, email)
            dialogTitle = "Perfil Actualizado"
            dialogMessage = "Tus datos han sido guardados exitosamente."
        },
        onBackClick = onBackClick,
        modifier = modifier
    )

    if (dialogTitle != null && dialogMessage != null) {
        AlertDialog(
            onDismissRequest = {
                val wasSuccess = dialogTitle == "Perfil Actualizado"
                dialogTitle = null
                dialogMessage = null
                if (wasSuccess) {
                    onBackClick()
                }
            },
            title = { Text(dialogTitle!!) },
            text = { Text(dialogMessage!!) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val wasSuccess = dialogTitle == "Perfil Actualizado"
                        dialogTitle = null
                        dialogMessage = null
                        if (wasSuccess) {
                            onBackClick()
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}
