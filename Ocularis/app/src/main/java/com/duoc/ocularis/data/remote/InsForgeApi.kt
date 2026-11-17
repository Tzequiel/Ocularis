package com.duoc.ocularis.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

data class LoginRequest(
    @SerializedName("method") val method: String = "password",
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String? = null,
    @SerializedName("email_verified", alternate = ["emailVerified"]) val emailVerified: Boolean = false,
    @SerializedName("user_metadata", alternate = ["userMetadata"]) val userMetadata: Map<String, Any>? = null,
    @SerializedName("app_metadata", alternate = ["appMetadata"]) val appMetadata: Map<String, Any>? = null
)

data class LoginResponse(
    @SerializedName("user") val user: UserDto,
    @SerializedName("access_token", alternate = ["accessToken"]) val accessToken: String,
    @SerializedName("refresh_token", alternate = ["refreshToken"]) val refreshToken: String? = null
)

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterResponse(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("created_at", alternate = ["createdAt"]) val createdAt: String? = null
)

// DTOs para las tablas de InsForge (PostgREST DB 3FN)
data class UsuarioInsForgeDto(
    @SerializedName("id") val id: String,
    @SerializedName("insforge_auth_id") val insforgeAuthId: String? = null,
    @SerializedName("rut") val rut: String,
    @SerializedName("nombre_completo") val nombreCompleto: String,
    @SerializedName("email") val email: String,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("rol") val rol: String, // 'paciente', 'especialista', 'administrador'
    @SerializedName("activo") val activo: Boolean = true
)

data class PacienteDetalladoDto(
    @SerializedName("paciente_id") val pacienteId: String,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("rut") val rut: String,
    @SerializedName("nombre_completo") val nombreCompleto: String,
    @SerializedName("email") val email: String,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("ficha") val ficha: String,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String,
    @SerializedName("edad") val edad: Int? = null,
    @SerializedName("prevision") val prevision: String,
    @SerializedName("alertas_clinicas") val alertasClinicas: String? = null,
    @SerializedName("estado_clinico") val estadoClinico: String? = null
)

data class EspecialistaInsForgeDto(
    @SerializedName("id") val id: String,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("rcm") val rcm: String,
    @SerializedName("especialidad") val especialidad: String,
    @SerializedName("sucursal_id") val sucursalId: String? = null,
    @SerializedName("box_consulta") val boxConsulta: String? = null,
    @SerializedName("estado_disponibilidad") val estadoDisponibilidad: String? = null
)

data class ConsultaMedicaInsForgeDto(
    @SerializedName("id") val id: String,
    @SerializedName("paciente_id") val pacienteId: String,
    @SerializedName("especialista_id") val especialistaId: String,
    @SerializedName("fecha_hora") val fechaHora: String,
    @SerializedName("tipo_atencion") val tipoAtencion: String,
    @SerializedName("motivo_consulta") val motivoConsulta: String,
    @SerializedName("diagnostico") val diagnostico: String
)

data class ExamenInsForgeDto(
    @SerializedName("id") val id: String,
    @SerializedName("paciente_id") val pacienteId: String,
    @SerializedName("consulta_id") val consultaId: String? = null,
    @SerializedName("tipo_examen") val tipoExamen: String,
    @SerializedName("fecha_hora") val fechaHora: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("ojo_evaluado") val ojoEvaluado: String,
    @SerializedName("observacion") val observacion: String? = null
)

interface InsForgeApiService {
    @Headers("Content-Type: application/json")
    @POST("api/auth/sessions")
    suspend fun login(
        @Header("Authorization") authorization: String,
        @Header("apikey") apikey: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("api/auth/users")
    suspend fun registerUser(
        @Header("Authorization") authorization: String,
        @Header("apikey") apikey: String,
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    // --- ENDPOINTS TABLAS INSFORGE (PostgREST DB) ---

    @GET("rest/v1/usuarios")
    suspend fun getUsuarios(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Query("select") select: String = "*"
    ): Response<List<UsuarioInsForgeDto>>

    @GET("rest/v1/usuarios")
    suspend fun getUsuarioByEmail(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Query("email") emailFilter: String,
        @Query("select") select: String = "*"
    ): Response<List<UsuarioInsForgeDto>>

    @Headers("Content-Type: application/json")
    @POST("rest/v1/usuarios")
    suspend fun insertUsuario(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Body usuario: UsuarioInsForgeDto
    ): Response<List<UsuarioInsForgeDto>>

    @GET("rest/v1/v_pacientes_detallados")
    suspend fun getPacientesDetallados(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Query("select") select: String = "*"
    ): Response<List<PacienteDetalladoDto>>

    @GET("rest/v1/v_pacientes_detallados")
    suspend fun getPacienteDetalladoByEmail(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Query("email") emailFilter: String,
        @Query("select") select: String = "*"
    ): Response<List<PacienteDetalladoDto>>

    @GET("rest/v1/especialistas")
    suspend fun getEspecialistas(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Query("select") select: String = "*"
    ): Response<List<EspecialistaInsForgeDto>>

    @GET("rest/v1/examenes")
    suspend fun getExamenes(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Query("select") select: String = "*"
    ): Response<List<ExamenInsForgeDto>>

    @Headers("Content-Type: application/json")
    @POST("rest/v1/examenes")
    suspend fun insertExamen(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Body examen: ExamenInsForgeDto
    ): Response<List<ExamenInsForgeDto>>

    @GET("rest/v1/consultas_medicas")
    suspend fun getConsultasMedicas(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Query("select") select: String = "*"
    ): Response<List<ConsultaMedicaInsForgeDto>>

    @Headers("Content-Type: application/json")
    @POST("rest/v1/consultas_medicas")
    suspend fun insertConsultaMedica(
        @Header("Authorization") authorization: String = "Bearer ${InsForgeClient.API_KEY}",
        @Header("apikey") apikey: String = InsForgeClient.API_KEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Body consulta: ConsultaMedicaInsForgeDto
    ): Response<List<ConsultaMedicaInsForgeDto>>
}
