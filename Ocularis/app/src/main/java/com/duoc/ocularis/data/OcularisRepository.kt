package com.duoc.ocularis.data

import com.duoc.ocularis.data.dao.*
import com.duoc.ocularis.data.entity.*
import kotlinx.coroutines.flow.Flow

class OcularisRepository(
    private val pacienteDao: PacienteDao,
    private val examenDao: ExamenDao,
    private val consultaDao: ConsultaMedicaDao,
    private val especialistaDao: EspecialistaDao,
    private val usuarioDao: UsuarioDao
) {
    val allPacientes: Flow<List<PacienteEntity>> = pacienteDao.getAllPacientes()
    val allExamenes: Flow<List<ExamenEntity>> = examenDao.getAllExamenes()
    val allConsultas: Flow<List<ConsultaMedicaEntity>> = consultaDao.getAllConsultas()

    fun getPacienteByRutOrFicha(rutOrFicha: String): Flow<PacienteEntity?> {
        return pacienteDao.getPacienteByRutOrFicha(rutOrFicha)
    }

    fun getPacienteByRut(rut: String): Flow<PacienteEntity?> {
        return pacienteDao.getPacienteByRut(rut)
    }

    fun getExamenesByPaciente(pacienteRutOrNombre: String): Flow<List<ExamenEntity>> {
        return examenDao.getExamenesByPaciente(pacienteRutOrNombre)
    }

    fun getConsultasByPaciente(pacienteRut: String): Flow<List<ConsultaMedicaEntity>> {
        return consultaDao.getConsultasByPaciente(pacienteRut)
    }

    fun getExamenById(id: String): Flow<ExamenEntity?> {
        return examenDao.getExamenById(id)
    }

    suspend fun insertPaciente(paciente: PacienteEntity) {
        pacienteDao.insertPaciente(paciente)
    }

    suspend fun updatePaciente(paciente: PacienteEntity) {
        pacienteDao.updatePaciente(paciente)
    }

    suspend fun insertExamen(examen: ExamenEntity) {
        examenDao.insertExamen(examen)
    }

    suspend fun insertConsulta(consulta: ConsultaMedicaEntity) {
        consultaDao.insertConsulta(consulta)
    }

    fun getUsuario(emailOrRut: String): Flow<UsuarioEntity?> {
        return usuarioDao.getUsuarioByEmailOrRut(emailOrRut)
    }

    suspend fun insertUsuario(usuario: UsuarioEntity) {
        usuarioDao.insertUsuario(usuario)
    }
}
