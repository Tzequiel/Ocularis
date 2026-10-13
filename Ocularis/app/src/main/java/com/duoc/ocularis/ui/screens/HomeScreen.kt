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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.R

// Paleta de colores de Ocularis
private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val LightPink = Color(0xFFFFEBEB)
private val DarkPinkText = Color(0xFFC64141)
private val GrayText = Color(0xFF666666)
private val BackgroundColor = Color(0xFFFAFAFA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToNuevaAtencion: () -> Unit,
    onNavigateToDetalle: (String) -> Unit
) {
    Scaffold(
        topBar = { HomeTopBar() },
        bottomBar = { HomeBottomNavigation() },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Nueva Atención", color = Color.White, fontWeight = FontWeight.SemiBold) },
                icon = { Icon(Icons.Default.Add, contentDescription = null, tint = Color.White) },
                onClick = onNavigateToNuevaAtencion,
                containerColor = DarkTeal,
                shape = RoundedCornerShape(16.dp)
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { SearchAndFilterSection() }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Pacientes Recientes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(14 activos)",
                            fontSize = 12.sp,
                            color = DarkTeal,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Text(text = "Ver historial", fontSize = 13.sp, color = DarkTeal)
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Datos de prueba según el prototipo
            val pacientes = listOf(
                MockPaciente("VM", "Valentina ...", "18.421.902-3", "#OC-8921", "Estudio Completado", "Ayer — Fondo de Ojo", true, Icons.Default.CalendarToday),
                MockPaciente("CM", "Carlos Mendoza", "15.782.110-K", "#OC-8890", "Pendiente Informe", "14 Oct — Tonometría", false, Icons.Default.Visibility),
                MockPaciente("MA", "Matías Alarcón Peña", "20.104.551-8", "#OC-8874", "Validado", "12 Oct — OCT Macular", true, Icons.Default.Science),
                MockPaciente("ER", "Elena Rojas Villa...", "11.392.404-5", "#OC-8850", "Control Anual", "08 Oct — Campo Visual", true, Icons.Default.GridView)
            )

            items(pacientes) { paciente ->
                PatientCard(paciente, onNavigateToDetalle)
            }

            item { StatsCard() }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun HomeTopBar() {
    Column {
        // --- Barra Principal ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkTeal)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Título a la izquierda
            Text(
                text = "Pacientes Inicio",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Sección Derecha: Únicamente Notificaciones
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notificaciones",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // --- Sub-barra de Estado con el Isotipo ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightCyan)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.isotipo),
                    contentDescription = "Isotipo Ocularis",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Consulta Oftalmológica Central",
                    color = DarkTeal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.White.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(DarkTeal)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "En línea",
                        color = DarkTeal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndFilterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightCyan.copy(alpha = 0.3f))
            .padding(vertical = 16.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(
                    text = "Buscar por nombre, RUT o Nº de fi",
                    color = GrayText,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DarkTeal) },
            trailingIcon = { Icon(Icons.Default.Tune, contentDescription = null, tint = DarkTeal) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = DarkTeal
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chips = listOf("Todos (14)" to true, "Fondo de Ojo" to false, "Tonometría" to false, "OCT" to false)
            items(chips) { (text, isSelected) ->
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) DarkTeal else LightCyan,
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = text,
                        color = if (isSelected) Color.White else DarkTeal,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PatientCard(paciente: MockPaciente, onNavigateToDetalle: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clickable { onNavigateToDetalle(paciente.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .defaultMinSize(minHeight = 120.dp)
                    .background(if (paciente.isSuccess) LightCyan else LightPink)
            )

            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Surface(
                            shape = CircleShape,
                            color = LightCyan,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(paciente.initials, color = DarkTeal, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(paciente.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                Text("RUT:\n${paciente.rut}", fontSize = 12.sp, color = GrayText, lineHeight = 16.sp)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(paciente.id, fontSize = 12.sp, color = DarkTeal)
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (paciente.isSuccess) LightCyan else LightPink
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (paciente.isSuccess) Icons.Default.CheckCircleOutline else Icons.Default.MoreHoriz,
                                contentDescription = null,
                                tint = if (paciente.isSuccess) DarkTeal else DarkPinkText,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = paciente.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (paciente.isSuccess) DarkTeal else DarkPinkText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightCyan.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(paciente.examIcon, contentDescription = null, tint = DarkTeal, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(paciente.examDetails, fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                        }
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = DarkTeal)
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightCyan)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkTeal,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.SsidChart, contentDescription = null, tint = Color.White, modifier = Modifier.padding(12.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Tasa de Exámenes Hoy", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                    Text("8 completados de 10 programados", fontSize = 12.sp, color = GrayText)
                }
            }
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(4.dp, DarkTeal),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("80%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkTeal)
                }
            }
        }
    }
}

@Composable
fun HomeBottomNavigation() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.People, contentDescription = null) },
            label = { Text("Pacientes") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkTeal,
                selectedTextColor = DarkTeal,
                indicatorColor = LightCyan
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Folder, contentDescription = null) },
            label = { Text("Historial") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
            label = { Text("Exámenes") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Perfil") },
            selected = false,
            onClick = { }
        )
    }
}

data class MockPaciente(
    val initials: String,
    val name: String,
    val rut: String,
    val id: String,
    val status: String,
    val examDetails: String,
    val isSuccess: Boolean,
    val examIcon: androidx.compose.ui.graphics.vector.ImageVector
)