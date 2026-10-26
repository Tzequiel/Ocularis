package com.duoc.ocularis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pacientes")
data class PacienteEntity(
    @PrimaryKey
    val rut: String,
    val nombre: String,
    val ficha: String,
    val edad: Int,
    val fechaNacimiento: String = "14/03/1996",
    val prevision: String,
    val telefono: String = "+56 9 8765 4321",
    val email: String = "paciente@correo.cl",
    val alertasClinicas: String = "Sin alertas registradas",
    val proximoControl: String = "Próximo mes",
    val ultimoExamen: String = "Fondo de Ojo",
    val fechaUltimoExamen: String = "Ayer",
    val estado: String = "Estudio Completado"
)
