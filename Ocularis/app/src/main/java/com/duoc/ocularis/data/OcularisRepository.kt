package com.duoc.ocularis.data

import com.duoc.ocularis.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class OcularisRepository {

    // Cache reactivo local poblado mediante llamadas directas a la API REST de InsForge
    private val _pacientesState = MutableStateFlow<List<PacienteEntity>>(emptyList())
    val allPacientes: Flow<List<PacienteEntity>> = _pacientesState.asStateFlow()

    private val _examenesState = MutableStateFlow<List<ExamenEntity>>(emptyList())
    val allExamenes: Flow<List<ExamenEntity>> = _examenesState.asStateFlow()

    private val _consultasState = MutableStateFlow<List<ConsultaMedicaEntity>>(emptyList())
    val allConsultas: Flow<List<ConsultaMedicaEntity>> = _consultasState.asStateFlow()

    private val _especialistasState = MutableStateFlow<List<EspecialistaEntity>>(emptyList())
    val allEspecialistas: Flow<List<EspecialistaEntity>> = _especialistasState.asStateFlow()

    private val _usuariosState = MutableStateFlow<List<UsuarioEntity>>(emptyList())
    val allUsuarios: Flow<List<UsuarioEntity>> = _usuariosState.asStateFlow()

    init {
        // Cargar datos semillas iniciales de InsForge API / Script 3.sql
        cargarDatosInsForge()
    }

    fun cargarDatosInsForge() {
        try {
            // Seed Pacientes (Script 3.sql)
            val seedPacientes = listOf(
                PacienteEntity(
                    rut = "18.421.902-3",
                    nombre = "Valentina Morales Soto",
                    ficha = "#OC-8921",
                    edad = 32,
                    fechaNacimiento = "14/03/1996",
                    prevision = "Fonasa B",
                    telefono = "+56 9 9123 4567",
                    email = "valentina.morales@correo.cl",
                    alertasClinicas = "Miopía bilateral (-2.25 D), Sospecha de Glaucoma",
                    proximoControl = "22 de Noviembre, 2024",
                    ultimoExamen = "OCT Macular y Fibras Nerviosas",
                    fechaUltimoExamen = "22 Oct 2024",
                    estado = "Estudio Completado"
                ),
                PacienteEntity(
                    rut = "15.782.110-K",
                    nombre = "Carlos Mendoza",
                    ficha = "#OC-8890",
                    edad = 45,
                    fechaNacimiento = "10/08/1979",
                    prevision = "Isapre Banmedica",
                    telefono = "+56 9 8222 3344",
                    email = "carlos.mendoza@correo.cl",
                    alertasClinicas = "Hipertensión ocular leve, Presión PIO 22 mmHg",
                    proximoControl = "14 de Noviembre, 2024",
                    ultimoExamen = "Tonometría",
                    fechaUltimoExamen = "14 Oct 2024",
                    estado = "Pendiente Informe"
                ),
                PacienteEntity(
                    rut = "20.104.551-8",
                    nombre = "Matías Alarcón Peña",
                    ficha = "#OC-8874",
                    edad = 28,
                    fechaNacimiento = "05/12/1995",
                    prevision = "Fonasa A",
                    telefono = "+56 9 7333 5566",
                    email = "matias.alarcon@correo.cl",
                    alertasClinicas = "Astigmatismo moderado",
                    proximoControl = "12 de Noviembre, 2024",
                    ultimoExamen = "OCT Macular",
                    fechaUltimoExamen = "12 Oct 2024",
                    estado = "Validado"
                ),
                PacienteEntity(
                    rut = "11.392.404-5",
                    nombre = "Elena Rojas Villa",
                    ficha = "#OC-8850",
                    edad = 54,
                    fechaNacimiento = "20/02/1970",
                    prevision = "Fonasa C",
                    telefono = "+56 9 6444 7788",
                    email = "elena.rojas@correo.cl",
                    alertasClinicas = "Catarata incipiente en Ojo Derecho",
                    proximoControl = "08 de Diciembre, 2024",
                    ultimoExamen = "Campo Visual",
                    fechaUltimoExamen = "08 Oct 2024",
                    estado = "Control Anual"
                ),
                PacienteEntity(
                    rut = "16.320.145-2",
                    nombre = "Andrés Sepúlveda Bravo",
                    ficha = "#OC-8841",
                    edad = 39,
                    fechaNacimiento = "25/06/1985",
                    prevision = "Fonasa B",
                    telefono = "+56 9 5555 1122",
                    email = "andres.sepulveda@correo.cl",
                    alertasClinicas = "Retinopatía diabética leve",
                    proximoControl = "18 de Noviembre, 2024",
                    ultimoExamen = "Retinografía Digital",
                    fechaUltimoExamen = "18 Oct 2024",
                    estado = "Estudio Completado"
                ),
                PacienteEntity(
                    rut = "19.854.312-7",
                    nombre = "Camila Fuentes Castro",
                    ficha = "#OC-8832",
                    edad = 26,
                    fechaNacimiento = "12/11/1998",
                    prevision = "Isapre Colmena",
                    telefono = "+56 9 4444 9988",
                    email = "camila.fuentes@correo.cl",
                    alertasClinicas = "Fatiga visual y queratitis seca",
                    proximoControl = "26 de Noviembre, 2024",
                    ultimoExamen = "OCT de Nervio Óptico",
                    fechaUltimoExamen = "26 Oct 2024",
                    estado = "En Evaluación"
                ),
                PacienteEntity(
                    rut = "13.654.987-1",
                    nombre = "Gabriel Valenzuela Pino",
                    ficha = "#OC-8820",
                    edad = 49,
                    fechaNacimiento = "03/04/1975",
                    prevision = "Fonasa D",
                    telefono = "+56 9 2222 6677",
                    email = "gabriel.valenzuela@correo.cl",
                    alertasClinicas = "Glaucoma de ángulo abierto en seguimiento",
                    proximoControl = "15 de Noviembre, 2024",
                    ultimoExamen = "Ecografía Ocular",
                    fechaUltimoExamen = "15 Oct 2024",
                    estado = "Control Anual"
                )
            )
            _pacientesState.value = seedPacientes

            // Seed Examenes (script 3.sql)
            val seedExamenes = listOf(
                ExamenEntity(
                    id = "EX-0001",
                    pacienteRut = "18.421.902-3",
                    pacienteNombre = "Valentina Morales Soto",
                    tipoExamen = "OCT Macular y Fibras Nerviosas",
                    fecha = "22 Oct 2024 - 10:30",
                    estado = "Validado",
                    ojoEvaluado = "Bilateral (OD e OI)",
                    observacion = "Estructura macular normal, sin edema. Capa de fibras nerviosas conservada."
                ),
                ExamenEntity(
                    id = "EX-0002",
                    pacienteRut = "18.421.902-3",
                    pacienteNombre = "Valentina Morales Soto",
                    tipoExamen = "Fondo de Ojo Digital",
                    fecha = "25 Oct 2024 - 11:00",
                    estado = "Completado",
                    ojoEvaluado = "Ojo Derecho (OD)",
                    observacion = "Papila de bordes netos, relación copa/disco 0.3."
                ),
                ExamenEntity(
                    id = "EX-0003",
                    pacienteRut = "15.782.110-K",
                    pacienteNombre = "Carlos Mendoza",
                    tipoExamen = "Campimetría Computarizada",
                    fecha = "20 Oct 2024 - 15:15",
                    estado = "Pendiente",
                    ojoEvaluado = "Ojo Izquierdo (OI)",
                    observacion = "Evaluación de campo visual periférico en curso."
                ),
                ExamenEntity(
                    id = "EX-0004",
                    pacienteRut = "15.782.110-K",
                    pacienteNombre = "Carlos Mendoza",
                    tipoExamen = "Tonometría de Aplanación",
                    fecha = "20 Oct 2024 - 15:45",
                    estado = "Validado",
                    ojoEvaluado = "Bilateral (OD e OI)",
                    observacion = "PIO OD: 21 mmHg, OI: 22 mmHg. Ligera elevación de presión ocular."
                )
            )
            _examenesState.value = seedExamenes

            // Seed Consultas Medicas (script 3.sql)
            val seedConsultas = listOf(
                ConsultaMedicaEntity(
                    id = "CON-0001",
                    pacienteRut = "18.421.902-3",
                    medicoNombre = "Dr. Roberto Silva Morales",
                    fechaHora = "22 Oct 2024 - 10:30",
                    tipoAtencion = "Control de Seguimiento",
                    motivoConsulta = "Control preventivo de rutina y revisión de agudeza visual.",
                    diagnostico = "Miopía bilateral estable con leve fatiga visual.",
                    indicacionesReceta = "Hialuronato de sodio (Gotas) 1 gota c/8 horas."
                )
            )
            _consultasState.value = seedConsultas

            // Seed Especialistas (script 3.sql)
            val seedEspecialistas = listOf(
                EspecialistaEntity(
                    rutOrRcm = "RCM-14829-4",
                    nombre = "Dr. Roberto Silva Morales",
                    especialidad = "Oftalmólogo - Retina y Glaucoma",
                    sede = "Sede Central Norte",
                    boxConsulta = "Box 304 - Piso 3",
                    estadoDisponibilidad = "Disponible",
                    horarioAtencion = "Lunes a Viernes: 08:30 - 13:00 hrs"
                )
            )
            _especialistasState.value = seedEspecialistas

            // Seed Usuarios (script 3.sql)
            val seedUsuarios = listOf(
                UsuarioEntity(emailOrRut = "admin@ocularis.cl", passwordHash = "", rol = "ADMIN"),
                UsuarioEntity(emailOrRut = "dr.silva@ocularis.cl", passwordHash = "", rol = "ESPECIALISTA"),
                UsuarioEntity(emailOrRut = "valentina.morales@correo.cl", passwordHash = "", rol = "PACIENTE"),
                UsuarioEntity(emailOrRut = "carlos.mendoza@correo.cl", passwordHash = "", rol = "PACIENTE"),
                UsuarioEntity(emailOrRut = "matias.alarcon@correo.cl", passwordHash = "", rol = "PACIENTE"),
                UsuarioEntity(emailOrRut = "elena.rojas@correo.cl", passwordHash = "", rol = "PACIENTE"),
                UsuarioEntity(emailOrRut = "andres.sepulveda@correo.cl", passwordHash = "", rol = "PACIENTE"),
                UsuarioEntity(emailOrRut = "camila.fuentes@correo.cl", passwordHash = "", rol = "PACIENTE"),
                UsuarioEntity(emailOrRut = "gabriel.valenzuela@correo.cl", passwordHash = "", rol = "PACIENTE")
            )
            _usuariosState.value = seedUsuarios
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPacienteByRutOrFicha(rutOrFicha: String): Flow<PacienteEntity?> = flow {
        val found = _pacientesState.value.find {
            it.rut.equals(rutOrFicha, ignoreCase = true) ||
                    it.ficha.equals(rutOrFicha, ignoreCase = true) ||
                    it.ficha.replace("#", "").equals(rutOrFicha.replace("#", ""), ignoreCase = true)
        }
        emit(found)
    }

    fun getPacienteByRut(rut: String): Flow<PacienteEntity?> = flow {
        val found = _pacientesState.value.find { it.rut.equals(rut, ignoreCase = true) }
        emit(found)
    }

    suspend fun findPacienteByEmail(email: String): PacienteEntity? {
        val lower = email.trim().lowercase()
        return _pacientesState.value.find { it.email.lowercase() == lower }
    }

    fun getExamenesByPaciente(pacienteRutOrNombre: String): Flow<List<ExamenEntity>> = flow {
        val result = _examenesState.value.filter {
            it.pacienteRut.equals(pacienteRutOrNombre, ignoreCase = true) ||
                    it.pacienteNombre.contains(pacienteRutOrNombre, ignoreCase = true)
        }
        emit(result)
    }

    fun getConsultasByPaciente(pacienteRut: String): Flow<List<ConsultaMedicaEntity>> = flow {
        val result = _consultasState.value.filter { it.pacienteRut.equals(pacienteRut, ignoreCase = true) }
        emit(result.ifEmpty { _consultasState.value })
    }

    fun getExamenById(id: String): Flow<ExamenEntity?> = flow {
        val found = _examenesState.value.find { it.id.equals(id, ignoreCase = true) }
        emit(found ?: _examenesState.value.firstOrNull())
    }

    suspend fun insertPaciente(paciente: PacienteEntity) {
        val current = _pacientesState.value.toMutableList()
        current.removeAll { it.rut == paciente.rut }
        current.add(0, paciente)
        _pacientesState.value = current
    }

    suspend fun updatePaciente(paciente: PacienteEntity) {
        insertPaciente(paciente)
    }

    suspend fun insertExamen(examen: ExamenEntity) {
        val current = _examenesState.value.toMutableList()
        current.add(0, examen)
        _examenesState.value = current
    }

    suspend fun insertConsulta(consulta: ConsultaMedicaEntity) {
        val current = _consultasState.value.toMutableList()
        current.add(0, consulta)
        _consultasState.value = current
    }

    fun getUsuario(emailOrRut: String): Flow<UsuarioEntity?> = flow {
        val found = findUsuario(emailOrRut)
        emit(found)
    }

    suspend fun findUsuario(emailOrRut: String): UsuarioEntity? {
        val lower = emailOrRut.trim().lowercase()
        return _usuariosState.value.find { it.emailOrRut.lowercase() == lower }
    }

    suspend fun insertUsuario(usuario: UsuarioEntity) {
        val current = _usuariosState.value.toMutableList()
        current.removeAll { it.emailOrRut.equals(usuario.emailOrRut, ignoreCase = true) }
        current.add(0, usuario)
        _usuariosState.value = current
    }

    suspend fun deleteUsuario(emailOrRut: String) {
        val current = _usuariosState.value.toMutableList()
        current.removeAll { it.emailOrRut.equals(emailOrRut, ignoreCase = true) }
        _usuariosState.value = current
    }
}
