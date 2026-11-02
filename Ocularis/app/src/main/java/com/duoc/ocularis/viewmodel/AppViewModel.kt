package com.duoc.ocularis.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.ocularis.data.OcularisDatabase
import com.duoc.ocularis.data.OcularisRepository
import com.duoc.ocularis.data.entity.ConsultaMedicaEntity
import com.duoc.ocularis.data.entity.ExamenEntity
import com.duoc.ocularis.data.entity.PacienteEntity
import com.duoc.ocularis.data.remote.InsForgeClient
import com.duoc.ocularis.data.remote.LoginRequest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val database = OcularisDatabase.getDatabase(application)
    private val repository = OcularisRepository(
        database.pacienteDao(),
        database.examenDao(),
        database.consultaMedicaDao(),
        database.especialistaDao()
    )

    // Estado de Usuario Logueado
    var usuario by mutableStateOf("Dr. Roberto Silva")

    // Flujos reactivos desde la BD
    val pacientes: StateFlow<List<PacienteEntity>> = repository.allPacientes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val examenes: StateFlow<List<ExamenEntity>> = repository.allExamenes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val consultas: StateFlow<List<ConsultaMedicaEntity>> = repository.allConsultas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Autenticación con InsForge
    fun loginWithInsForge(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(
                    method = "password",
                    email = email,
                    password = password
                )
                val response = InsForgeClient.api.login(
                    authorization = "Bearer ${InsForgeClient.API_KEY}",
                    request = request
                )
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    usuario = body.user.email
                    onSuccess()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Credenciales inválidas"
                    onError("Error de autenticación InsForge: $errorMsg")
                }
            } catch (e: Exception) {
                onError("Error de red: ${e.localizedMessage ?: "No se pudo conectar a InsForge"}")
            }
        }
    }

    // Operaciones de escritura persistente
    fun registrarConsultaYExamen(
        consulta: ConsultaMedicaEntity,
        examen: ExamenEntity?,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.insertConsulta(consulta)
            if (examen != null) {
                repository.insertExamen(examen)
            }
            onComplete()
        }
    }

    fun guardarPaciente(paciente: PacienteEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertPaciente(paciente)
            onComplete()
        }
    }

    fun agregarExamen(examen: ExamenEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertExamen(examen)
            onComplete()
        }
    }

    fun getPacienteFlow(rutOrFicha: String): Flow<PacienteEntity?> {
        return repository.getPacienteByRutOrFicha(rutOrFicha)
    }

    fun getExamenesByPacienteFlow(rutOrNombre: String): Flow<List<ExamenEntity>> {
        return repository.getExamenesByPaciente(rutOrNombre)
    }

    fun getConsultasByPacienteFlow(rut: String): Flow<List<ConsultaMedicaEntity>> {
        return repository.getConsultasByPaciente(rut)
    }
}
