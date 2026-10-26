package com.duoc.ocularis.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.ocularis.R
import com.duoc.ocularis.ui.theme.OcularisTheme
import com.duoc.ocularis.viewmodel.AppViewModel

private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val LightCyanBg = Color(0xFFEBF7F7)
private val SoftCardBg = Color(0xFFF3FAF9)
private val DarkText = Color(0xFF112929)
private val SubtitleText = Color(0xFF527173)
private val GreenActive = Color(0xFF10A37F)
private val GreenActiveBg = Color(0xFFE2F7F2)

@Composable
fun ListaExamenesScreen(
    pacienteId: String = "OC-8921",
    vm: AppViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToDetalleExamen: (String) -> Unit = {}
) {
    var busquedaQuery by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    val examenesDb by vm.getExamenesByPacienteFlow(pacienteId).collectAsState(initial = emptyList())

    val listaExamenes = if (examenesDb.isNotEmpty()) {
        examenesDb.map { ex ->
            ExamenDetallado(
                id = ex.id,
                titulo = ex.tipoExamen,
                estado = ex.estado,
                fecha = ex.fecha,
                hora = "",
                ojoEvaluado = ex.ojoEvaluado,
                tecnologiaPill = "Room DB",
                icono = Icons.Default.Science,
                archivos = listOf(
                    ArchivoDisponible("PDF Clínico (3.8 MB)", Icons.Default.PictureAsPdf),
                    ArchivoDisponible("DICOM Raw", Icons.Default.DataObject)
                )
            )
        }
    } else {
        listOf(
            ExamenDetallado(
                id = "EX-001",
                titulo = "OCT Macular y Fibras Nerviosas",
                estado = "Validado",
                fecha = "22 de Octubre, 2024",
                hora = "10:30 hrs",
                ojoEvaluado = "Ambos ojos (Bilateral OD/OI)",
                tecnologiaPill = "HD Spectral",
                icono = Icons.Default.Science,
                archivos = listOf(
                    ArchivoDisponible("PDF Clínico (3.8 MB)", Icons.Default.PictureAsPdf),
                    ArchivoDisponible("DICOM Raw", Icons.Default.DataObject)
                )
            )
        )
    }

    val examenesFiltrados = listaExamenes.filter { ex ->
        busquedaQuery.isBlank() ||
        ex.titulo.contains(busquedaQuery, ignoreCase = true) ||
        ex.id.contains(busquedaQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkTeal)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Lista de Exámenes (BD)",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.isotipo),
                            contentDescription = "Isotipo",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        },
        containerColor = LightCyanBg
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = busquedaQuery,
                    onValueChange = { busquedaQuery = it },
                    placeholder = { Text("Buscar examen en base de datos...", color = SubtitleText, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DarkTeal) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkTeal,
                        unfocusedBorderColor = LightCyan,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
                )
            }

            item {
                PacienteInfoBanner(pacienteId = pacienteId)
            }

            items(examenesFiltrados) { examen ->
                ExamenDetalladoCard(examen = examen, onVerDetalle = { onNavigateToDetalleExamen(examen.id) })
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun PacienteInfoBanner(pacienteId: String) {
    val fichaTexto = if (pacienteId.startsWith("#")) pacienteId else "#$pacienteId"
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SoftCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightCyan)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = LightCyan,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = DarkTeal)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Paciente Ficha $fichaTexto",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Text(
                    text = "Mostrando registros sincronizados en Room DB",
                    fontSize = 12.sp,
                    color = SubtitleText
                )
            }
        }
    }
}

data class ExamenDetallado(
    val id: String,
    val titulo: String,
    val estado: String,
    val fecha: String,
    val hora: String,
    val ojoEvaluado: String,
    val tecnologiaPill: String,
    val icono: ImageVector,
    val archivos: List<ArchivoDisponible>
)

data class ArchivoDisponible(
    val nombre: String,
    val icono: ImageVector
)

@Composable
fun ExamenDetalladoCard(examen: ExamenDetallado, onVerDetalle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onVerDetalle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LightCyan,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(examen.icono, contentDescription = null, tint = DarkTeal, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = examen.titulo,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${examen.fecha} · ${examen.ojoEvaluado}",
                            fontSize = 12.sp,
                            color = SubtitleText
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = GreenActiveBg
                ) {
                    Text(
                        text = examen.estado,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenActive,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = LightCyan, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    examen.archivos.take(2).forEach { archivo ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SoftCardBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(archivo.icono, contentDescription = null, tint = DarkTeal, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(archivo.nombre, fontSize = 11.sp, color = DarkText, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                IconButton(onClick = onVerDetalle, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ver Detalle",
                        tint = DarkTeal
                    )
                }
            }
        }
    }
}
