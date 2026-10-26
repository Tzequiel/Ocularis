package com.duoc.ocularis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.duoc.ocularis.data.entity.PacienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PacienteDao {
    @Query("SELECT * FROM pacientes")
    fun getAllPacientes(): Flow<List<PacienteEntity>>

    @Query("SELECT * FROM pacientes WHERE rut = :rut OR ficha = :rut LIMIT 1")
    fun getPacienteByRutOrFicha(rut: String): Flow<PacienteEntity?>

    @Query("SELECT * FROM pacientes WHERE rut = :rut LIMIT 1")
    fun getPacienteByRut(rut: String): Flow<PacienteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaciente(paciente: PacienteEntity)

    @Update
    suspend fun updatePaciente(paciente: PacienteEntity)
}
