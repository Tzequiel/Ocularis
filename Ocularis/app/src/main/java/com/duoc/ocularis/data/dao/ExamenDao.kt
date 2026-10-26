package com.duoc.ocularis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.ocularis.data.entity.ExamenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamenDao {
    @Query("SELECT * FROM examenes")
    fun getAllExamenes(): Flow<List<ExamenEntity>>

    @Query("SELECT * FROM examenes WHERE pacienteRut = :pacienteRut OR pacienteNombre LIKE '%' || :pacienteRut || '%'")
    fun getExamenesByPaciente(pacienteRut: String): Flow<List<ExamenEntity>>

    @Query("SELECT * FROM examenes WHERE id = :id LIMIT 1")
    fun getExamenById(id: String): Flow<ExamenEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamen(examen: ExamenEntity)
}
