package com.duoc.ocularis.model

data class Examen(
    val id: String,
    val pacienteNombre: String,
    val tipoExamen: String, // Ej: "OCT Macular", "Campimetría Computarizada"
    val fecha: String,
    val estado: String,
    val ojoEvaluado: String, // "OD", "OI", "Bilateral"
    val observacion: String = ""
)