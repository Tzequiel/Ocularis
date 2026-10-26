package com.duoc.ocularis.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
private val RedLogoutBg = Color(0xFFFFEBEB)
private val RedLogoutText = Color(0xFFC64141)

@Composable
fun PerfilScreen(
    vm: AppViewModel? = null,
    onLogout: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current

    // Estados interactivos
    val doctorNombre = vm?.usuario ?: "Dr. Roberto Silva Morales"
    var estadoDisponibilidadIndex by remember { mutableIntStateOf(0) }
    val estadosDisponibilidad = listOf(
        "En Consulta / Disponible",
        "En Pabellón Quirúrgico",
        "En Pausa / No Disponible"
    )

    var notifExamenes by remember { mutableStateOf(true) }
    var accesoPacs by remember { mutableStateOf(true) }
    var contrasteClinico by remember { mutableStateOf(false) }

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
                        text = "Perfil",
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
            // 1. Tarjeta Principal del Médico
            item {
                MedicoHeaderCard(
                    nombreDoctor = doctorNombre,
                    estadoActual = estadosDisponibilidad[estadoDisponibilidadIndex],
                    onCambiarEstado = {
                        estadoDisponibilidadIndex = (estadoDisponibilidadIndex + 1) % estadosDisponibilidad.size
                        Toast.makeText(context, "Estado actualizado: ${estadosDisponibilidad[estadoDisponibilidadIndex]}", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // 2. Métricas y Estadísticas Clínicas
            item {
                MetricasClinicasSection()
            }

            // 3. Credenciales y Sede
            item {
                CredencialesYSedeCard()
            }

            // 4. Preferencias de Consulta
            item {
                PreferenciasConsultaCard(
                    notifExamenes = notifExamenes,
                    onNotifChange = { notifExamenes = it },
                    accesoPacs = accesoPacs,
                    onPacsChange = { accesoPacs = it },
                    contrasteClinico = contrasteClinico,
                    onContrasteChange = { contrasteClinico = it }
                )
            }

            // 5. Acciones de Cuenta y Seguridad
            item {
                AccionesCuentaSection(
                    onHorarios = {
                        Toast.makeText(context, "Abriendo Horarios y Agenda Quirúrgica", Toast.LENGTH_SHORT).show()
                    },
                    onSoporte = {
                        Toast.makeText(context, "Conectando con Soporte Técnico Clínico", Toast.LENGTH_SHORT).show()
                    },
                    onCerrarSesion = onLogout
                )
            }

            // Pie de página confidencial médico
            item {
                Text(
                    text = "Ocularis Care Clinical v3.4.1 · HIPAA & GDPR Compliant",
                    fontSize = 11.sp,
                    color = SubtitleText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun MedicoHeaderCard(
    nombreDoctor: String,
    estadoActual: String,
    onCambiarEstado: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar con marco e icono de verificación
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = CircleShape,
                    color = LightCyan,
                    modifier = Modifier.size(88.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = DarkTeal,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Especialista Verificado",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Datos Profesionales
            Text(
                text = nombreDoctor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Médico Cirujano Oftalmólogo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTeal,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Especialista en Retina, Vítreo y Glaucoma",
                fontSize = 12.sp,
                color = SubtitleText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Badge RCM
            Surface(
                shape = RoundedCornerShape(50),
                color = SoftCardBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RCM: 14829-4 · Sede Central Norte",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector interactivo de Disponibilidad
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SoftCardBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (estadoActual.contains("Disponible")) GreenActive else DarkTeal)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Estado actual",
                                fontSize = 10.sp,
                                color = SubtitleText
                            )
                            Text(
                                text = estadoActual,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTeal
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onCambiarEstado,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTeal),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkTeal.copy(alpha = 0.3f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = "Cambiar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricasClinicasSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Tarjeta 1
        MetricaCardItem(
            modifier = Modifier.weight(1f),
            icono = Icons.Default.PersonalInjury,
            numero = "28",
            etiqueta = "Pacientes este mes"
        )

        // Tarjeta 2
        MetricaCardItem(
            modifier = Modifier.weight(1f),
            icono = Icons.Default.FactCheck,
            numero = "142",
            etiqueta = "Exámenes validados"
        )

        // Tarjeta 3
        MetricaCardItem(
            modifier = Modifier.weight(1f),
            icono = Icons.Default.Star,
            numero = "4.9 ★",
            etiqueta = "Valoración clínica"
        )
    }
}

@Composable
fun MetricaCardItem(
    modifier: Modifier = Modifier,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    numero: String,
    etiqueta: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = LightCyan,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = numero,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = etiqueta,
                fontSize = 10.sp,
                color = SubtitleText,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun CredencialesYSedeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = DarkTeal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Credenciales y Sede",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Formación
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SoftCardBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LightCyan,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Formación Académica",
                            fontSize = 11.sp,
                            color = SubtitleText
                        )
                        Text(
                            text = "Pontificia Universidad Católica",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Fellowship Retina · Hospital Clínico",
                            fontSize = 11.sp,
                            color = SubtitleText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Firma Digital
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GreenActiveBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GreenActive,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.EnhancedEncryption,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Firma Digital Avanzada",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenActive
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = GreenActive,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = "Firma Electrónica Médica Válida",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "● Token PKI Criptográfico Activo",
                            fontSize = 11.sp,
                            color = GreenActive,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lugar de Consulta
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SoftCardBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LightCyan,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.MeetingRoom,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Lugar de Consulta",
                            fontSize = 11.sp,
                            color = SubtitleText
                        )
                        Text(
                            text = "Box 304 - Piso 3",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Edificio Oftalmológico Central",
                            fontSize = 11.sp,
                            color = SubtitleText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PreferenciasConsultaCard(
    notifExamenes: Boolean,
    onNotifChange: (Boolean) -> Unit,
    accesoPacs: Boolean,
    onPacsChange: (Boolean) -> Unit,
    contrasteClinico: Boolean,
    onContrasteChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = DarkTeal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Preferencias de Consulta",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Toggle 1
            PreferenciasToggleRow(
                icono = Icons.Default.NotificationsActive,
                titulo = "Notificación de Nuevos Exámenes",
                subtitulo = "Alertas inmediatas de campo visual y OCT",
                checked = notifExamenes,
                onCheckedChange = onNotifChange
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = LightCyan)

            // Toggle 2
            PreferenciasToggleRow(
                icono = Icons.Default.UploadFile,
                titulo = "Acceso Directo PACS / DICOM",
                subtitulo = "Visor de tomografía retinal optimizado",
                checked = accesoPacs,
                onCheckedChange = onPacsChange
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = LightCyan)

            // Toggle 3
            PreferenciasToggleRow(
                icono = Icons.Default.Contrast,
                titulo = "Contraste Clínico Diagnóstico",
                subtitulo = "Calibrado para angiografía con fluoresceína",
                checked = contrasteClinico,
                onCheckedChange = onContrasteChange
            )
        }
    }
}

@Composable
fun PreferenciasToggleRow(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LightCyan,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = titulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Text(
                    text = subtitulo,
                    fontSize = 11.sp,
                    color = SubtitleText
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = DarkTeal,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun AccionesCuentaSection(
    onHorarios: () -> Unit,
    onSoporte: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHorarios() }
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = LightCyan,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Horarios y Agenda Quirúrgica",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = SubtitleText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSoporte() }
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = LightCyan,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Soporte Técnico Clínico",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = SubtitleText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RedLogoutBg,
                contentColor = RedLogoutText
            ),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión Segura",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    OcularisTheme {
        PerfilScreen()
    }
}
