package com.example.agrosmart.presentation.ui.components.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.agrosmart.R
import com.example.agrosmart.presentation.navigation.CameraRoute
import com.example.agrosmart.presentation.navigation.ConfigRoute
import com.example.agrosmart.presentation.navigation.CropInfoRoute
import com.example.agrosmart.presentation.navigation.DeficienciesRoute
import com.example.agrosmart.presentation.navigation.DeficiencyInfoRoute
import com.example.agrosmart.presentation.navigation.DetectionRoute
import com.example.agrosmart.presentation.navigation.DiagnosisInfoRoute
import com.example.agrosmart.presentation.navigation.EditProfileRoute
import com.example.agrosmart.presentation.navigation.FertilizerInfoRoute
import com.example.agrosmart.presentation.navigation.FertilizersRoute
import com.example.agrosmart.presentation.navigation.HomeRoute
import com.example.agrosmart.presentation.navigation.PersonalDataRoute
import com.example.agrosmart.presentation.navigation.ProfileRoute
import kotlin.reflect.KClass

sealed class BottomNavItem(
    val route: Any,
    @param:StringRes val titleRes: Int,
    @param:DrawableRes val iconRes: Int,
    val associatedRoutes: List<KClass<*>>
) {
    object Home : BottomNavItem(
        route = HomeRoute,
        titleRes = R.string.String_navigation_1,
        iconRes = R.drawable.hogar_24,
        associatedRoutes = listOf(
            HomeRoute::class,
            CropInfoRoute::class,
            FertilizersRoute::class,
            FertilizerInfoRoute::class,
            DeficienciesRoute::class,
            DeficiencyInfoRoute::class
        )
    )

    object Detection : BottomNavItem(
        route = DetectionRoute(),
        titleRes = R.string.String_navigation_2,
        iconRes = R.drawable.visor_de_la_camara_24,
        associatedRoutes = listOf(
            DetectionRoute::class,
            DiagnosisInfoRoute::class
        )
    )

    object Profile : BottomNavItem(
        route = ProfileRoute,
        titleRes = R.string.String_navigation_4,
        iconRes = R.drawable.usuario_del_portapapeles_24,
        associatedRoutes = listOf(
            ProfileRoute::class,
            EditProfileRoute::class,
            ConfigRoute::class,
            PersonalDataRoute::class
        )
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Detection,
    BottomNavItem.Profile
)

@Composable
fun AgroSmartBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Ocultar barra inferior en pantalla de cámara
    if (currentDestination?.hasRoute(CameraRoute::class) == true) {
        return
    }

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.let { dest ->
                item.associatedRoutes.any { routeClass -> dest.hasRoute(routeClass) }
            } ?: false

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.titleRes),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}
