package com.duoc.ocularis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.ocularis.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE LOWER(emailOrRut) = LOWER(:emailOrRut) LIMIT 1")
    fun getUsuarioByEmailOrRut(emailOrRut: String): Flow<UsuarioEntity?>

    @Query("SELECT * FROM usuarios WHERE LOWER(emailOrRut) = LOWER(:emailOrRut) LIMIT 1")
    suspend fun findUsuarioByEmailOrRut(emailOrRut: String): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios WHERE emailOrRut = :emailOrRut")
    suspend fun deleteUsuario(emailOrRut: String)

    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): Flow<List<UsuarioEntity>>
}
