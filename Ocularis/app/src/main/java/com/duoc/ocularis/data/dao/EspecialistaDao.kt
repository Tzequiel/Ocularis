package com.duoc.ocularis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.ocularis.data.entity.EspecialistaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EspecialistaDao {
    @Query("SELECT * FROM especialistas WHERE rutOrRcm = :rutOrRcm LIMIT 1")
    fun getEspecialista(rutOrRcm: String): Flow<EspecialistaEntity?>

    @Query("SELECT * FROM especialistas")
    fun getAllEspecialistas(): Flow<List<EspecialistaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEspecialista(especialista: EspecialistaEntity)
}
