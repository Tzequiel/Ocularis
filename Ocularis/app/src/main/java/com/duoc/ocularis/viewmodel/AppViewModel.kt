package com.duoc.ocularis.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.ocularis.data.OcularisRepository
import com.duoc.ocularis.data.entity.ConsultaMedicaEntity
import com.duoc.ocularis.data.entity.EspecialistaEntity
import com.duoc.ocularis.data.entity.ExamenEntity
import com.duoc.ocularis.data.entity.PacienteEntity
import com.duoc.ocularis.data.entity.UsuarioEntity
import com.duoc.ocularis.data.remote.InsForgeClient
import com.duoc.ocularis.data.remote.LoginRequest
import com.duoc.ocularis.data.remote.RegisterRequest
import com.duoc.ocularis.data.remote.UserDto
import com.duoc.ocularis.model.RolUsuario
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OcularisRepository()

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
                    val userDto = response.body()!!.user
                    val userEmail = userDto.email

                    // 1. Verificar si InsForge devuelve rol en la metadata o campo role del usuario
                    val insForgeRole = extractRoleFromInsForge(userDto)

                    // 2. Buscar el usuario en la tabla de usuarios de Room
                    val dbUser = repository.findUsuario(userEmail)

                    // 3. Buscar si el correo pertenece a un paciente registrado
                    val dbPaciente = repository.findPacienteByEmail(userEmail)

                    val userRol = when {
                        insForgeRole != null -> insForgeRole
                        dbUser != null -> {
                            when (dbUser.rol.uppercase()) {
                                "ADMIN" -> RolUsuario.ADMIN
                                "PACIENTE" -> RolUsuario.PACIENTE
                                else -> RolUsuario.ESPECIALISTA
                            }
                        }
                        dbPaciente != null -> RolUsuario.PACIENTE
                        else -> resolverRolPorCorreo(userEmail)
                    }

                    // Asegurar persistencia del rol en Room si no existía
                    if (dbUser == null) {
                        repository.insertUsuario(
                            UsuarioEntity(
                                emailOrRut = userEmail,
                                passwordHash = password,
                                rol = userRol.name
                            )
                        )
                    }

                    usuario = when {
                        userRol == RolUsuario.PACIENTE -> {
                            dbPaciente?.nombre
                                ?: if (userEmail.equals("pa.apellido@hospital.org", ignoreCase = true) ||
                                    userEmail.equals("carmen.fernandez@correo.cl", ignoreCase = true)) "Carmen Rosa Fernández"
                                else userEmail.substringBefore("@").replace(".", " ").replace("_", " ")
                                    .split(" ").joinToString(" ") { word -> word.replaceFirstChar { c -> c.uppercase() } }
                        }
                        userRol == RolUsuario.ADMIN -> "Administrador del Sistema"
                        else -> dbUser?.emailOrRut ?: userEmail
                    }

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

    // Auxiliar para extraer el rol directamente del DTO de InsForge si viene en metadata
    private fun extractRoleFromInsForge(user: UserDto): RolUsuario? {
        val roleStr = (user.userMetadata?.get("role") as? String)
            ?: (user.userMetadata?.get("rol") as? String)
            ?: (user.appMetadata?.get("role") as? String)
            ?: (user.appMetadata?.get("rol") as? String)
            ?: user.role

        if (roleStr != null) {
            return when (roleStr.uppercase()) {
                "ADMIN" -> RolUsuario.ADMIN
                "PACIENTE", "PATIENT" -> RolUsuario.PACIENTE
                "ESPECIALISTA", "DOCTOR", "MEDICO" -> RolUsuario.ESPECIALISTA
                else -> null
            }
        }
        return null
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
            lower.contains("admin") || lower.startsWith("ad.") -> RolUsuario.ADMIN
            lower.contains("paciente") || lower.contains("patient") ||
                    lower.startsWith("pa.") || lower.startsWith("pa_") ||
                    lower.startsWith("p.") || lower.startsWith("p_") ||
                    lower.contains("fonasa") || lower.contains("isapre") -> RolUsuario.PACIENTE
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
