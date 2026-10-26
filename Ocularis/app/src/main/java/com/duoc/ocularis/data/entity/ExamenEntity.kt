package com.duoc.ocularis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "examenes")
data class ExamenEntity(
    @PrimaryKey
    val id: String,
    val pacienteRut: String,
    val pacienteNombre: String,
    val tipoExamen: String,
    val fecha: String,
    val estado: String,
    val ojoEvaluado: String,
    val observacion: String = ""
)
