package com.duoc.ocularis.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.data.entity.EspecialistaEntity
import com.duoc.ocularis.viewmodel.AppViewModel

// Paleta de Colores Oficial
private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val GrayText = Color(0xFF666666)
private val BackgroundColor = Color(0xFFFAFAFA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteHomeScreen(
    nombrePaciente: String = "Carmen Rosa Fernández",
    onNavigateToMisExamenes: () -> Unit = {},
    onLogout: () -> Unit = {},
    vm: AppViewModel? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var mostrarModalSolicitud by remember { mutableStateOf(false) }

    // Estado persistente/reactivo de la Próxima Cita agendada
    var proximaCitaTipo by remember { mutableStateOf("Control de Glaucoma & OCT Macular") }
    var proximaCitaEspecialista by remember { mutableStateOf("Dr. Fernando Mendoza (Glaucomatólogo)") }
    var proximaCitaFechaHora by remember { mutableStateOf("Jueves 24 de Octubre - 10:30 hrs") }
    var proximaCitaLugar by remember { mutableStateOf("Sede Central Ocularis - Consulta 302") }
    var proximaCitaEstado by remember { mutableStateOf("CONFIRMADA") }

    // Obtener la lista de especialistas desde Room DB o lista por defecto si está vacía
    val especialistasDB by vm?.especialistas?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    
    val listaEspecialistas = remember(especialistasDB) {
        if (especialistasDB.isNotEmpty()) {
            especialistasDB
        } else {
            listOf(
                EspecialistaEntity(
                    rutOrRcm = "RCM: 19540-2",
                    nombre = "Dr. Fernando Mendoza",
                    especialidad = "Glaucomatólogo y Presión Ocular",
                    sede = "Sede Central",
                    boxConsulta = "Box 302 - Piso 3",
                    estadoDisponibilidad = "Disponible",
                    horarioAtencion = "Martes y Jueves: 09:00 - 16:00 hrs"
                ),
                EspecialistaEntity(
                    rutOrRcm = "RCM: 14829-4",
                    nombre = "Dr. Roberto Silva Morales",
                    especialidad = "Oftalmólogo - Retina y Glaucoma",
                    sede = "Sede Central Norte",
                    boxConsulta = "Box 304 - Piso 3",
                    estadoDisponibilidad = "Disponible",
                    horarioAtencion = "Lunes a Viernes: 08:30 - 13:00 hrs"
                ),
                EspecialistaEntity(
                    rutOrRcm = "RCM: 22108-9",
                    nombre = "Dra. Camila Fuenzalida",
                    especialidad = "Especialista en Córnea y Superficie Ocular",
                    sede = "Sede Providencia",
                    boxConsulta = "Box 108 - Piso 1",
                    estadoDisponibilidad = "Disponible",
                    horarioAtencion = "Lunes, Miércoles y Viernes: 14:00 - 18:30 hrs"
                ),
                EspecialistaEntity(
                    rutOrRcm = "RCM: 17890-1",
                    nombre = "Dra. Javiera Torres",
                    especialidad = "Oftalmopediatría y Estrabismo",
                    sede = "Sede Las Condes",
                    boxConsulta = "Box 205 - Piso 2",
                    estadoDisponibilidad = "Disponible",
                    horarioAtencion = "Lunes a Jueves: 10:00 - 15:00 hrs"
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Portal Paciente",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "Ocularis Oftalmología",
                            fontSize = 12.sp,
                            color = GrayText
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = DarkTeal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header de Bienvenida
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightCyan)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = DarkTeal,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "¡Hola, $nombrePaciente!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Bienvenida a tu salud visual",
                            fontSize = 13.sp,
                            color = GrayText
                        )
                    }
                }
            }

            // TARJETA DESTACADA: Próxima Cita (Actualizable dinámicamente)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkTeal),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                tint = LightCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PRÓXIMA CITA",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = LightCyan,
                                letterSpacing = 1.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = LightCyan.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = proximaCitaEstado,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = proximaCitaTipo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    HorizontalDivider(color = LightCyan.copy(alpha = 0.3f))

                    // Detalles de la cita (Especialista, Fecha, Hora)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = LightCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = proximaCitaEspecialista,
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = LightCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = proximaCitaFechaHora,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = LightCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = proximaCitaLugar,
                            fontSize = 13.sp,
                            color = LightCyan
                        )
                    }
                }
            }

            // BOTÓN GRANDE: "Solicitar nueva hora"
            Button(
                onClick = { mostrarModalSolicitud = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Solicitar nueva hora",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Tarjeta de Acceso a Mis Exámenes
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToMisExamenes() },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = LightCyan,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mis Exámenes Clínicos",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "Revisar informes de OCT, Campimetría y PDFs",
                            fontSize = 12.sp,
                            color = GrayText
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ir a Exámenes",
                        tint = DarkTeal
                    )
                }
            }

            // Consejos e Indicaciones Rápidas
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Indicaciones para su próxima cita",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                    }
                    Text(
                        text = "• Acudir con 15 minutos de anticipación.\n• En caso de dilatación pupilar, venir acompañado.\n• Traer sus lentes ópticos actuales.",
                        fontSize = 12.sp,
                        color = GrayText,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }

    // Modal para solicitar nueva hora con Selección de Especialista y Horario
    if (mostrarModalSolicitud) {
        ModalSolicitarHora(
            listaEspecialistas = listaEspecialistas,
            onDismiss = { mostrarModalSolicitud = false },
            onConfirmarCita = { tipo, especialista, fechaHora, lugar ->
                proximaCitaTipo = tipo
                proximaCitaEspecialista = "${especialista.nombre} (${especialista.especialidad})"
                proximaCitaFechaHora = fechaHora
                proximaCitaLugar = "${especialista.sede} - ${especialista.boxConsulta}"
                proximaCitaEstado = "CONFIRMADA"
                mostrarModalSolicitud = false
                Toast.makeText(
                    context,
                    "¡Hora agendada exitosamente con ${especialista.nombre} para el $fechaHora!",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalSolicitarHora(
    listaEspecialistas: List<EspecialistaEntity>,
    onDismiss: () -> Unit,
    onConfirmarCita: (tipo: String, especialista: EspecialistaEntity, fechaHora: String, lugar: String) -> Unit
) {
    var motivoSeleccionado by remember { mutableStateOf("Consulta Oftalmológica General") }
    var especialistaSeleccionado by remember { mutableStateOf(listaEspecialistas.firstOrNull() ?: listaEspecialistas[0]) }
    var fechaSeleccionada by remember { mutableStateOf("Jueves 31 de Octubre") }
    var horaSeleccionada by remember { mutableStateOf("10:30 hrs") }

    val listaMotivos = listOf(
        "Consulta Oftalmológica General",
        "Control de Glaucoma & Presión Ocular",
        "Evaluación de Retina y OCT Macular",
        "Córnea y Superficie Ocular",
        "Examen Campimetría Computarizada"
    )

    val bloquesHorarios = listOf("09:00 hrs", "10:30 hrs", "11:45 hrs", "14:30 hrs", "16:00 hrs")
    val fechasDisponibles = listOf("Mañana 25 Oct", "Lunes 28 Oct", "Martes 29 Oct", "Jueves 31 Oct", "Viernes 01 Nov")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = DarkTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Agendar Hora Médica",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal
                    )
                    Text(
                        text = "Seleccione su especialista y horario",
                        fontSize = 12.sp,
                        color = GrayText
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Motivo de Consulta
                Text(
                    text = "1. Motivo de la atención",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BackgroundColor,
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        listaMotivos.forEach { motivo ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { motivoSeleccionado = motivo }
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                            ) {
                                RadioButton(
                                    selected = motivoSeleccionado == motivo,
                                    onClick = { motivoSeleccionado = motivo },
                                    colors = RadioButtonDefaults.colors(selectedColor = DarkTeal)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = motivo,
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                // 2. Selección de Especialista disponible y Horarios de atención
                Text(
                    text = "2. Seleccionar Especialista disponible",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listaEspecialistas.forEach { especialista ->
                        val esSeleccionado = especialista.rutOrRcm == especialistaSeleccionado.rutOrRcm

                        Card(
                            onClick = { especialistaSeleccionado = especialista },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (esSeleccionado) LightCyan else Color.White
                            ),
                            border = BorderStroke(
                                width = if (esSeleccionado) 2.dp else 1.dp,
                                color = if (esSeleccionado) DarkTeal else Color.LightGray.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (esSeleccionado) DarkTeal else Color.LightGray.copy(alpha = 0.3f),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.MedicalServices,
                                                contentDescription = null,
                                                tint = if (esSeleccionado) Color.White else DarkTeal,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = especialista.nombre,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkTeal
                                        )
                                        Text(
                                            text = especialista.especialidad,
                                            fontSize = 12.sp,
                                            color = GrayText
                                        )
                                    }

                                    if (esSeleccionado) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Seleccionado",
                                            tint = DarkTeal,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Horario de atención del especialista
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = DarkTeal,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Horario: ${especialista.horarioAtencion}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = DarkTeal
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = GrayText,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${especialista.sede} (${especialista.boxConsulta})",
                                        fontSize = 11.sp,
                                        color = GrayText
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Selección de Fecha y Bloque Horario según la atención
                Text(
                    text = "3. Seleccionar Fecha y Horario disponible",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )

                // Fechas
                Text(text = "Fecha:", fontSize = 12.sp, color = GrayText)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    fechasDisponibles.take(3).forEach { fecha ->
                        FilterChip(
                            selected = fechaSeleccionada == fecha,
                            onClick = { fechaSeleccionada = fecha },
                            label = { Text(fecha, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DarkTeal,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Bloques de Hora
                Text(text = "Bloque de Hora:", fontSize = 12.sp, color = GrayText)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    bloquesHorarios.take(3).forEach { bloque ->
                        FilterChip(
                            selected = horaSeleccionada == bloque,
                            onClick = { horaSeleccionada = bloque },
                            label = { Text(bloque, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DarkTeal,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Tarjeta de Resumen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCyan.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Resumen de su cita:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "• Especialista: ${especialistaSeleccionado.nombre}",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "• Servicio: $motivoSeleccionado",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "• Fecha y Hora: $fechaSeleccionada - $horaSeleccionada",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmarCita(
                        motivoSeleccionado,
                        especialistaSeleccionado,
                        "$fechaSeleccionada - $horaSeleccionada",
                        "${especialistaSeleccionado.sede} - ${especialistaSeleccionado.boxConsulta}"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Confirmar y Agendar Hora",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar", color = GrayText)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}
