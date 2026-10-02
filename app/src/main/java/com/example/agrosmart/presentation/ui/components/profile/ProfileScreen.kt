package com.example.agrosmart.presentation.ui.components.profile

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.DataExploration
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.agrosmart.R
import com.example.agrosmart.domain.models.User
import com.example.agrosmart.presentation.theme.AgroSmartTheme
import com.example.agrosmart.presentation.theme.GrassGreen

/**
 * Pantalla principal de Perfil en Jetpack Compose.
 * Soporta tanto el estado de usuario autenticado como el de invitado.
 */
@Composable
fun ProfileScreen(
    user: User?,
    onGoogleSignInClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onViewDataClick: () -> Unit,
    onConfigClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado de perfil: Avatar, Nombre y Correo
        ProfileHeaderSection(user = user)

        Spacer(modifier = Modifier.height(24.dp))

        if (user == null) {
            // Sección de invitado: Llamado a la acción para iniciar sesión
            GuestActionSection(onGoogleSignInClick = onGoogleSignInClick)
        } else {
            // Sección de cuenta: Opciones del usuario registrado
            AccountMenuSection(
                onEditProfileClick = onEditProfileClick,
                onViewDataClick = onViewDataClick,
                onConfigClick = onConfigClick,
                onSignOutClick = onSignOutClick
            )
        }

        // Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * Encabezado con imagen circular, nombre y email del usuario.
 */
@Composable
private fun ProfileHeaderSection(
    user: User?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var avatarBitmap by remember(user?.imageUser) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(user?.imageUser) {
        val uri: Uri? = user?.imageUser
        if (uri != null) {
            Glide.with(context)
                .asBitmap()
                .load(uri)
                .circleCrop()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        avatarBitmap = resource.asImageBitmap()
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        avatarBitmap = null
                    }
                })
        } else {
            avatarBitmap = null
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar circular con elevación y borde sutil
        Surface(
            modifier = Modifier.size(130.dp),
            shape = CircleShape,
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap!!,
                    contentDescription = stringResource(id = R.string.imageprofileText),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.invitado_holi),
                    contentDescription = stringResource(id = R.string.imageprofileText),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre de usuario
        Text(
            text = user?.username?.ifBlank { "Invitado" } ?: "Invitado",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Correo o etiqueta de invitado
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = user?.email?.ifBlank { "@invitado" } ?: "@invitado",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Sección para usuarios no autenticados (invitados).
 */
@Composable
private fun GuestActionSection(
    onGoogleSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Inicia Sesión",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Accede a las funciones completas de respaldo, sincronización e inteligencia artificial con tu cuenta.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Google Sign-In (Directo, sin Row anidada)
            Button(
                onClick = onGoogleSignInClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GrassGreen
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(id = R.string.googleButtonText),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

/**
 * Menú de opciones para usuarios autenticados.
 */
@Composable
private fun AccountMenuSection(
    onEditProfileClick: () -> Unit,
    onViewDataClick: () -> Unit,
    onConfigClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Opción 1: Editar perfil
            ProfileMenuItem(
                icon = ProfileIcons.icons.Edit,
                title = "Editar datos del perfil",
                onClick = onEditProfileClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Opción 2: Ver mis datos
            ProfileMenuItem(
                icon = ProfileIcons.icons.DataExploration,
                title = "Ver mis datos",
                onClick = onViewDataClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Opción 3: Configuraciones
            ProfileMenuItem(
                icon = ProfileIcons.icons.Settings,
                title = "Configuraciones",
                onClick = onConfigClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Opción 4: Cerrar sesión
            ProfileMenuItem(
                icon = ProfileIcons.icons.Logout,
                title = "Cerrar sesión",
                textColor = MaterialTheme.colorScheme.error,
                iconTint = MaterialTheme.colorScheme.error,
                onClick = onSignOutClick
            )
        }
    }
}

/**
 * Fila reutilizable para opciones del menú de perfil.
 */
@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = textColor,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.angulo_derecho_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenGuestPreview() {
    AgroSmartTheme {
        ProfileScreen(
            user = null,
            onGoogleSignInClick = {},
            onEditProfileClick = {},
            onViewDataClick = {},
            onConfigClick = {},
            onSignOutClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenUserPreview() {
    AgroSmartTheme {
        ProfileScreen(
            user = User("Carlos Agricultor", "carlos@agrosmart.com", null),
            onGoogleSignInClick = {},
            onEditProfileClick = {},
            onViewDataClick = {},
            onConfigClick = {},
            onSignOutClick = {}
        )
    }
}

object ProfileIcons {
    val icons = Icons.Rounded
}