package com.duoc.ocularis.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.ocularis.R
import com.duoc.ocularis.ui.theme.*
import com.duoc.ocularis.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: AppViewModel = viewModel(),
    onNavigateToNuevaAtencion: () -> Unit,
    onNavigateToDetalle: (String) -> Unit
) {
    val pacientesDb by vm.pacientes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var mostrarSoloPendientes by remember { mutableStateOf(true) }

    val filteredPacientesDb = pacientesDb.filter { p ->
        val coincideBusqueda = searchQuery.isBlank() ||
            p.nombre.contains(searchQuery, ignoreCase = true) ||
            p.rut.contains(searchQuery, ignoreCase = true) ||
            p.ficha.contains(searchQuery, ignoreCase = true)

        val esPendienteOEnCurso = !p.estado.contains("Completado", ignoreCase = true) &&
                                  !p.estado.contains("Validado", ignoreCase = true)

        if (mostrarSoloPendientes) {
            coincideBusqueda && esPendienteOEnCurso
        } else {
            coincideBusqueda
        }
    }

    Scaffold(
        topBar = { HomeTopBar() },
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
            item {
                SearchAndFilterSection(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = mostrarSoloPendientes,
                        onClick = { mostrarSoloPendientes = true },
                        label = { Text("Agenda Pendiente") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkTeal,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = !mostrarSoloPendientes,
                        onClick = { mostrarSoloPendientes = false },
                        label = { Text("Todos (${pacientesDb.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkTeal,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = if (mostrarSoloPendientes) "Agenda y Atenciones del Día" else "Todos los Pacientes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${filteredPacientesDb.size} en lista)",
                            fontSize = 12.sp,
                            color = DarkTeal,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }

            val pacientes = filteredPacientesDb.map { p ->
                val initials = p.nombre.split(" ").let { parts ->
                    if (parts.size >= 2) "${parts[0].take(1)}${parts[1].take(1)}" else p.nombre.take(2)
                }.uppercase()
                val isSuccess = p.estado.contains("Completado", ignoreCase = true) || p.estado.contains("Validado", ignoreCase = true)
                val icon = when {
                    p.ultimoExamen.contains("Fondo", ignoreCase = true) -> Icons.Default.CalendarToday
                    p.ultimoExamen.contains("Tonometría", ignoreCase = true) -> Icons.Default.Visibility
                    else -> Icons.Default.Science
                }
                MockPaciente(
                    initials = initials,
                    name = p.nombre,
                    rut = p.rut,
                    id = p.rut,
                    status = p.estado,
                    examDetails = "${p.fechaUltimoExamen} — ${p.ultimoExamen}",
                    isSuccess = isSuccess,
                    examIcon = icon
                )
            }

            items(pacientes) { paciente ->
                PatientCard(paciente, onNavigateToDetalle)
            }

            item { StatsCard() }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Agenda",
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
            Surface(
                shape = CircleShape,
                color = LightCyan,
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
            Spacer(modifier = Modifier.width(8.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun SearchAndFilterSection(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Buscar paciente por nombre o RUT...", color = GrayText, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DarkTeal) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkTeal,
                unfocusedBorderColor = LightCyan,
                focusedContainerColor = BackgroundColor,
                unfocusedContainerColor = BackgroundColor,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            singleLine = true
        )
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
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.width(6.dp).fillMaxHeight().defaultMinSize(minHeight = 120.dp).background(if (paciente.isSuccess) LightCyan else LightPink))
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row {
                        Surface(shape = CircleShape, color = LightCyan, modifier = Modifier.size(44.dp)) {
                            Box(contentAlignment = Alignment.Center) { Text(paciente.initials, color = DarkTeal, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
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
                    val shortStatus = when {
                        paciente.status.contains("Completado", ignoreCase = true) || paciente.status.contains("Validado", ignoreCase = true) -> "Completo"
                        paciente.status.contains("Pendiente", ignoreCase = true) -> "Pendiente"
                        else -> "Al día"
                    }
                    Surface(shape = RoundedCornerShape(50), color = if (paciente.isSuccess) LightCyan else LightPink) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = if (paciente.isSuccess) Icons.Default.CheckCircleOutline else Icons.Default.MoreHoriz, contentDescription = null, tint = if (paciente.isSuccess) DarkTeal else DarkPinkText, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = shortStatus, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = if (paciente.isSuccess) DarkTeal else DarkPinkText)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Surface(shape = RoundedCornerShape(12.dp), color = LightCyan.copy(alpha = 0.3f), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
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
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightCyan)
    ) {
        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(12.dp), color = DarkTeal, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Default.SsidChart, contentDescription = null, tint = Color.White, modifier = Modifier.padding(12.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Tasa de Exámenes Hoy", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                    Text("Base de Datos Room Sincronizada", fontSize = 12.sp, color = GrayText)
                }
            }
            Surface(shape = CircleShape, color = Color.Transparent, border = BorderStroke(4.dp, DarkTeal), modifier = Modifier.size(48.dp)) {
                Box(contentAlignment = Alignment.Center) { Text("100%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkTeal) }
            }
        }
    }
}

data class MockPaciente(val initials: String, val name: String, val rut: String, val id: String, val status: String, val examDetails: String, val isSuccess: Boolean, val examIcon: androidx.compose.ui.graphics.vector.ImageVector)
