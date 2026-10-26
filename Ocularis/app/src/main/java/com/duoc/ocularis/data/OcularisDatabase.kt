package com.duoc.ocularis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.duoc.ocularis.data.dao.*
import com.duoc.ocularis.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UsuarioEntity::class,
        ConsultaMedicaEntity::class,
        ExamenEntity::class,
        PacienteEntity::class,
        EspecialistaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class OcularisDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun consultaMedicaDao(): ConsultaMedicaDao
    abstract fun examenDao(): ExamenDao
    abstract fun pacienteDao(): PacienteDao
    abstract fun especialistaDao(): EspecialistaDao

    companion object {
        @Volatile
        private var INSTANCE: OcularisDatabase? = null

        fun getDatabase(context: Context): OcularisDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OcularisDatabase::class.java,
                    "ocularis_database"
                )
                    .addCallback(OcularisDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class OcularisDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }
        }

        suspend fun populateDatabase(database: OcularisDatabase) {
            val pacienteDao = database.pacienteDao()
            val examenDao = database.examenDao()
            val consultaDao = database.consultaMedicaDao()
            val especialistaDao = database.especialistaDao()
            val usuarioDao = database.usuarioDao()

            // Seed Pacientes
            val pacientes = listOf(
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
                )
            )

            for (p in pacientes) {
                pacienteDao.insertPaciente(p)
            }

            // Seed Examenes
            val examenes = listOf(
                ExamenEntity(
                    id = "EX-001",
                    pacienteRut = "18.421.902-3",
                    pacienteNombre = "Valentina Morales Soto",
                    tipoExamen = "OCT Macular y Fibras Nerviosas",
                    fecha = "22 Oct 2024 - 10:30",
                    estado = "Validado",
                    ojoEvaluado = "Bilateral (OD e OI)",
                    observacion = "Estructura macular normal, sin edema. Capa de fibras nerviosas conservada."
                ),
                ExamenEntity(
                    id = "EX-002",
                    pacienteRut = "15.782.110-K",
                    pacienteNombre = "Carlos Mendoza",
                    tipoExamen = "Campimetría Computarizada",
                    fecha = "20 Oct 2024 - 15:15",
                    estado = "Pendiente",
                    ojoEvaluado = "Ojo Izquierdo (OI)",
                    observacion = "Evaluación de campo visual periférico en curso."
                )
            )

            for (e in examenes) {
                examenDao.insertExamen(e)
            }

            // Seed Consultas Medicas
            val consultas = listOf(
                ConsultaMedicaEntity(
                    id = "CON-2024-001",
                    pacienteRut = "18.421.902-3",
                    medicoNombre = "Dr. Roberto Silva Morales",
                    fechaHora = "22 Oct 2024 - 10:30",
                    tipoAtencion = "Control de Seguimiento",
                    motivoConsulta = "Control preventivo de rutina y revisión de agudeza visual.",
                    diagnostico = "Miopía bilateral estable con leve fatiga visual.",
                    indicacionesReceta = "Lentes de descanso para uso prolongado frente a pantallas. Gotas lubricantes (Hialuronato de sodio) 1 gota c/8 horas."
                )
            )

            for (c in consultas) {
                consultaDao.insertConsulta(c)
            }

            // Seed Especialistas
            val especialista = EspecialistaEntity(
                rutOrRcm = "RCM: 14829-4",
                nombre = "Dr. Roberto Silva Morales",
                especialidad = "Oftalmólogo - Retina y Glaucoma",
                sede = "Sede Central Norte",
                boxConsulta = "Box 304 - Piso 3",
                estadoDisponibilidad = "En Consulta / Disponible"
            )
            especialistaDao.insertEspecialista(especialista)

            // Seed Usuarios
            val usuario = UsuarioEntity(
                emailOrRut = "roberto.silva",
                passwordHash = "123456",
                rol = "ESPECIALISTA",
                referenciaId = "RCM: 14829-4"
            )
            usuarioDao.insertUsuario(usuario)
        }
    }
}
