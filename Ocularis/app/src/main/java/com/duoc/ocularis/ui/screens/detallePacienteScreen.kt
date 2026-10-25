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
private val WarningOrange = Color(0xFFD97706)
private val WarningOrangeBg = Color(0xFFFEF3C7)

@Composable
fun DetallePacienteScreen(
    pacienteId: String = "OC-8921",
    onNavigateBack: () -> Unit = {},
    onVerListaExamenes: () -> Unit = {}
) {
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
                        text = "Detalle del Paciente",
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
            item { PacienteHeaderCard(pacienteId = pacienteId) }

            // Sección 2: Resumen y Alertas Oftalmológicas
            item { ResumenAlertasCard() }

            // Sección 3: Exámenes del Paciente en Base de Datos (Pantalla 5 actual)
            item { ExamenesBaseDatosCard(onVerListaExamenes = onVerListaExamenes) }

            // Sección 4: Historial de Consultas
            item { HistorialConsultasCard() }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun PacienteHeaderCard(pacienteId: String = "OC-8921") {
    val fichaTexto = if (pacienteId.startsWith("#")) pacienteId else "#$pacienteId"
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
                            text = "VM",
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
                                    text = "Paciente Activo",
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
                                text = "Ficha $fichaTexto",
                                color = SubtitleText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Valentina Morales Soto",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    Text(
                        text = "RUT: 18.421.902-3",
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
                            text = "28 años",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "14/03/1996",
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
                            text = "Previsión Médica",
                            fontSize = 11.sp,
                            color = SubtitleText,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Fonasa",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Tramo B",
                            fontSize = 11.sp,
                            color = DarkTeal,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Datos de contacto (Teléfono y Correo)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = SoftCardBg
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+56 9 8765 4321",
                            fontSize = 13.sp,
                            color = DarkText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "valentina.morales@email.com",
                            fontSize = 13.sp,
                            color = DarkText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current
            val numeroTelefono = "+56987654321"

            // Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val intentLlamada = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$numeroTelefono"))
                        context.startActivity(intentLlamada)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DarkTeal
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkTeal.copy(alpha = 0.3f)),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Llamar", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = {
                        val intentSms = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$numeroTelefono")).apply {
                            putExtra("sms_body", "Estimada Valentina, le escribimos desde la Clínica Ocularis...")
                        }
                        context.startActivity(intentSms)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DarkTeal
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkTeal.copy(alpha = 0.3f)),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Message,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Mensaje", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { },
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkTeal,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Nueva ...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun ResumenAlertasCard() {
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
                text = "Diagnósticos / Hallazgos Clínicos:",
                fontSize = 12.sp,
                color = SubtitleText,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de Diagnósticos
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Item 1
                Surface(
                    shape = RoundedCornerShape(50),
                    color = SoftCardBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(DarkTeal)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Miopía bilateral (-2.25 D)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText
                        )
                    }
                }

                // Item 2: Alerta
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
                            text = "Sospecha de Glaucoma en estudio",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WarningOrange
                        )
                    }
                }

                // Item 3
                Surface(
                    shape = RoundedCornerShape(50),
                    color = SoftCardBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = GreenActive,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Córneas transparentes",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkText
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
                            text = "22 de Noviembre, 2024",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "(Control PIO semestral)",
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
            // Encabezado
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
                        text = "Base de Datos Clinica Ocularis • 4 exámenes sincronizados",
                        fontSize = 11.sp,
                        color = SubtitleText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Banner Intermedio / Botón a Pantalla 5.5 (Lista de Exámenes)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onVerListaExamenes() },
                shape = RoundedCornerShape(16.dp),
                color = DarkTeal
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
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.FolderZip,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Ver Lista Completa de Exámenes (4)",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Descargar PDF, DICOM, Excel y análisis",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Exámenes recientes indexados:",
                fontSize = 12.sp,
                color = SubtitleText,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Examen Recent 1
            ExamenRecentItem(
                icon = Icons.Default.Science,
                titulo = "OCT Macular & Nervio Óptico",
                estado = "Validado",
                detalles = "14 Ago 2024 - Bilateral (OU) - PDF &..."
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Examen Recent 2
            ExamenRecentItem(
                icon = Icons.Default.Visibility,
                titulo = "Campimetría Humphrey 24-2",
                estado = "Validado",
                detalles = "14 Ago 2024 - Bilateral (OU) - Infor..."
            )
        }
    }
}

@Composable
fun ExamenRecentItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    estado: String,
    detalles: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SoftCardBg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = LightCyan,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = titulo,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = GreenActiveBg
                        ) {
                            Text(
                                text = estado,
                                fontSize = 10.sp,
                                color = GreenActive,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = detalles,
                        fontSize = 11.sp,
                        color = SubtitleText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = { }) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Descargar",
                            tint = DarkTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
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
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Historial de Consultas (3)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                Text(
                    text = "Ver todas",
                    fontSize = 12.sp,
                    color = DarkTeal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timeline Items
            TimelineConsultaItem(
                fecha = "22 Oct 2024",
                tipo = "Control",
                titulo = "Consulta de Seguimiento Especialidad",
                doctor = "Dr. Roberto Silva M.",
                isLast = false
            )

            TimelineConsultaItem(
                fecha = "14 Ago 2024",
                tipo = "Exámenes",
                titulo = "Examen de Campo Visual y Evaluación Glaucoma",
                doctor = "Dra. Camila Navarro",
                isLast = false
            )

            TimelineConsultaItem(
                fecha = "10 May 2024",
                tipo = "Preventivo",
                titulo = "Control Preventivo Anual",
                doctor = "Dr. Roberto Silva M.",
                isLast = true
            )
        }
    }
}

@Composable
fun TimelineConsultaItem(
    fecha: String,
    tipo: String,
    titulo: String,
    doctor: String,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Línea y nodo de la línea de tiempo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(DarkTeal)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(70.dp)
                        .background(DarkTeal.copy(alpha = 0.2f))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Contenido de la consulta
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SoftCardBg,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (isLast) 0.dp else 12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fecha,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal
                    )

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White
                    ) {
                        Text(
                            text = tipo,
                            fontSize = 10.sp,
                            color = SubtitleText,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = titulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = SubtitleText,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = doctor,
                        fontSize = 11.sp,
                        color = SubtitleText
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetallePacienteScreenPreview() {
    OcularisTheme {
        DetallePacienteScreen()
    }
}
