package com.duoc.ocularis.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.ocularis.R
import com.duoc.ocularis.data.entity.PacienteEntity
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
private val WarningOrange = Color(0xFFD97706)
private val WarningOrangeBg = Color(0xFFFEF3C7)

@Composable
fun DetallePacienteScreen(
    pacienteId: String = "OC-8921",
    vm: AppViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onVerListaExamenes: () -> Unit = {}
) {
    val paciente by vm.getPacienteFlow(pacienteId).collectAsState(initial = null)

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
                        text = "Detalle del Paciente (Room DB)",
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
            // Sección 1: Datos Principales del Paciente
            item { PacienteHeaderCard(paciente = paciente, pacienteIdFallback = pacienteId) }

            // Sección 2: Resumen y Alertas Oftalmológicas
            item { ResumenAlertasCard(paciente = paciente) }

            // Sección 3: Exámenes del Paciente en Base de Datos
            item { ExamenesBaseDatosCard(onVerListaExamenes = onVerListaExamenes) }

            // Sección 4: Historial de Consultas
            item { HistorialConsultasCard() }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun PacienteHeaderCard(paciente: PacienteEntity?, pacienteIdFallback: String) {
    val nombre = paciente?.nombre ?: "Valentina Morales Soto"
    val rut = paciente?.rut ?: "18.421.902-3"
    val ficha = paciente?.ficha ?: pacienteIdFallback
    val edad = paciente?.edad ?: 32
    val nac = paciente?.fechaNacimiento ?: "14/03/1996"
    val prevision = paciente?.prevision ?: "Fonasa B"
    val tel = paciente?.telefono ?: "+56 9 9123 4567"
    val email = paciente?.email ?: "valentina.morales@correo.cl"

    val initials = nombre.split(" ").let { parts ->
        if (parts.size >= 2) "${parts[0].take(1)}${parts[1].take(1)}" else nombre.take(2)
    }.uppercase()

    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fila Superior: Avatar + Badges + Nombre + RUT
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LightCyan,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = initials,
                            color = DarkTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = GreenActiveBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(GreenActive)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Activo en BD",
                                    color = GreenActive,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = SoftCardBg
                        ) {
                            Text(
                                text = "Ficha $ficha",
                                color = SubtitleText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    Text(
                        text = "RUT: $rut",
                        fontSize = 13.sp,
                        color = SubtitleText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grilla de Edad/Nacimiento y Previsión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = SoftCardBg
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Edad & Nacimiento",
                            fontSize = 11.sp,
                            color = SubtitleText,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$edad años",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = nac,
                            fontSize = 11.sp,
                            color = SubtitleText
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = SoftCardBg
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Previsión Salud",
                            fontSize = 11.sp,
                            color = SubtitleText,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = prevision,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Validada en Sistema",
                            fontSize = 11.sp,
                            color = GreenActive
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de Contacto Rápido (Teléfono y Email)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Llamar", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTeal)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mensaje", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun ResumenAlertasCard(paciente: PacienteEntity?) {
    val alertas = paciente?.alertasClinicas ?: "Miopía bilateral (-2.25 D), Sospecha de Glaucoma"
    val proximoControl = paciente?.proximoControl ?: "22 de Noviembre, 2024"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Resumen y Alertas Oftalmológicas",
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
                        text = "Al Día",
                        fontSize = 11.sp,
                        color = DarkTeal,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Diagnósticos / Hallazgos Clínicos (Room DB):",
                fontSize = 12.sp,
                color = SubtitleText,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de Diagnósticos
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = WarningOrangeBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = WarningOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = alertas,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WarningOrange
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cuadro Próximo Control
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SoftCardBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkTeal,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Próximo Control Oftalmológico:",
                            fontSize = 11.sp,
                            color = SubtitleText,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = proximoControl,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "(Control registrado en BD)",
                            fontSize = 11.sp,
                            color = DarkTeal,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExamenesBaseDatosCard(onVerListaExamenes: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = DarkTeal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Exámenes del Paciente en Base de Datos",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "Base de Datos Clínica Ocularis • Sincronizado",
                        fontSize = 11.sp,
                        color = SubtitleText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onVerListaExamenes,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Exámenes Sincronizados", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun HistorialConsultasCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = DarkTeal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Historial de Consultas Médicas",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Registros almacenados permanentemente en Room Database.",
                fontSize = 12.sp,
                color = SubtitleText
            )
        }
    }
}
