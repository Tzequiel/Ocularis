package com.duoc.ocularis.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Pacientes : Screen("pacientes")
    object NuevaAtencion : Screen("nueva_atencion")
    object Historial : Screen("historial")
    object SubirExamen : Screen("subir_examen")
    object DetallePaciente : Screen("detalle_paciente/{pacienteId}") {
        fun createRoute(pacienteId: String) = "detalle_paciente/$pacienteId"
    }
    object DetalleExamen : Screen("detalle_examen/{examenId}") {
        fun createRoute(examenId: String) = "detalle_examen/$examenId"
    }
}