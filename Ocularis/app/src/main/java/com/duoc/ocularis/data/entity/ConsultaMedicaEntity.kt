package com.duoc.ocularis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consultas_medicas")
data class ConsultaMedicaEntity(
    @PrimaryKey
    val id: String,
    val pacienteRut: String,
    val medicoNombre: String,
    val fechaHora: String,
    val tipoAtencion: String,
    val motivoConsulta: String,
    val diagnostico: String,
    val indicacionesReceta: String
)
