package com.duoc.ocularis.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.viewmodel.AppViewModel

// Paleta de Colores solicitada
private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val GrayText = Color(0xFF666666)
private val BackgroundColor = Color(0xFFFAFAFA)

data class ExamenPacienteUiModel(
    val id: String,
    val tipoExamen: String,
    val fecha: String,
    val ojoEvaluado: String,
    val estado: String, // "Disponible" o "Pendiente de informe"
    val profesional: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisExamenesScreen(
    onNavigateBack: () -> Unit = {},
    vm: AppViewModel? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Datos falsos realistas del rubro oftalmológico
    val listaExamenes = remember {
        listOf(
            ExamenPacienteUiModel(
                id = "EX-101",
                tipoExamen = "OCT Macular Alta Resolución",
                fecha = "12/10/2024",
                ojoEvaluado = "Bilateral",
                estado = "Disponible",
                profesional = "Dr. Roberto Silva"
            ),
            ExamenPacienteUiModel(
                id = "EX-102",
                tipoExamen = "Campimetría Computarizada 24-2",
                fecha = "05/10/2024",
                ojoEvaluado = "Ojo Izquierdo (OI)",
                estado = "Disponible",
                profesional = "Dra. María Mendoza"
            ),
            ExamenPacienteUiModel(
                id = "EX-103",
                tipoExamen = "Angiografía Retinal Fluoresceínica",
                fecha = "18/10/2024",
                ojoEvaluado = "Ojo Derecho (OD)",
                estado = "Pendiente de informe",
                profesional = "Dr. Roberto Silva"
            ),
            ExamenPacienteUiModel(
                id = "EX-104",
                tipoExamen = "Tonometría de Aplanación Goldmann",
                fecha = "20/09/2024",
                ojoEvaluado = "Bilateral",
                estado = "Disponible",
                profesional = "Dr. Mendoza"
            ),
            ExamenPacienteUiModel(
                id = "EX-105",
                tipoExamen = "Paquimetría Ultrasónica Corneal",
                fecha = "15/08/2024",
                ojoEvaluado = "Bilateral",
                estado = "Disponible",
                profesional = "Dra. Sofía Alarcón"
            )
        )
    }

    var filtroTexto by remember { mutableStateOf("") }
    var estadoFiltro by remember { mutableStateOf<String?>(null) }
    var examenVerPdf by remember { mutableStateOf<ExamenPacienteUiModel?>(null) }

    val examenesFiltrados = listaExamenes.filter { examen ->
        val coincideTexto = examen.tipoExamen.contains(filtroTexto, ignoreCase = true) ||
                examen.profesional.contains(filtroTexto, ignoreCase = true)
        val coincideEstado = estadoFiltro == null || examen.estado == estadoFiltro
        coincideTexto && coincideEstado
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Mis Exámenes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "Historial clínico de exploraciones oftálmicas",
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
        containerColor = BackgroundColor,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Buscador por tipo de examen
            OutlinedTextField(
                value = filtroTexto,
                onValueChange = { filtroTexto = it },
                placeholder = { Text("Buscar por examen o profesional...", color = GrayText, fontSize = 14.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = DarkTeal) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DarkTeal,
                    unfocusedBorderColor = DarkTeal.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Chips de Filtro
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = estadoFiltro == null,
                    onClick = { estadoFiltro = null },
                    label = { Text("Todos (${listaExamenes.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkTeal,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = estadoFiltro == "Disponible",
                    onClick = {
                        estadoFiltro = if (estadoFiltro == "Disponible") null else "Disponible"
                    },
                    label = { Text("Disponibles") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkTeal,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = estadoFiltro == "Pendiente de informe",
                    onClick = {
                        estadoFiltro = if (estadoFiltro == "Pendiente de informe") null else "Pendiente de informe"
                    },
                    label = { Text("Pendientes") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkTeal,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lista (LazyColumn) de Exámenes
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(examenesFiltrados, key = { it.id }) { examen ->
                    ExamenCardItem(
                        examen = examen,
                        onDescargarPdfClick = {
                            if (examen.estado == "Disponible") {
                                examenVerPdf = examen
                            } else {
                                Toast.makeText(
                                    context,
                                    "El examen se encuentra pendiente de informe médico.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }

    // Modal de Visualización/Descarga de PDF de Examen
    examenVerPdf?.let { examen ->
        AlertDialog(
            onDismissRequest = { examenVerPdf = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Informe PDF Disponible",
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = examen.tipoExamen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Fecha: ${examen.fecha} • Ojo: ${examen.ojoEvaluado}",
                        fontSize = 13.sp,
                        color = GrayText
                    )
                    Text(
                        text = "Médico informante: ${examen.profesional}",
                        fontSize = 13.sp,
                        color = GrayText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "El archivo informe_oftalmologico_${examen.id}.pdf está listo para su descarga e impresión.",
                        fontSize = 12.sp,
                        color = DarkTeal
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        examenVerPdf = null
                        Toast.makeText(
                            context,
                            "Descargando PDF: ${examen.tipoExamen}...",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Descargar PDF", color = Color.White)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { examenVerPdf = null }) {
                    Text("Cerrar", color = GrayText)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun ExamenCardItem(
    examen: ExamenPacienteUiModel,
    onDescargarPdfClick: () -> Unit
) {
    val esDisponible = examen.estado == "Disponible"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Encabezado con Tipo y Badge de Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = examen.tipoExamen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Ojo: ${examen.ojoEvaluado}",
                        fontSize = 12.sp,
                        color = GrayText
                    )
                }

                // Badge de Estado (Disponible / Pendiente)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (esDisponible) LightCyan else Color(0xFFFFF3E0)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (esDisponible) Icons.Default.Visibility else Icons.Default.HourglassTop,
                            contentDescription = null,
                            tint = if (esDisponible) DarkTeal else Color(0xFFE65100),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = examen.estado,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (esDisponible) DarkTeal else Color(0xFFE65100)
                        )
                    }
                }
            }

            HorizontalDivider(color = BackgroundColor)

            // Detalles del examen (Fecha y Profesional)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Fecha examen: ${examen.fecha}",
                        fontSize = 12.sp,
                        color = GrayText
                    )
                    Text(
                        text = "Informado por: ${examen.profesional}",
                        fontSize = 12.sp,
                        color = GrayText
                    )
                }

                // Botón/Icono para Descargar / Ver PDF (Requisito Explícito)
                if (esDisponible) {
                    Button(
                        onClick = onDescargarPdfClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LightCyan, contentColor = DarkTeal),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "PDF Examen",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ver PDF",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTeal
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onDescargarPdfClick,
                        enabled = false,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "En Informe",
                            fontSize = 12.sp,
                            color = GrayText
                        )
                    }
                }
            }
        }
    }
}
