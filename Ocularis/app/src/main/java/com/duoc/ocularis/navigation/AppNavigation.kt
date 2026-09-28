package com.duoc.ocularis.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.duoc.ocularis.ui.screens.FuncionScreen
import com.duoc.ocularis.ui.screens.HomeScreen
import com.duoc.ocularis.ui.screens.LoginSreen
import com.duoc.ocularis.viewmodel.AppViewModel


@Composable
fun AppNavigation(vm: AppViewModel = viewModel()){
    //controlar los cambios entre pantallas
    val navController = rememberNavController()

    //NavHost = contiene las rutas de la app
    NavHost(navController = navController, startDestination = "Login"){
        //Ruta 1
        composable("login"){
            LoginSreen(onLogin = {navController.navigate("home")})
        }

        //Ruta 2
        composable("home"){
            HomeScreen(nombre = vm.usuario, onFuncion = {navController.navigate("funcion")})
        }

        //Ruta 3
        composable("funcion"){
            FuncionScreen(onVolver = {navController.popBackStack()})
        }
    }
}