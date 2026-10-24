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
import com.duoc.ocularis.R
import com.duoc.ocularis.ui.theme.OcularisTheme

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
    onNavigateBack: () -> Unit = {},
    onNavigateToDetalleExamen: (String) -> Unit = {}
) {
    var busquedaQuery by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf("Todos (4)") }

    val listaExamenes = remember {
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
            ),
            ExamenDetallado(
                id = "EX-002",
                titulo = "Campimetría Computarizada",
                estado = "Validado",
                fecha = "14 de Agosto, 2024",
                hora = "09:15 hrs",
                ojoEvaluado = "Ojo Izquierdo (OI)",
                tecnologiaPill = "Humphrey SITA",
                icono = Icons.Default.GridView,
                archivos = listOf(
                    ArchivoDisponible("PDF Informe (2.1 MB)", Icons.Default.PictureAsPdf)
                )
            ),
            ExamenDetallado(
                id = "EX-003",
                titulo = "Retinografía Digital Alta Res.",
                estado = "Validado",
                fecha = "10 de Mayo, 2024",
                hora = "16:00 hrs",
                ojoEvaluado = "Bilateral (OD/OI)",
                tecnologiaPill = "Color 50°",
                icono = Icons.Default.CameraAlt,
                archivos = listOf(
                    ArchivoDisponible("Pack Imágenes HD (15 MB)", Icons.Default.FolderZip),
                    ArchivoDisponible("Informe PDF", Icons.Default.PictureAsPdf)
                )
            ),
            ExamenDetallado(
                id = "EX-004",
                titulo = "Paquimetría Corneal Ultrasonido",
                estado = "Validado",
                fecha = "15 de Enero, 2024",
                hora = "11:20 hrs",
                ojoEvaluado = "Ojo Derecho (OD)",
                tecnologiaPill = "Ultrasonido",
                icono = Icons.Default.Straighten,
                archivos = listOf(
                    ArchivoDisponible("Planilla Mediciones (Excel)", Icons.Default.TableChart),
                    ArchivoDisponible("PDF", Icons.Default.PictureAsPdf)
                )
            )
        )
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
                        text = "Exámenes",
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            // Encabezado del Paciente
            item {
                PacienteInfoBanner(pacienteId = pacienteId)
            }

            // Indicador de estado de sincronización PACS
            item {
                PacsSyncBanner()
            }

            // Campo de Búsqueda y Chips de Filtro
            item {
                BusquedaYFiltrosSection(
                    query = busquedaQuery,
                    onQueryChange = { busquedaQuery = it },
                    filtroSeleccionado = filtroSeleccionado,
                    onFiltroSelect = { filtroSeleccionado = it }
                )
            }

            // Sub-filtros (Estado y Orden)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Text(
                            text = "Estado: ",
                            fontSize = 12.sp,
                            color = SubtitleText
                        )
                        Text(
                            text = "Validados (4)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = DarkText,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = null,
                            tint = SubtitleText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Más recientes primero",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SubtitleText
                        )
                    }
                }
            }

            // Lista de Tarjetas de Exámenes
            items(listaExamenes) { examen ->
                ExamenCardItem(
                    examen = examen,
                    onVerDetalle = { onNavigateToDetalleExamen(examen.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun PacienteInfoBanner(pacienteId: String) {
    val fichaTexto = if (pacienteId.startsWith("#")) pacienteId else "#$pacienteId"
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Valentina Morales Soto",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = LightCyan
                ) {
                    Text(
                        text = fichaTexto,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = SubtitleText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Expediente Oftalmológico Centralizado • 4 estudios registrados",
                    fontSize = 11.sp,
                    color = SubtitleText
                )
            }
        }
    }
}

@Composable
fun PacsSyncBanner() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SoftCardBg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(GreenActive)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Conectado a PACS / Base Central Ocularis • A...",
                    fontSize = 11.sp,
                    color = DarkTeal,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Sincronizar",
                tint = DarkTeal,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun BusquedaYFiltrosSection(
    query: String,
    onQueryChange: (String) -> Unit,
    filtroSeleccionado: String,
    onFiltroSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "Buscar por tipo de examen, fecha u ojo.",
                    color = SubtitleText,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = DarkTeal
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = DarkTeal
            ),
            singleLine = true
        )

        val chips = listOf("Todos (4)", "OCT Macular", "Campimetría", "Retinografía", "Paquimetría")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chips) { chipText ->
                val isSelected = filtroSeleccionado == chipText
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) DarkTeal else LightCyan,
                    modifier = Modifier.clickable { onFiltroSelect(chipText) }
                ) {
                    Text(
                        text = chipText,
                        color = if (isSelected) Color.White else DarkTeal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExamenCardItem(
    examen: ExamenDetallado,
    onVerDetalle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado: Icono + Título + Badge Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LightCyan,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = examen.icono,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = examen.titulo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = GreenActiveBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = GreenActive,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = examen.estado,
                            fontSize = 11.sp,
                            color = GreenActive,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fecha y Hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = SubtitleText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = examen.fecha,
                    fontSize = 12.sp,
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = SubtitleText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = examen.hora,
                    fontSize = 12.sp,
                    color = SubtitleText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ojo Evaluado y Pill Tecnológico
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SoftCardBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ojo Evaluado: ",
                            fontSize = 11.sp,
                            color = SubtitleText
                        )
                        Text(
                            text = examen.ojoEvaluado,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LightCyan
                    ) {
                        Text(
                            text = examen.tecnologiaPill,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkTeal,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Archivos oficiales disponibles
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = SubtitleText,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Archivos oficiales disponibles:",
                    fontSize = 11.sp,
                    color = SubtitleText,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                examen.archivos.forEach { archivo ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = LightCyan,
                        modifier = Modifier.clickable { }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = archivo.tipoIcono,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = archivo.nombre,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTeal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Botón Ver Detalle Completo
            Button(
                onClick = onVerDetalle,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkTeal,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ver Detalle Completo",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
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
    val tipoIcono: ImageVector
)

@Preview(showBackground = true)
@Composable
fun ListaExamenesScreenPreview() {
    OcularisTheme {
        ListaExamenesScreen()
    }
}
