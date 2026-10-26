package com.duoc.ocularis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "especialistas")
data class EspecialistaEntity(
    @PrimaryKey
    val rutOrRcm: String,
    val nombre: String,
    val especialidad: String,
    val sede: String,
    val boxConsulta: String,
    val estadoDisponibilidad: String
)
