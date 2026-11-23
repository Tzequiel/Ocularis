package com.duoc.ocularis.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
fun HistorialScreen(
    vm: AppViewModel = viewModel(),
    onNavigateToDetalle: (String) -> Unit = {}
) {
    val pacientesDb by vm.pacientes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredPacientes = pacientesDb.filter { p ->
        searchQuery.isBlank() ||
        p.nombre.contains(searchQuery, ignoreCase = true) ||
        p.rut.contains(searchQuery, ignoreCase = true) ||
        p.ficha.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Directorio de Pacientes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "${filteredPacientes.size} pacientes registrados en Ocularis",
                            fontSize = 12.sp,
                            color = GrayText
                        )
                    }
                },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.isotipo),
                        contentDescription = "Isotipo",
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Barra de Búsqueda Funcional
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar paciente por nombre, RUT o ficha...", color = GrayText, fontSize = 14.sp) },
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

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredPacientes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron pacientes en la base de datos.",
                        fontSize = 14.sp,
                        color = GrayText
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredPacientes, key = { it.rut }) { paciente ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToDetalle(paciente.rut) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, DarkTeal.copy(alpha = 0.2f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = LightCyan,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        val initials = paciente.nombre.split(" ").let { parts ->
                                            if (parts.size >= 2) "${parts[0].take(1)}${parts[1].take(1)}" else paciente.nombre.take(2)
                                        }.uppercase()
                                        Text(
                                            text = initials,
                                            color = DarkTeal,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = paciente.nombre,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "RUT: ${paciente.rut} • Ficha: ${paciente.ficha}",
                                        fontSize = 12.sp,
                                        color = GrayText
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Previsión: ${paciente.prevision} • Último Examen: ${paciente.ultimoExamen}",
                                        fontSize = 11.sp,
                                        color = DarkTeal
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Ver Detalle",
                                    tint = DarkTeal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
