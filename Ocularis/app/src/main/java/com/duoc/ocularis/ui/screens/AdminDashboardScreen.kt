package com.duoc.ocularis.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.viewmodel.AppViewModel

// Paleta de Colores solicitada
private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val GrayText = Color(0xFF666666)
private val BackgroundColor = Color(0xFFFAFAFA)

data class MetricaAdmin(
    val titulo: String,
    val valor: String,
    val detalle: String,
    val icono: ImageVector,
    val colorIcono: Color = DarkTeal
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToGestionUsuarios: () -> Unit = {},
    onLogout: () -> Unit = {},
    vm: AppViewModel? = null,
    modifier: Modifier = Modifier
) {
    val metricas = listOf(
        MetricaAdmin(
            titulo = "Usuarios Activos Hoy",
            valor = "24",
            detalle = "+15% vs ayer",
            icono = Icons.Default.People
        ),
        MetricaAdmin(
            titulo = "Exámenes Realizados",
            valor = "156",
            detalle = "32 pendientes de informe",
            icono = Icons.Default.Visibility
        ),
        MetricaAdmin(
            titulo = "Consultas de Hoy",
            valor = "42",
            detalle = "8 especialidades activas",
            icono = Icons.Default.MedicalServices
        ),
        MetricaAdmin(
            titulo = "Equipos Oftalmológicos",
            valor = "8",
            detalle = "Todos en línea (OCT/Camp.)",
            icono = Icons.Default.Devices
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Ocularis Admin",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "Panel de Control y Métricas",
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
            // Banner de Bienvenida Admin
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkTeal)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Panel General de Administración",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Monitoreo en tiempo real del centro oftálmico",
                            fontSize = 13.sp,
                            color = LightCyan
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = LightCyan.copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // Sección: Métricas Principales (Tarjetas con Iconos)
            Text(
                text = "Resumen de Métricas del Sistema",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTeal
            )

            // Grid de 2x2 para las métricas
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MetricaCard(metrica = metricas[0], modifier = Modifier.weight(1f))
                    MetricaCard(metrica = metricas[1], modifier = Modifier.weight(1f))
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MetricaCard(metrica = metricas[2], modifier = Modifier.weight(1f))
                    MetricaCard(metrica = metricas[3], modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sección: Gestión Directa
            Text(
                text = "Accesos Directos de Administración",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTeal
            )

            // Tarjeta para navegar a Gestión de Usuarios
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToGestionUsuarios() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightCyan),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkTeal,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.GroupAdd,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Gestión de Usuarios",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "Administrar médicos, especialistas y pacientes registrados",
                            fontSize = 12.sp,
                            color = GrayText
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ir a Gestión",
                        tint = DarkTeal
                    )
                }
            }

            // Sección: Estado del Sistema
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Estado de Servidores & Integraciones",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Badge(containerColor = DarkTeal) {
                            Text(
                                text = "OPERATIVO",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = BackgroundColor)

                    EstadoItem(etiqueta = "Interoperabilidad HL7 / DICOM", estado = "En línea", ok = true)
                    EstadoItem(etiqueta = "Base de Datos Ocularis (Room)", estado = "Sincronizada", ok = true)
                    EstadoItem(etiqueta = "Almacenamiento de Imágenes OCT", estado = "1.4 TB Disponibles", ok = true)
                }
            }
        }
    }
}

@Composable
private fun MetricaCard(metrica: MetricaAdmin, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = LightCyan,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = metrica.icono,
                            contentDescription = null,
                            tint = metrica.colorIcono,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = metrica.valor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
            }

            Text(
                text = metrica.titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                text = metrica.detalle,
                fontSize = 11.sp,
                color = GrayText
            )
        }
    }
}

@Composable
private fun EstadoItem(etiqueta: String, estado: String, ok: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (ok) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (ok) DarkTeal else Color(0xFFD32F2F),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                color = GrayText
            )
        }
        Text(
            text = estado,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkTeal
        )
    }
}
