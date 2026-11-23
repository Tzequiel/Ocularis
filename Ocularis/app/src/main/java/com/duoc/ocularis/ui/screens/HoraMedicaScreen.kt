package com.duoc.ocularis.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.duoc.ocularis.data.entity.ConsultaMedicaEntity
import com.duoc.ocularis.ui.theme.*
import com.duoc.ocularis.viewmodel.AppViewModel
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraMedicaScreen(
    pacienteId: String = "OC-8921",
    vm: AppViewModel? = null,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetallePaciente: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // Obtener datos del paciente desde Room DB
    val pacienteFlow = vm?.getPacienteFlow(pacienteId)
    val paciente by (pacienteFlow ?: flowOf(null)).collectAsState(initial = null)

    var observacionesConsulta by remember { mutableStateOf("") }
    var diagnosticoConsulta by remember { mutableStateOf("Control oftalmológico en curso") }
    var isSaving by remember { mutableStateOf(false) }

    val nombrePaciente = paciente?.nombre ?: "Paciente en Consulta"
    val rutPaciente = paciente?.rut ?: pacienteId
    val fichaPaciente = paciente?.ficha ?: pacienteId
    val previsionPaciente = paciente?.prevision ?: "Fonasa B"
    val alertasClinicas = paciente?.alertasClinicas ?: "Sin alertas registradas"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Consulta Médica Activa",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "Expediente $fichaPaciente",
                            fontSize = 12.sp,
                            color = GrayText
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = DarkTeal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de Información del Paciente
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = LightCyan,
                                modifier = Modifier.size(50.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = DarkTeal,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = nombrePaciente,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "RUT: $rutPaciente • $previsionPaciente",
                                    fontSize = 12.sp,
                                    color = GrayText
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = BackgroundColor)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = DarkPinkText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Alertas: $alertasClinicas",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkPinkText
                        )
                    }

                    // Botón para acceder al Expediente Completo
                    OutlinedButton(
                        onClick = { onNavigateToDetallePaciente(rutPaciente) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTeal),
                        border = BorderStroke(1.dp, DarkTeal.copy(alpha = 0.5f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ver Expediente Clínico Completo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // Tarjeta de Registro de Atención y Diagnóstico
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Registro de Atención Médica",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal
                    )

                    OutlinedTextField(
                        value = diagnosticoConsulta,
                        onValueChange = { diagnosticoConsulta = it },
                        label = { Text("Diagnóstico / Motivo de Consulta") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = observacionesConsulta,
                        onValueChange = { observacionesConsulta = it },
                        label = { Text("Observaciones y Hallazgos Clínicos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Botón de Finalizar Atención (Cambio de Estado en la BD)
            Button(
                onClick = {
                    if (paciente != null && vm != null) {
                        isSaving = true
                        val fechaActual = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault()).format(Date())
                        val pacienteActualizado = paciente!!.copy(
                            estado = "Estudio Completado",
                            ultimoExamen = "Atención Realizada — $diagnosticoConsulta",
                            fechaUltimoExamen = fechaActual.take(11)
                        )
                        val nuevaConsulta = ConsultaMedicaEntity(
                            id = "CON-${System.currentTimeMillis().toString().takeLast(6)}",
                            pacienteRut = rutPaciente,
                            medicoNombre = vm.usuario,
                            fechaHora = fechaActual,
                            tipoAtencion = "Consulta Oftalmológica",
                            motivoConsulta = diagnosticoConsulta,
                            diagnostico = if (observacionesConsulta.isNotBlank()) observacionesConsulta else diagnosticoConsulta,
                            indicacionesReceta = "Emitida en consulta médica Ocularis"
                        )

                        vm.guardarPaciente(pacienteActualizado) {
                            vm.registrarConsultaYExamen(nuevaConsulta, null) {
                                isSaving = false
                                Toast.makeText(
                                    context,
                                    "¡Atención finalizada! Paciente marcado como atendido.",
                                    Toast.LENGTH_LONG
                                ).show()
                                onNavigateBack()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Atención finalizada con éxito", Toast.LENGTH_SHORT).show()
                        onNavigateBack()
                    }
                },
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Finalizando...", color = Color.White, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Finalizar Atención", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
