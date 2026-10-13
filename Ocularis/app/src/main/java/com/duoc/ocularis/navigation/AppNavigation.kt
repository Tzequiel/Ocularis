package com.duoc.ocularis.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.ocularis.ui.screens.FuncionScreen
import com.duoc.ocularis.ui.screens.HomeScreen
import com.duoc.ocularis.ui.screens.LoginScreen
import com.duoc.ocularis.viewmodel.AppViewModel

@Composable
fun AppNavigation(vm: AppViewModel = viewModel()){
    // controlar los cambios entre pantallas
    val navController = rememberNavController()

    // NavHost contiene las rutas de la app
    NavHost(navController = navController, startDestination = "login"){

        // Ruta 1: Login
        composable(route = "login"){
            // Le pasamos el vm para que guarde el usuario al iniciar sesión
            LoginScreen(
                onLogin = { navController.navigate(route = "home") },
                vm = vm
            )
        }

        // Ruta 2: Inicio / Pacientes (AQUÍ ESTÁ LA CORRECCIÓN)
        composable(route = "home"){
            HomeScreen(
                onNavigateToNuevaAtencion = {
                    navController.navigate(route = "nueva_atencion")
                },
                onNavigateToDetalle = { idPaciente ->
                    // Más adelante lo enviaremos al detalle real, por ahora va a 'funcion'
                    navController.navigate(route = "funcion")
                }
            )
        }

        // Ruta 3: Pantalla de prueba
        composable(route = "funcion"){
            FuncionScreen(onVolver = { navController.popBackStack() })
        }

        // Ruta 4: Placeholder temporal para que el botón "Nueva Atención" no rompa la app
        composable(route = "nueva_atencion"){
            // Mientras construimos la pantalla real, mostramos FuncionScreen
            FuncionScreen(onVolver = { navController.popBackStack() })
        }
    }
}