package com.duoc.ocularis.data

import com.duoc.ocularis.data.entity.*
import com.duoc.ocularis.data.remote.InsForgeClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class OcularisRepository {

    // Cache reactivo local poblado mediante llamadas directas a la API REST de InsForge con respaldo offline
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
        // Cargar datos iniciales solo si la caché está vacía para preservar actualizaciones locales
        if (_pacientesState.value.isEmpty()) {
            cargarSeedLocal()
            cargarDatosInsForge()
        }
    }

    fun cargarDatosInsForge() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Sincronizar Pacientes desde InsForge
                val respPacientes = InsForgeClient.api.getPacientesDetallados()
                if (respPacientes.isSuccessful && !respPacientes.body().isNullOrEmpty()) {
                    val dtoPacientes = respPacientes.body()!!
                    val pacientesMapeados = dtoPacientes.map { dto ->
                        PacienteEntity(
                            rut = dto.rut,
                            nombre = dto.nombreCompleto,
                            ficha = dto.ficha,
                            edad = dto.edad ?: 35,
                            fechaNacimiento = dto.fechaNacimiento.take(10),
                            prevision = dto.prevision,
                            telefono = dto.telefono ?: "+56 9 0000 0000",
                            email = dto.email,
                            alertasClinicas = dto.alertasClinicas ?: "Sin alertas registradas",
                            proximoControl = "Próximo control programado",
                            ultimoExamen = dto.estadoClinico ?: "Control Oftalmológico",
                            fechaUltimoExamen = "Reciente",
                            estado = dto.estadoClinico ?: "Validado"
                        )
                    }
                    _pacientesState.value = pacientesMapeados
                }

                // 2. Sincronizar Exámenes desde InsForge
                val respExamenes = InsForgeClient.api.getExamenes()
                if (respExamenes.isSuccessful && !respExamenes.body().isNullOrEmpty()) {
                    val dtoExamenes = respExamenes.body()!!
                    val listaP = _pacientesState.value
                    val examenesMapeados = dtoExamenes.map { dto ->
                        val pacienteAsociado = listaP.find { it.ficha.contains(dto.pacienteId.takeLast(4)) } ?: listaP.firstOrNull()
                        ExamenEntity(
                            id = dto.id,
                            pacienteRut = pacienteAsociado?.rut ?: "18.421.902-3",
                            pacienteNombre = pacienteAsociado?.nombre ?: "Paciente Ocularis",
                            tipoExamen = dto.tipoExamen,
                            fecha = dto.fechaHora,
                            estado = dto.estado,
                            ojoEvaluado = dto.ojoEvaluado,
                            observacion = dto.observacion ?: "Sin observaciones clínicas registradas."
                        )
                    }
                    _examenesState.value = examenesMapeados
                }

                // 3. Sincronizar Consultas desde InsForge
                val respConsultas = InsForgeClient.api.getConsultasMedicas()
                if (respConsultas.isSuccessful && !respConsultas.body().isNullOrEmpty()) {
                    val dtoConsultas = respConsultas.body()!!
                    val listaP = _pacientesState.value
                    val consultasMapeadas = dtoConsultas.map { dto ->
                        val pacienteAsociado = listaP.find { it.ficha.contains(dto.pacienteId.takeLast(4)) } ?: listaP.firstOrNull()
                        ConsultaMedicaEntity(
                            id = dto.id,
                            pacienteRut = pacienteAsociado?.rut ?: "18.421.902-3",
                            medicoNombre = "Dr. Roberto Silva Morales",
                            fechaHora = dto.fechaHora,
                            tipoAtencion = dto.tipoAtencion,
                            motivoConsulta = dto.motivoConsulta,
                            diagnostico = dto.diagnostico,
                            indicacionesReceta = "Seguir tratamiento indicado por especialista."
                        )
                    }
                    _consultasState.value = consultasMapeadas
                }

                // 4. Sincronizar Especialistas desde InsForge
                val respEspecialistas = InsForgeClient.api.getEspecialistas()
                if (respEspecialistas.isSuccessful && !respEspecialistas.body().isNullOrEmpty()) {
                    val dtoEsp = respEspecialistas.body()!!
                    val espMapeados = dtoEsp.map { dto ->
                        EspecialistaEntity(
                            rutOrRcm = dto.rcm,
                            nombre = "Dr. Especialista Ocularis",
                            especialidad = dto.especialidad,
                            sede = "Sede Central Norte",
                            boxConsulta = dto.boxConsulta ?: "Box 304",
                            estadoDisponibilidad = dto.estadoDisponibilidad ?: "Disponible",
                            horarioAtencion = "Lunes a Viernes: 08:30 - 13:00 hrs"
                        )
                    }
                    _especialistasState.value = espMapeados
                }

                // 5. Sincronizar Usuarios desde InsForge
                val respUsuarios = InsForgeClient.api.getUsuarios()
                if (respUsuarios.isSuccessful && !respUsuarios.body().isNullOrEmpty()) {
                    val dtoUsers = respUsuarios.body()!!
                    val usersMapeados = dtoUsers.map { dto ->
                        UsuarioEntity(
                            emailOrRut = dto.email,
                            passwordHash = "",
                            rol = dto.rol.uppercase().let { r ->
                                when {
                                    r.contains("ADMIN") -> "ADMIN"
                                    r.contains("PACIENTE") -> "PACIENTE"
                                    else -> "ESPECIALISTA"
                                }
                            }
                        )
                    }
                    _usuariosState.value = usersMapeados
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Si falla la red, mantenemos los datos locales de respaldo (seed)
            }
        }
    }

    private fun cargarSeedLocal() {
        val seedPacientes = listOf(
            PacienteEntity("18.421.902-3", "Valentina Morales Soto", "#OC-8921", 32, "14/03/1996", "Fonasa B", "+56 9 9123 4567", "valentina.morales@correo.cl", "Miopía bilateral (-2.25 D), Sospecha de Glaucoma", "22 de Noviembre, 2024", "OCT Macular y Fibras Nerviosas", "22 Oct 2024", "Estudio Completado"),
            PacienteEntity("15.782.110-K", "Carlos Mendoza", "#OC-8890", 45, "10/08/1979", "Isapre Banmedica", "+56 9 8222 3344", "carlos.mendoza@correo.cl", "Hipertensión ocular leve, Presión PIO 22 mmHg", "14 de Noviembre, 2024", "Tonometría", "14 Oct 2024", "Pendiente Informe"),
            PacienteEntity("20.104.551-8", "Matías Alarcón Peña", "#OC-8874", 28, "05/12/1995", "Fonasa A", "+56 9 7333 5566", "matias.alarcon@correo.cl", "Astigmatismo moderado", "12 de Noviembre, 2024", "OCT Macular", "12 Oct 2024", "Validado"),
            PacienteEntity("11.392.404-5", "Elena Rojas Villa", "#OC-8850", 54, "20/02/1970", "Fonasa C", "+56 9 6444 7788", "elena.rojas@correo.cl", "Catarata incipiente en Ojo Derecho", "08 de Diciembre, 2024", "Campo Visual", "08 Oct 2024", "Control Anual"),
            PacienteEntity("16.320.145-2", "Andrés Sepúlveda Bravo", "#OC-8841", 39, "25/06/1985", "Fonasa B", "+56 9 5555 1122", "andres.sepulveda@correo.cl", "Retinopatía diabética leve", "18 de Noviembre, 2024", "Retinografía Digital", "18 Oct 2024", "Estudio Completado"),
            PacienteEntity("19.854.312-7", "Camila Fuentes Castro", "#OC-8832", 26, "12/11/1998", "Isapre Colmena", "+56 9 4444 9988", "camila.fuentes@correo.cl", "Fatiga visual y queratitis seca", "26 de Noviembre, 2024", "OCT de Nervio Óptico", "26 Oct 2024", "En Evaluación"),
            PacienteEntity("13.654.987-1", "Gabriel Valenzuela Pino", "#OC-8820", 49, "03/04/1975", "Fonasa D", "+56 9 2222 6677", "gabriel.valenzuela@correo.cl", "Glaucoma de ángulo abierto en seguimiento", "15 de Noviembre, 2024", "Ecografía Ocular", "15 Oct 2024", "Control Anual")
        )
        _pacientesState.value = seedPacientes

        val seedExamenes = listOf(
            ExamenEntity("EX-0001", "18.421.902-3", "Valentina Morales Soto", "OCT Macular y Fibras Nerviosas", "Oct 22, 2024, 07:30 AM", "Validado", "Bilateral (OD e OI)", "Estructura macular normal, sin edema."),
            ExamenEntity("EX-0002", "18.421.902-3", "Valentina Morales Soto", "Fondo de Ojo Digital", "Oct 25, 2024, 08:00 AM", "Completado", "Ojo Derecho (OD)", "Papila de bordes netos, relación copa/disco 0.3."),
            ExamenEntity("EX-0003", "15.782.110-K", "Carlos Mendoza", "Campimetría Computarizada", "Oct 20, 2024, 12:15 PM", "Pendiente", "Ojo Izquierdo (OI)", "Evaluación de campo visual periférico en curso."),
            ExamenEntity("EX-0004", "15.782.110-K", "Carlos Mendoza", "Tonometría de Aplanación", "Oct 20, 2024, 12:45 PM", "Validado", "Bilateral (OD e OI)", "PIO OD: 21 mmHg, OI: 22 mmHg."),
            ExamenEntity("EX-0005", "20.104.551-8", "Matías Alarcón Peña", "OCT Macular", "Oct 12, 2024, 06:00 AM", "Validado", "Ojo Izquierdo (OI)", "Sin alteraciones maculares significativas."),
            ExamenEntity("EX-0006", "20.104.551-8", "Matías Alarcón Peña", "Topografía Corneal", "Oct 12, 2024, 06:30 AM", "Completado", "Bilateral (OD e OI)", "Astigmatismo miópico regular bilateral."),
            ExamenEntity("EX-0007", "11.392.404-5", "Elena Rojas Villa", "Campo Visual Perimétrico", "Oct 08, 2024, 11:00 AM", "Validado", "Ojo Derecho (OD)", "Escotoma relativo periférico leve."),
            ExamenEntity("EX-0008", "11.392.404-5", "Elena Rojas Villa", "Biometría Ocular", "Oct 08, 2024, 11:40 AM", "Validado", "Bilateral (OD e OI)", "Cálculo de LIO intraocular registrado."),
            ExamenEntity("EX-0009", "16.320.145-2", "Andrés Sepúlveda Bravo", "Retinografía Digital Con Contraste", "Oct 18, 2024, 09:00 AM", "Validado", "Bilateral (OD e OI)", "Presencia de microaneurismas leves."),
            ExamenEntity("EX-0010", "16.320.145-2", "Andrés Sepúlveda Bravo", "Paquimetría Corneal", "Oct 18, 2024, 09:30 AM", "Validado", "Bilateral (OD e OI)", "Grosor corneal central promedio."),
            ExamenEntity("EX-0011", "19.854.312-7", "Camila Fuentes Castro", "OCT de Nervio Óptico", "Oct 26, 2024, 01:00 PM", "En Proceso", "Bilateral (OD e OI)", "Análisis de capa de fibras en progreso."),
            ExamenEntity("EX-0012", "19.854.312-7", "Camila Fuentes Castro", "Angiografía de Retina con Fluoresceína", "Oct 26, 2024, 01:45 PM", "Pendiente", "Ojo Derecho (OD)", "Programado para estudio vascular."),
            ExamenEntity("EX-0013", "13.654.987-1", "Gabriel Valenzuela Pino", "Ecografía Ocular Modo A/B", "Oct 15, 2024, 07:15 AM", "Validado", "Ojo Izquierdo (OI)", "Vítreo claro, retina aplicada en 360 grados."),
            ExamenEntity("EX-0014", "13.654.987-1", "Gabriel Valenzuela Pino", "Curva de Tensión Ocular (PIO)", "Oct 15, 2024, 08:30 AM", "Completado", "Bilateral (OD e OI)", "Registro tensional dentro de rangos.")
        )
        _examenesState.value = seedExamenes

        val seedConsultas = listOf(
            ConsultaMedicaEntity("CON-0001", "18.421.902-3", "Dr. Roberto Silva Morales", "Oct 22, 2024, 07:30 AM", "Control de Seguimiento", "Control preventivo de rutina y revisión de agudeza visual.", "Miopía bilateral estable con leve fatiga visual.", "Hialuronato de sodio (Gotas) 1 gota c/8 horas.")
        )
        _consultasState.value = seedConsultas

        val seedEspecialistas = listOf(
            EspecialistaEntity("RCM-14829-4", "Dr. Roberto Silva Morales", "Oftalmólogo - Retina y Glaucoma", "Sede Central Norte", "Box 304 - Piso 3", "Disponible", "Lunes a Viernes: 08:30 - 13:00 hrs")
        )
        _especialistasState.value = seedEspecialistas

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
        if (result.isNotEmpty()) {
            emit(result)
        } else {
            val paciente = _pacientesState.value.find {
                it.rut.equals(pacienteRutOrNombre, ignoreCase = true) ||
                it.nombre.contains(pacienteRutOrNombre, ignoreCase = true) ||
                it.ficha.equals(pacienteRutOrNombre, ignoreCase = true)
            }
            if (paciente != null) {
                emit(
                    listOf(
                        ExamenEntity(
                            id = "EX-${paciente.rut.hashCode().absoluteValue}",
                            pacienteRut = paciente.rut,
                            pacienteNombre = paciente.nombre,
                            tipoExamen = paciente.ultimoExamen,
                            fecha = paciente.fechaUltimoExamen,
                            estado = paciente.estado,
                            ojoEvaluado = "Bilateral (OD e OI)",
                            observacion = "Informe oftalmológico oficial para ${paciente.nombre}. Alertas clínicas: ${paciente.alertasClinicas}."
                        )
                    )
                )
            } else {
                emit(emptyList())
            }
        }
    }

    fun getConsultasByPaciente(pacienteRut: String): Flow<List<ConsultaMedicaEntity>> = flow {
        val result = _consultasState.value.filter { it.pacienteRut.equals(pacienteRut, ignoreCase = true) }
        if (result.isNotEmpty()) {
            emit(result)
        } else {
            val paciente = _pacientesState.value.find { it.rut.equals(pacienteRut, ignoreCase = true) || it.ficha.equals(pacienteRut, ignoreCase = true) }
            emit(
                listOf(
                    ConsultaMedicaEntity(
                        id = "CON-${pacienteRut.hashCode().absoluteValue}",
                        pacienteRut = pacienteRut,
                        medicoNombre = "Dr. Roberto Silva Morales",
                        fechaHora = paciente?.proximoControl ?: "Próximo control programado",
                        tipoAtencion = "Control Oftalmológico de Seguimiento",
                        motivoConsulta = paciente?.alertasClinicas ?: "Evaluación clínica general",
                        diagnostico = paciente?.estado ?: "Estudio en curso",
                        indicacionesReceta = "Mantener controles según pauta médica y uso de colirios indicados."
                    )
                )
            )
        }
    }

    fun getExamenesByPacienteFlow(rutOrNombre: String): Flow<List<ExamenEntity>> = flow {
        val result = _examenesState.value.filter {
            it.pacienteRut.equals(rutOrNombre, ignoreCase = true) ||
                    it.pacientNameIfAny(rutOrNombre)
        }
        emit(result)
    }

    private fun ExamenEntity.pacientNameIfAny(query: String): Boolean {
        return pacienteNombre.contains(query, ignoreCase = true)
    }

    fun getExamenById(id: String): Flow<ExamenEntity?> = flow {
        val found = _examenesState.value.find { it.id.equals(id, ignoreCase = true) }
        if (found != null) {
            emit(found)
        } else {
            val examenGenerado = ExamenEntity(
                id = id,
                pacienteRut = "11.392.404-5",
                pacienteNombre = "Paciente Clínico Ocularis",
                tipoExamen = "Examen Oftalmológico Especializado",
                fecha = "Octubre 2024",
                estado = "Validado",
                ojoEvaluado = "Bilateral (OD e OI)",
                observacion = "Estudio clínico oftalmológico validado correctamente en sistema Ocularis."
            )
            emit(examenGenerado)
        }
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

    suspend fun findUsuario(emailOrRut: String, password: String): UsuarioEntity? {
        // Implementation
        return null
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
