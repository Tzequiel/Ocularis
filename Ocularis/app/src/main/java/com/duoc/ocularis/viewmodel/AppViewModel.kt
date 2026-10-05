package com.duoc.ocularis.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.ocularis.model.Examen
import com.duoc.ocularis.model.Paciente

class AppViewModel : ViewModel() {
    // Estado de Usuario Logueado
    var usuario by mutableStateOf("Dr. Roberto Silva")

    // Lista global de Pacientes de prueba
    var listaPacientes by mutableStateOf(
        listOf(
            Paciente("18.421.902-3", "Valentina Morales Soto", "#OC-8921", 32, "Fonasa B", "Fondo de Ojo", "Ayer", "Estudio Completado"),
            Paciente("15.782.110-K", "Carlos Mendoza", "#OC-8890", 45, "Isapre", "Tonometría", "14 Oct", "Pendiente Informe"),
            Paciente("20.104.551-8", "Matías Alarcón Peña", "#OC-8874", 28, "Fonasa A", "OCT Macular", "12 Oct", "Validado")
        )
    )

    // Lista global de Exámenes de prueba
    var listaExamenes by mutableStateOf(
        listOf(
            Examen("OC-8921", "Valentina Morales", "OCT Macular y Fibras Nerviosas", "22 Oct 2024 - 10:30", "Validado", "Bilateral (OD e OI)"),
            Examen("OC-8890", "Carlos Mendoza", "Campimetría Computarizada", "20 Oct 2024 - 15:15", "Pendiente", "Ojo Izquierdo (OI)")
        )
    )
}