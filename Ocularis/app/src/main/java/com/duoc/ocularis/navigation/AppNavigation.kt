package com.duoc.ocularis.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.duoc.ocularis.ui.screens.AdminDashboardScreen
import com.duoc.ocularis.ui.screens.AtencionScreen
import com.duoc.ocularis.ui.screens.DetalleExamenScreen
import com.duoc.ocularis.ui.screens.DetallePacienteScreen
import com.duoc.ocularis.ui.screens.GestionUsuariosScreen
import com.duoc.ocularis.ui.screens.HistorialScreen
import com.duoc.ocularis.model.RolUsuario
import com.duoc.ocularis.ui.screens.HomeScreen
import com.duoc.ocularis.ui.screens.HoraMedicaScreen
import com.duoc.ocularis.ui.screens.ListaExamenesScreen
import com.duoc.ocularis.ui.screens.LoginScreen
import com.duoc.ocularis.ui.screens.MisExamenesScreen
import com.duoc.ocularis.ui.screens.PacienteHomeScreen
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

    // Rutas donde SÍ queremos que se vea la barra de navegación del Especialista
    val rutasConBarraInferior = listOf("home", "pacientes", "perfil")

    val startDestination = if (vm.isLoggedIn) {
        when (vm.rolActual) {
            RolUsuario.ADMIN -> Screen.AdminDashboard.route
            RolUsuario.PACIENTE -> Screen.PacienteHome.route
            RolUsuario.ESPECIALISTA -> "home"
        }
    } else {
        Screen.Login.route
    }

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
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Ruta 1: Login con validación exclusiva InsForge y roles de Base de Datos
            composable(route = Screen.Login.route) {
                LoginScreen(
                    onLoginAdmin = {
                        navController.navigate(Screen.AdminDashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onLoginEspecialista = {
                        navController.navigate("home") {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onLoginPaciente = {
                        navController.navigate(Screen.PacienteHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    vm = vm
                )
            }

            // Ruta 2: Inicio del Administrador (AdminDashboardScreen)
            composable(route = Screen.AdminDashboard.route) {
                AdminDashboardScreen(
                    onNavigateToGestionUsuarios = {
                        navController.navigate(Screen.GestionUsuarios.route)
                    },
                    onLogout = {
                        vm.cerrarSesion()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    vm = vm
                )
            }

            // Ruta 3: Gestión de Usuarios (GestionUsuariosScreen)
            composable(route = Screen.GestionUsuarios.route) {
                GestionUsuariosScreen(
                    onNavigateBack = { navController.popBackStack() },
                    vm = vm
                )
            }

            // Ruta 4: Inicio del Paciente (PacienteHomeScreen)
            composable(route = Screen.PacienteHome.route) {
                PacienteHomeScreen(
                    nombrePaciente = vm.usuario,
                    onNavigateToMisExamenes = {
                        navController.navigate(Screen.MisExamenes.route)
                    },
                    onLogout = {
                        vm.cerrarSesion()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    vm = vm
                )
            }

            // Ruta 5: Mis Exámenes del Paciente (MisExamenesScreen)
            composable(route = Screen.MisExamenes.route) {
                MisExamenesScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetalleExamen = { examenId ->
                        navController.navigate(Screen.DetalleExamen.createRoute(examenId))
                    },
                    vm = vm
                )
            }

            // --- Vistas del ESPECIALISTA ---

            // Ruta 6: Inicio / Pacientes (Especialista)
            composable(route = "home") {
                HomeScreen(
                    onNavigateToNuevaAtencion = { navController.navigate(route = "nueva_atencion") },
                    onNavigateToDetalle = { idPaciente ->
                        navController.navigate(Screen.HoraMedica.createRoute(idPaciente))
                    }
                )
            }

            // Ruta 6.4: Consulta / Hora Médica Activa (Especialista)
            composable(
                route = Screen.HoraMedica.route
            ) { backStackEntry ->
                val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: "OC-8921"
                HoraMedicaScreen(
                    pacienteId = pacienteId,
                    vm = vm,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetallePaciente = { idFicha ->
                        navController.navigate(Screen.DetallePaciente.createRoute(idFicha))
                    }
                )
            }

            // Ruta 6.5: Detalle de Paciente
            composable(
                route = Screen.DetallePaciente.route
            ) { backStackEntry ->
                val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: "OC-8921"
                DetallePacienteScreen(
                    pacienteId = pacienteId,
                    vm = vm,
                    onNavigateBack = { navController.popBackStack() },
                    onVerListaExamenes = {
                        navController.navigate(Screen.ListaExamenes.createRoute(pacienteId))
                    }
                )
            }

            // Ruta 6.6: Lista Completa de Exámenes
            composable(
                route = Screen.ListaExamenes.route
            ) { backStackEntry ->
                val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: "OC-8921"
                ListaExamenesScreen(
                    pacienteId = pacienteId,
                    vm = vm,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetalleExamen = { examenId ->
                        navController.navigate(Screen.DetalleExamen.createRoute(examenId))
                    }
                )
            }

            // Ruta 6.7: Detalle del Examen
            composable(
                route = Screen.DetalleExamen.route
            ) { backStackEntry ->
                val examenId = backStackEntry.arguments?.getString("examenId") ?: "EX-0001"
                DetalleExamenScreen(
                    examenId = examenId,
                    vm = vm,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Ruta 7: Pantalla de Nueva Atención
            composable(route = "nueva_atencion") {
                AtencionScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onGuardarAtencion = { navController.popBackStack() }
                )
            }

            // Ruta 8: Directorio de Pacientes
            composable(route = "pacientes") {
                HistorialScreen(
                    vm = vm,
                    onNavigateToDetalle = { idPaciente ->
                        navController.navigate(Screen.DetallePaciente.createRoute(idPaciente))
                    }
                )
            }

            // Ruta 9: Perfil del Especialista
            composable(route = "perfil") {
                PerfilScreen(
                    vm = vm,
                    onLogout = {
                        vm.cerrarSesion()
                        navController.navigate(Screen.Login.route) {
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
            icon = { Icon(Icons.Default.Schedule, contentDescription = null) },
            label = { Text("Agenda") },
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkTeal,
                selectedTextColor = DarkTeal,
                indicatorColor = LightCyan
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.People, contentDescription = null) },
            label = { Text("Pacientes") },
            selected = currentRoute == "pacientes",
            onClick = { onNavigate("pacientes") },
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
