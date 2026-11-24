package com.duoc.ocularis.data

import android.content.Context
import com.duoc.ocularis.data.entity.*
import com.duoc.ocularis.data.remote.InsForgeClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OcularisRepository(context: Context) {
    private val database = OcularisDatabase.getDatabase(context)
    private val pacienteDao = database.pacienteDao()
    private val examenDao = database.examenDao()
    private val consultaDao = database.consultaMedicaDao()
    private val especialistaDao = database.especialistaDao()
    private val usuarioDao = database.usuarioDao()

    val allPacientes: Flow<List<PacienteEntity>> = pacienteDao.getAllPacientes()
    val allExamenes: Flow<List<ExamenEntity>> = examenDao.getAllExamenes()
    val allConsultas: Flow<List<ConsultaMedicaEntity>> = consultaDao.getAllConsultas()
    val allEspecialistas: Flow<List<EspecialistaEntity>> = especialistaDao.getAllEspecialistas()
    val allUsuarios: Flow<List<UsuarioEntity>> = usuarioDao.getAllUsuarios()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            // Asegurar que si Room está vacío, se pueble inmediatamente con los datos locales (Script 3.sql)
            val existingPacientes = pacienteDao.getAllPacientes().first()
            if (existingPacientes.isEmpty()) {
                poblarBaseDeDatosLocal()
            }
            // Intentar sincronizar con InsForge en segundo plano
            cargarDatosInsForge()
        }
    }

    private suspend fun poblarBaseDeDatosLocal() {
        val seedPacientes = listOf(
            PacienteEntity("18.421.902-3", "Valentina Morales Soto", "#OC-8921", 32, "14/03/1996", "Fonasa B", "+56 9 9123 4567", "valentina.morales@correo.cl", "Miopía bilateral (-2.25 D), Sospecha de Glaucoma", "22 de Noviembre, 2024", "OCT Macular y Fibras Nerviosas", "22 Oct 2024", "Estudio Completado"),
            PacienteEntity("15.782.110-K", "Carlos Mendoza", "#OC-8890", 45, "10/08/1979", "Isapre Banmedica", "+56 9 8222 3344", "carlos.mendoza@correo.cl", "Hipertensión ocular leve, Presión PIO 22 mmHg", "14 de Noviembre, 2024", "Tonometría", "14 Oct 2024", "Pendiente Informe"),
            PacienteEntity("20.104.551-8", "Matías Alarcón Peña", "#OC-8874", 28, "05/12/1995", "Fonasa A", "+56 9 7333 5566", "matias.alarcon@correo.cl", "Astigmatismo moderado", "12 de Noviembre, 2024", "OCT Macular", "12 Oct 2024", "Validado"),
            PacienteEntity("11.392.404-5", "Elena Rojas Villa", "#OC-8850", 54, "20/02/1970", "Fonasa C", "+56 9 6444 7788", "elena.rojas@correo.cl", "Catarata incipiente en Ojo Derecho", "08 de Diciembre, 2024", "Campo Visual", "08 Oct 2024", "Control Anual"),
            PacienteEntity("16.320.145-2", "Andrés Sepúlveda Bravo", "#OC-8841", 39, "25/06/1985", "Fonasa B", "+56 9 5555 1122", "andres.sepulveda@correo.cl", "Retinopatía diabética leve", "18 de Noviembre, 2024", "Retinografía Digital", "18 Oct 2024", "Estudio Completado"),
            PacienteEntity("19.854.312-7", "Camila Fuentes Castro", "#OC-8832", 26, "12/11/1998", "Isapre Colmena", "+56 9 4444 9988", "camila.fuentes@correo.cl", "Fatiga visual y queratitis seca", "26 de Noviembre, 2024", "OCT de Nervio Óptico", "26 Oct 2024", "En Evaluación"),
            PacienteEntity("13.654.987-1", "Gabriel Valenzuela Pino", "#OC-8820", 49, "03/04/1975", "Fonasa D", "+56 9 2222 6677", "gabriel.valenzuela@correo.cl", "Glaucoma de ángulo abierto en seguimiento", "15 de Noviembre, 2024", "Ecografía Ocular", "15 Oct 2024", "Control Anual")
        )
        for (p in seedPacientes) {
            pacienteDao.insertPaciente(p)
        }

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
        for (e in seedExamenes) {
            examenDao.insertExamen(e)
        }

        val seedConsultas = listOf(
            ConsultaMedicaEntity("CON-0001", "18.421.902-3", "Dr. Roberto Silva Morales", "Oct 22, 2024, 07:30 AM", "Control de Seguimiento", "Control preventivo de rutina y revisión de agudeza visual.", "Miopía bilateral estable con leve fatiga visual.", "Hialuronato de sodio (Gotas) 1 gota c/8 horas.")
        )
        for (c in seedConsultas) {
            consultaDao.insertConsulta(c)
        }

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
        for (u in seedUsuarios) {
            usuarioDao.insertUsuario(u)
        }
    }

    fun cargarDatosInsForge() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val respPacientes = InsForgeClient.api.getPacientesDetallados()
                if (respPacientes.isSuccessful && !respPacientes.body().isNullOrEmpty()) {
                    for (dto in respPacientes.body()!!) {
                        val paciente = PacienteEntity(
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
                        pacienteDao.insertPaciente(paciente)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPacienteByRutOrFicha(rutOrFicha: String): Flow<PacienteEntity?> {
        return pacienteDao.getPacienteByRutOrFicha(rutOrFicha)
    }

    fun getPacienteByRut(rut: String): Flow<PacienteEntity?> {
        return pacienteDao.getPacienteByRut(rut)
    }

    suspend fun findPacienteByEmail(email: String): PacienteEntity? {
        return pacienteDao.findPacienteByEmail(email)
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

    suspend fun findUsuario(emailOrRut: String): UsuarioEntity? {
        return usuarioDao.findUsuarioByEmailOrRut(emailOrRut)
    }

    suspend fun insertUsuario(usuario: UsuarioEntity) {
        usuarioDao.insertUsuario(usuario)
    }

    suspend fun deleteUsuario(emailOrRut: String) {
        usuarioDao.deleteUsuario(emailOrRut)
    }
}
