package com.duoc.ocularis.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.ocularis.ui.screens.AtencionScreen // <-- IMPORTANDO LA PANTALLA CORRECTA
import com.duoc.ocularis.ui.screens.HomeScreen
import com.duoc.ocularis.ui.screens.LoginScreen
import com.duoc.ocularis.viewmodel.AppViewModel

@Composable
fun AppNavigation(vm: AppViewModel = viewModel()) {
    // Controlar los cambios entre pantallas
    val navController = rememberNavController()

    // NavHost contiene las rutas de la app
    NavHost(navController = navController, startDestination = "login") {

        // Ruta 1: Login
        composable(route = "login") {
            LoginScreen(
                onLogin = { navController.navigate(route = "home") },
                vm = vm
            )
        }

        // Ruta 2: Inicio / Pacientes
        composable(route = "home") {
            HomeScreen(
                onNavigateToNuevaAtencion = {
                    navController.navigate(route = "nueva_atencion")
                },
                onNavigateToDetalle = { idPaciente ->
                    // Por ahora no hace nada hasta que hagamos la pantalla de Detalle
                }
            )
        }

        // Ruta 3: Pantalla de Nueva Atención
        composable(route = "nueva_atencion") {
            AtencionScreen(
                onNavigateBack = { navController.popBackStack() }, // Para el botón Cancelar o flecha de atrás
                onGuardarAtencion = { navController.popBackStack() } // Para el botón Guardar
            )
        }
    }
}