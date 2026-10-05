package com.duoc.ocularis.model

data class Paciente(
    val rut: String,
    val nombre: String,
    val ficha: String,
    val edad: Int,
    val prevision: String,
    val ultimoExamen: String,
    val fechaUltimoExamen: String,
    val estado: String // Ej: "Estudio Completado", "Pendiente Informe", "Validado"
)