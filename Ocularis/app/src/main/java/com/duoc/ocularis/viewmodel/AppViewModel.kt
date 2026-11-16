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
import com.duoc.ocularis.data.entity.EspecialistaEntity
import com.duoc.ocularis.data.entity.ExamenEntity
import com.duoc.ocularis.data.entity.PacienteEntity
import com.duoc.ocularis.data.entity.UsuarioEntity
import com.duoc.ocularis.data.remote.InsForgeClient
import com.duoc.ocularis.data.remote.LoginRequest
import com.duoc.ocularis.data.remote.RegisterRequest
import com.duoc.ocularis.model.RolUsuario
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val database = OcularisDatabase.getDatabase(application)
    private val repository = OcularisRepository(
        database.pacienteDao(),
        database.examenDao(),
        database.consultaMedicaDao(),
        database.especialistaDao(),
        database.usuarioDao()
    )

    // Estado de Usuario Logueado y Rol
    var usuario by mutableStateOf("Dr. Roberto Silva")
    var rolActual by mutableStateOf(RolUsuario.ESPECIALISTA)

    fun seleccionarRol(rol: RolUsuario) {
        rolActual = rol
        usuario = when (rol) {
            RolUsuario.ADMIN -> "Administrador del Sistema"
            RolUsuario.ESPECIALISTA -> "Dr. Roberto Silva"
            RolUsuario.PACIENTE -> "Carmen Rosa Fernández"
        }
    }

    // Autenticación con InsForge y obtención de rol desde la Base de Datos
    fun loginWithInsForge(
        email: String,
        password: String,
        onSuccess: (RolUsuario) -> Unit,
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
                    apikey = InsForgeClient.API_KEY,
                    request = request
                )
                if (response.isSuccessful && response.body() != null) {
                    val userEmail = response.body()!!.user.email

                    // Buscar el rol del usuario en la base de datos (Room)
                    val dbUser = repository.findUsuario(userEmail)
                    val userRol = if (dbUser != null) {
                        when (dbUser.rol.uppercase()) {
                            "ADMIN" -> RolUsuario.ADMIN
                            "PACIENTE" -> RolUsuario.PACIENTE
                            else -> RolUsuario.ESPECIALISTA
                        }
                    } else {
                        // Si es un usuario nuevo en InsForge no registrado en Room, inferir por correo y guardar en la BD
                        val inferred = resolverRolPorCorreo(userEmail)
                        repository.insertUsuario(
                            UsuarioEntity(
                                emailOrRut = userEmail,
                                passwordHash = password,
                                rol = inferred.name
                            )
                        )
                        inferred
                    }

                    usuario = userEmail
                    rolActual = userRol
                    onSuccess(userRol)
                } else {
                    val rawError = response.errorBody()?.string() ?: ""
                    val errorMsg = when {
                        response.code() == 401 || rawError.contains("Invalid credentials") ->
                            "Correo o contraseña incorrectos. El usuario no está registrado en el servidor InsForge."
                        rawError.isNotBlank() -> rawError
                        else -> "Credenciales inválidas"
                    }
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                onError("Error de red: ${e.localizedMessage ?: "No se pudo conectar a InsForge"}")
            }
        }
    }

    // Registro automático de nuevos usuarios en InsForge Auth + Base de datos Room
    fun registerUserWithInsForge(
        email: String,
        password: String,
        rol: RolUsuario,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    email = email,
                    password = password
                )
                val response = InsForgeClient.api.registerUser(
                    authorization = "Bearer ${InsForgeClient.API_KEY}",
                    apikey = InsForgeClient.API_KEY,
                    request = request
                )
                if (response.isSuccessful && response.body() != null) {
                    val authId = response.body()!!.id

                    // Registrar también en la base de datos local Room con su rol
                    repository.insertUsuario(
                        UsuarioEntity(
                            emailOrRut = email,
                            passwordHash = password,
                            rol = rol.name
                        )
                    )

                    onSuccess(authId)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "No se pudo registrar en InsForge"
                    onError("Error de registro InsForge: $errorMsg")
                }
            } catch (e: Exception) {
                onError("Error de red: ${e.localizedMessage ?: "No se pudo conectar a InsForge"}")
            }
        }
    }

    // Auxiliar para inferir rol por correo en caso de nuevos usuarios sin registro en Room
    private fun resolverRolPorCorreo(email: String): RolUsuario {
        val lower = email.trim().lowercase()
        return when {
            lower.startsWith("ad.") || lower.startsWith("admin") -> RolUsuario.ADMIN
            lower.startsWith("pa.") || lower.startsWith("paciente") -> RolUsuario.PACIENTE
            else -> RolUsuario.ESPECIALISTA
        }
    }

    // Flujos reactivos desde la BD
    val pacientes: StateFlow<List<PacienteEntity>> = repository.allPacientes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val examenes: StateFlow<List<ExamenEntity>> = repository.allExamenes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val consultas: StateFlow<List<ConsultaMedicaEntity>> = repository.allConsultas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val especialistas: StateFlow<List<EspecialistaEntity>> = repository.allEspecialistas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
