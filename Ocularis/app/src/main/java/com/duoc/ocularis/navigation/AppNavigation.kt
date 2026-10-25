package com.duoc.ocularis.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.duoc.ocularis.ui.screens.AtencionScreen
import com.duoc.ocularis.ui.screens.DetalleExamenScreen
import com.duoc.ocularis.ui.screens.DetallePacienteScreen
import com.duoc.ocularis.ui.screens.HomeScreen
import com.duoc.ocularis.ui.screens.ListaExamenesScreen
import com.duoc.ocularis.ui.screens.LoginScreen
import com.duoc.ocularis.ui.screens.HistorialScreen
import com.duoc.ocularis.ui.screens.PerfilScreen
import com.duoc.ocularis.viewmodel.AppViewModel

private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)

@Composable
fun AppNavigation(vm: AppViewModel = viewModel()) {
    val navController = rememberNavController()

    // Observamos la ruta actual para saber qué botón pintar y si mostrar la barra
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Rutas donde SÍ queremos que se vea la barra de navegación
    val rutasConBarraInferior = listOf("home", "historial", "perfil")

    Scaffold(
        bottomBar = {
            if (currentRoute in rutasConBarraInferior) {
                AppBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Mantiene 'home' como ancla de la navegación inferior
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        // El NavHost ahora recibe el padding del Scaffold principal
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Ruta 1: Login
            composable(route = "login") {
                LoginScreen(
                    onLogin = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    vm = vm
                )
            }

            // Ruta 2: Inicio / Pacientes
            composable(route = "home") {
                HomeScreen(
                    onNavigateToNuevaAtencion = { navController.navigate(route = "nueva_atencion") },
                    onNavigateToDetalle = { idPaciente ->
                        navController.navigate(Screen.DetallePaciente.createRoute(idPaciente))
                    }
                )
            }

            // Ruta 2.5: Detalle de Paciente
            composable(
                route = Screen.DetallePaciente.route
            ) { backStackEntry ->
                val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: "OC-8921"
                DetallePacienteScreen(
                    pacienteId = pacienteId,
                    onNavigateBack = { navController.popBackStack() },
                    onVerListaExamenes = {
                        navController.navigate(Screen.ListaExamenes.createRoute(pacienteId))
                    }
                )
            }

            // Ruta 2.6: Lista Completa de Exámenes (Pantalla 5.5)
            composable(
                route = Screen.ListaExamenes.route
            ) { backStackEntry ->
                val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: "OC-8921"
                ListaExamenesScreen(
                    pacienteId = pacienteId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetalleExamen = { examenId ->
                        navController.navigate(Screen.DetalleExamen.createRoute(examenId))
                    }
                )
            }

            // Ruta 2.7: Detalle del Examen (Pantalla 6)
            composable(
                route = Screen.DetalleExamen.route
            ) { backStackEntry ->
                val examenId = backStackEntry.arguments?.getString("examenId") ?: "OC-8921"
                DetalleExamenScreen(
                    examenId = examenId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Ruta 3: Pantalla de Nueva Atención
            composable(route = "nueva_atencion") {
                AtencionScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onGuardarAtencion = { navController.popBackStack() }
                )
            }

            // Ruta 4: Historial de Atenciones
            composable(route = "historial") {
                HistorialScreen(
                    onNavigateToDetalle = { idExploracion -> }
                )
            }

            // Ruta 5: Perfil del Especialista
            composable(route = "perfil") {
                PerfilScreen(
                    vm = vm,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.navigate("home") {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

// Componente centralizado de la barra de navegación
@Composable
fun AppBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.People, contentDescription = null) },
            label = { Text("Pacientes") },
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkTeal,
                selectedTextColor = DarkTeal,
                indicatorColor = LightCyan
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Folder, contentDescription = null) },
            label = { Text("Historial") },
            selected = currentRoute == "historial",
            onClick = { onNavigate("historial") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkTeal,
                selectedTextColor = DarkTeal,
                indicatorColor = LightCyan
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Perfil") },
            selected = currentRoute == "perfil",
            onClick = { onNavigate("perfil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkTeal,
                selectedTextColor = DarkTeal,
                indicatorColor = LightCyan
            )
        )
    }
}