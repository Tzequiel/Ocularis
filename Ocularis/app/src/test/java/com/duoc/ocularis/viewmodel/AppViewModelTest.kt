package com.duoc.ocularis.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.duoc.ocularis.MainDispatcherRule
import com.duoc.ocularis.data.entity.ConsultaMedicaEntity
import com.duoc.ocularis.data.entity.ExamenEntity
import com.duoc.ocularis.data.entity.PacienteEntity
import com.duoc.ocularis.model.RolUsuario
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val application: Application = mockk(relaxed = true)
    private val prefs: SharedPreferences = mockk(relaxed = true)
    private val editor: SharedPreferences.Editor = mockk(relaxed = true)

    private lateinit var viewModel: AppViewModel

    @Before
    fun setUp() {
        every { application.getSharedPreferences("ocularis_session", Context.MODE_PRIVATE) } returns prefs
        every { application.applicationContext } returns application
        every { prefs.getBoolean("is_logged_in", false) } returns false
        every { prefs.getString("saved_user_name", null) } returns null
        every { prefs.getString("saved_role", null) } returns null
        every { prefs.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.clear() } returns editor
        every { editor.apply() } just runs

        viewModel = AppViewModel(application)
    }

    @Test
    fun `test estado inicial de usuario y rol por defecto`() {
        assertFalse(viewModel.isLoggedIn)
        assertEquals("Dr. Roberto Silva", viewModel.usuario)
        assertEquals(RolUsuario.ESPECIALISTA, viewModel.rolActual)
        assertEquals(SyncStatus.SINCRONIZADO, viewModel.syncStatus)
    }

    @Test
    fun `test guardarSesion actualiza estado y sharedPreferences`() {
        viewModel.guardarSesion("Dra. Ana Torres", RolUsuario.ADMIN)

        assertTrue(viewModel.isLoggedIn)
        assertEquals("Dra. Ana Torres", viewModel.usuario)
        assertEquals(RolUsuario.ADMIN, viewModel.rolActual)

        verify { editor.putBoolean("is_logged_in", true) }
        verify { editor.putString("saved_role", RolUsuario.ADMIN.name) }
        verify { editor.putString("saved_user_name", "Dra. Ana Torres") }
    }

    @Test
    fun `test cerrarSesion resetea la sesion`() {
        viewModel.guardarSesion("Dr. Roberto Silva", RolUsuario.ESPECIALISTA)
        viewModel.cerrarSesion()

        assertFalse(viewModel.isLoggedIn)
        verify { editor.clear() }
    }

    @Test
    fun `test seleccionarRol cambia nombre predeterminado segun el rol`() {
        viewModel.seleccionarRol(RolUsuario.ADMIN)
        assertEquals("Administrador del Sistema", viewModel.usuario)
        assertEquals(RolUsuario.ADMIN, viewModel.rolActual)

        viewModel.seleccionarRol(RolUsuario.PACIENTE)
        assertEquals("Carmen Rosa Fernández", viewModel.usuario)
        assertEquals(RolUsuario.PACIENTE, viewModel.rolActual)
    }

    @Test
    fun `test actualizarEstadoSincronizacion modifica syncStatus`() {
        viewModel.actualizarEstadoSincronizacion(SyncStatus.OFFLINE)
        assertEquals(SyncStatus.OFFLINE, viewModel.syncStatus)

        viewModel.actualizarEstadoSincronizacion(SyncStatus.SINCRONIZANDO)
        assertEquals(SyncStatus.SINCRONIZANDO, viewModel.syncStatus)
    }

    @Test
    fun `test guardarPaciente inserta nuevo paciente en el StateFlow`() = runTest {
        val job = backgroundScope.launch { viewModel.pacientes.collect {} }

        val nuevoPaciente = PacienteEntity(
            rut = "12.345.678-9",
            nombre = "Juan Pérez Test",
            ficha = "#OC-9999",
            edad = 40,
            fechaNacimiento = "01/01/1984",
            prevision = "Fonasa B",
            telefono = "+56 9 1111 2222",
            email = "juan.perez@test.cl",
            alertasClinicas = "Ninguna",
            proximoControl = "10/10/2025",
            ultimoExamen = "Tonometría",
            fechaUltimoExamen = "01/01/2025",
            estado = "Al día"
        )

        var completed = false
        viewModel.guardarPaciente(nuevoPaciente) {
            completed = true
        }

        testScheduler.advanceUntilIdle()
        assertTrue(completed)
        assertTrue(viewModel.pacientes.value.any { it.rut == "12.345.678-9" })
        job.cancel()
    }

    @Test
    fun `test agregarExamen inserta examen en el StateFlow`() = runTest {
        val job = backgroundScope.launch { viewModel.examenes.collect {} }

        val nuevoExamen = ExamenEntity(
            id = "EX-9999",
            pacienteRut = "18.421.902-3",
            pacienteNombre = "Valentina Morales Soto",
            tipoExamen = "Retinografía Test",
            fecha = "15 Nov 2024",
            estado = "Validado",
            ojoEvaluado = "OD",
            observacion = "Sin hallazgos patológicos"
        )

        var completed = false
        viewModel.agregarExamen(nuevoExamen) {
            completed = true
        }

        testScheduler.advanceUntilIdle()
        assertTrue(completed)
        assertTrue(viewModel.examenes.value.any { it.id == "EX-9999" })
        job.cancel()
    }

    @Test
    fun `test registrarConsultaYExamen agrega ambos registros`() = runTest {
        val job1 = backgroundScope.launch { viewModel.consultas.collect {} }
        val job2 = backgroundScope.launch { viewModel.examenes.collect {} }

        val consulta = ConsultaMedicaEntity(
            id = "CON-9999",
            pacienteRut = "18.421.902-3",
            medicoNombre = "Dr. Roberto Silva",
            fechaHora = "15 Nov 2024 - 12:00",
            tipoAtencion = "Urgencia",
            motivoConsulta = "Dolor ocular",
            diagnostico = "Conjuntivitis",
            indicacionesReceta = "Gotas antibióticas"
        )

        val examen = ExamenEntity(
            id = "EX-8888",
            pacienteRut = "18.421.902-3",
            pacienteNombre = "Valentina Morales Soto",
            tipoExamen = "Biomicroscopía",
            fecha = "15 Nov 2024",
            estado = "Completado",
            ojoEvaluado = "OI",
            observacion = "Hiperemia conjuntival"
        )

        var completed = false
        viewModel.registrarConsultaYExamen(consulta, examen) {
            completed = true
        }

        testScheduler.advanceUntilIdle()
        assertTrue(completed)
        assertTrue(viewModel.consultas.value.any { it.id == "CON-9999" })
        assertTrue(viewModel.examenes.value.any { it.id == "EX-8888" })
        job1.cancel()
        job2.cancel()
    }
}
