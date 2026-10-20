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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.R

// Paleta de colores Ocularis
private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val GrayText = Color(0xFF666666)
private val BackgroundColor = Color(0xFFFAFAFA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    onNavigateToDetalle: (String) -> Unit = {}
) {
    var categoriaSeleccionada by remember { mutableStateOf("Todos (24)") }

    Scaffold(
        topBar = { HistorialTopBar() },
        containerColor = BackgroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- Categorías / Filtros Rápidos ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val categorias = listOf(
                        "Todos (24)" to Icons.Default.Check,
                        "OCT Macular" to Icons.Default.Visibility,
                        "Campimetría" to Icons.Default.GridView
                    )
                    items(categorias) { (nombre, icono) ->
                        val isSelected = categoriaSeleccionada == nombre
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isSelected) LightCyan else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) DarkTeal else Color.LightGray.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.clickable { categoriaSeleccionada = nombre }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = icono,
                                    contentDescription = null,
                                    tint = DarkTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = nombre,
                                    color = DarkTeal,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // --- Título de Sección ---
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Exploraciones Recientes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = LightCyan
                    ) {
                        Text(
                            text = "RF07 · Trazabilidad",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // --- Lista de Exploraciones (Completa) ---
            val exploraciones = listOf(
                ExploracionItem(
                    id = "1",
                    titulo = "Tomografía de Cohere...",
                    paciente = "Valentina Morales",
                    estado = "Validado",
                    fechaHora = "22 Oct 2024 · 10:30",
                    ojo = "OD y OI (Bilateral)",
                    nota = "Capa de Fibras Nerviosas (RNFL) ok",
                    icono = Icons.Default.Layers,
                    colorEstado = LightCyan,
                    textoEstadoColor = DarkTeal
                ),
                ExploracionItem(
                    id = "2",
                    titulo = "Campimetría Compu...",
                    paciente = "Carlos Mendoza",
                    estado = "Pendiente",
                    fechaHora = "20 Oct 2024 · 15:15",
                    ojo = "Ojo Izquierdo (OI)",
                    nota = "Revisión tecnólogo médico",
                    icono = Icons.Default.CenterFocusWeak,
                    colorEstado = LightCyan,
                    textoEstadoColor = DarkTeal
                ),
                ExploracionItem(
                    id = "3",
                    titulo = "Retinografía / Fondo d...",
                    paciente = "Elena Rojas",
                    estado = "Validado",
                    fechaHora = "18 Oct 2024 · 09:00",
                    ojo = "Ojo Derecho (OD)",
                    nota = "Retina sin signos de retinopatía",
                    icono = Icons.Default.CameraAlt,
                    colorEstado = LightCyan,
                    textoEstadoColor = DarkTeal
                ),
                ExploracionItem(
                    id = "4",
                    titulo = "Paquimetría Corne...",
                    paciente = "Matías Alarcón",
                    estado = "En Validación",
                    fechaHora = "15 Oct 2024 · 16:45",
                    ojo = "Bilateral",
                    nota = "Espesor corneal central (542µm / 545µm)",
                    icono = Icons.Default.Waves,
                    colorEstado = LightCyan,
                    textoEstadoColor = DarkTeal
                )
            )

            items(exploraciones) { item ->
                ExploracionCard(item, onNavigateToDetalle)
            }

            // --- Banner de Exportar Registros ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCyan)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = DarkTeal,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FileDownload,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Exportar registros", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                                Text("Formato DICOM & PDF clínico", fontSize = 12.sp, color = GrayText)
                            }
                        }

                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text("Generar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun HistorialTopBar() {
    Column {
        // TopBar Superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkTeal)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.isotipo),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Historial de Atenciones",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = Color.White)
            }
        }

        // Sub-barra Módulo Diagnóstico
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkTeal)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("MÓDULO DIAGNÓSTICO", fontSize = 10.sp, color = LightCyan, fontWeight = FontWeight.Bold)
                    Text("Registros Clínicos", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Row {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.SwapVert, contentDescription = null, tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Banner azul interno de sincronización
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = LightCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "24 estudios sincronizados desde equipos oftalmológicos.",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ExploracionCard(item: ExploracionItem, onNavigateToDetalle: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clickable { onNavigateToDetalle(item.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila 1: Icono + Título + Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LightCyan,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(item.icono, contentDescription = null, tint = DarkTeal, modifier = Modifier.size(22.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(item.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(item.paciente, fontSize = 13.sp, color = DarkTeal, fontWeight = FontWeight.Medium)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = item.colorEstado
                ) {
                    Text(
                        text = item.estado,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = item.textoEstadoColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fila 2: Fecha / Hora y Ojo examinado
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LightCyan.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GrayText, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(item.fechaHora, fontSize = 12.sp, color = GrayText)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = DarkTeal, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(item.ojo, fontSize = 12.sp, color = DarkTeal, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fila 3: Nota y Enlace Detalle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(DarkTeal))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.nota,
                        fontSize = 12.sp,
                        color = GrayText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Detalle", fontSize = 12.sp, color = DarkTeal, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = DarkTeal, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

data class ExploracionItem(
    val id: String,
    val titulo: String,
    val paciente: String,
    val estado: String,
    val fechaHora: String,
    val ojo: String,
    val nota: String,
    val icono: ImageVector,
    val colorEstado: Color,
    val textoEstadoColor: Color
)