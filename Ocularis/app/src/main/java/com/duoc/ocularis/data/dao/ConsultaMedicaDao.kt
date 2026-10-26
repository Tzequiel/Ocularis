package com.duoc.ocularis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.ocularis.data.entity.ConsultaMedicaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultaMedicaDao {
    @Query("SELECT * FROM consultas_medicas")
    fun getAllConsultas(): Flow<List<ConsultaMedicaEntity>>

    @Query("SELECT * FROM consultas_medicas WHERE pacienteRut = :pacienteRut")
    fun getConsultasByPaciente(pacienteRut: String): Flow<List<ConsultaMedicaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsulta(consulta: ConsultaMedicaEntity)
}
