package com.duoc.ocularis.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
//Una funcion que no recive argumentos y sin retorno
fun HomeScreen(
    nombre: String,
    onFuncion: ()-> Unit){
    Column (
        modifier = Modifier.padding(24.dp).fillMaxSize().statusBarsPadding()
    ) {
        Text("Bienvenido $nombre")

        Button(onClick = onFuncion) {Text("Ir a la pantalla de funcionalidad") }
    }
}