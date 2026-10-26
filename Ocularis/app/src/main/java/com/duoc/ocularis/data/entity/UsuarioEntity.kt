package com.duoc.ocularis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val emailOrRut: String,
    val passwordHash: String,
    val rol: String, // "ESPECIALISTA" o "PACIENTE"
    val referenciaId: String? = null
)
