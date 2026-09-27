package com.example.agrosmart.presentation.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agrosmart.R
import com.example.agrosmart.presentation.theme.AgroSmartTheme
import com.example.agrosmart.presentation.theme.GrassGreen

/**
 * Pantalla de edición de datos de perfil en Jetpack Compose.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    username: String,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    selectedMunicipality: String,
    municipalities: List<String>,
    onMunicipalityChange: (String) -> Unit,
    selectedSoilType: String,
    soilTypes: List<String>,
    onSoilTypeChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    dialogTitle: String? = null,
    dialogMessage: String? = null,
    onDismissDialog: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var municipalityExpanded by remember { mutableStateOf(false) }
    var soilTypeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Barra superior con botón de regreso
        EditProfileTopBar(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta informativa
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Asegúrate de ingresar datos precisos para optimizar las recomendaciones agronómicas en tu zona.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(14.dp)
                )
            }

            // Tarjeta con el formulario
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo 1: Nombre de usuario (Sólo lectura)
                    OutlinedTextField(
                        value = username,
                        onValueChange = {},
                        enabled = false,
                        label = { Text("Nombre de usuario") },
                        supportingText = { Text("El identificador está enlazado a tu cuenta") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Campo 2: Teléfono
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = onPhoneNumberChange,
                        label = { Text("Número de teléfono") },
                        placeholder = { Text("Ej: 88888888") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Campo 3: Municipio (Dropdown)
                    ExposedDropdownMenuBox(
                        expanded = municipalityExpanded,
                        onExpandedChange = { municipalityExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedMunicipality,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Municipio") },
                            placeholder = { Text("Selecciona tu municipio") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = municipalityExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = municipalityExpanded,
                            onDismissRequest = { municipalityExpanded = false }
                        ) {
                            municipalities.forEach { municipality ->
                                DropdownMenuItem(
                                    text = { Text(municipality) },
                                    onClick = {
                                        onMunicipalityChange(municipality)
                                        municipalityExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Campo 4: Tipos de suelo (Dropdown)
                    ExposedDropdownMenuBox(
                        expanded = soilTypeExpanded,
                        onExpandedChange = { soilTypeExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedSoilType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de suelo") },
                            placeholder = { Text("Selecciona el tipo de suelo") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = soilTypeExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = soilTypeExpanded,
                            onDismissRequest = { soilTypeExpanded = false }
                        ) {
                            soilTypes.forEach { soilType ->
                                DropdownMenuItem(
                                    text = { Text(soilType) },
                                    onClick = {
                                        onSoilTypeChange(soilType)
                                        soilTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón Guardar Cambios (Directo, sin Row anidada)
                    Button(
                        onClick = onSaveClick,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GrassGreen
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Guardar Cambios",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }

    // Diálogo de confirmación o error
    if (dialogTitle != null && dialogMessage != null) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            title = {
                Text(
                    text = dialogTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = dialogMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = onDismissDialog) {
                    Text("Aceptar", color = GrassGreen, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

/**
 * Barra superior para la pantalla de edición de perfil.
 */
@Composable
private fun EditProfileTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.angulo_izquierdo_24),
                contentDescription = stringResource(id = R.string.Back_Button),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Editar datos del perfil",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    AgroSmartTheme {
        EditProfileScreen(
            username = "Carlos Agricultor",
            phoneNumber = "88997766",
            onPhoneNumberChange = {},
            selectedMunicipality = "Juigalpa",
            municipalities = listOf("Juigalpa", "Acoyapa", "Comalapa"),
            onMunicipalityChange = {},
            selectedSoilType = "Franco",
            soilTypes = listOf("Arcilloso", "Franco", "Limoso"),
            onSoilTypeChange = {},
            onSaveClick = {},
            onBackClick = {}
        )
    }
}
