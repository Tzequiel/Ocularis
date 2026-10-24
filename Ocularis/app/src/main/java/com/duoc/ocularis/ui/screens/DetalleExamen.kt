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
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
private val RedPdfBg = Color(0xFFFFEBEB)
private val RedPdfText = Color(0xFFC64141)

@Composable
fun DetalleExamenScreen(
    examenId: String = "EX-001",
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var mostrarModalVisor by remember { mutableStateOf(false) }

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
                        text = "Detalle Del Examen",
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
            // Sub-barra de acciones superiores (Compartir e Imprimir)
            item {
                RegistroClinicoSubBar(
                    examenId = examenId,
                    onShare = {
                        Toast.makeText(context, "Enlace seguro copiado al portapapeles", Toast.LENGTH_SHORT).show()
                    },
                    onPrint = {
                        Toast.makeText(context, "Enviando a la cola de impresión clínica...", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Banner Herramientas de Visión y Asistencia Sonora (Accesibilidad)
            item {
                HerramientasAccesibilidadCard(
                    onAudio = {
                        Toast.makeText(context, "Reproduciendo audio del informe clínico completo...", Toast.LENGTH_SHORT).show()
                    },
                    onFontMore = {
                        Toast.makeText(context, "Tamaño de letra aumentado (+A)", Toast.LENGTH_SHORT).show()
                    },
                    onContrast = {
                        Toast.makeText(context, "Modo de alto contraste activado", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Tarjeta Principal del Examen Clínico
            item {
                ExamenPrincipalCard()
            }

            // Sección Documento Oficial Asociado (PDF)
            item {
                DocumentoAsociadoCard(
                    onVisualizar = { mostrarModalVisor = true },
                    onDescargar = {
                        Toast.makeText(context, "Descargando archivo cifrado Informe_OCT_OC-8921.pdf...", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Acciones Complementarias (Notificar, Segunda Opinión)
            item {
                AccionesComplementariasSection(
                    onNotificar = {
                        Toast.makeText(context, "Notificación enviada por SMS y Correo Electrónico", Toast.LENGTH_SHORT).show()
                    },
                    onSegundaOpinion = {
                        Toast.makeText(context, "Solicitud de interconsulta remitida al panel de especialistas", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Aviso Legal y Cumplimiento de Pacientes
            item {
                AvisoCumplimientoLegal()
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    // Modal Visor Seguro Ocularis
    if (mostrarModalVisor) {
        VisorDocumentoModal(
            onDismiss = { mostrarModalVisor = false },
            onImprimir = {
                mostrarModalVisor = false
                Toast.makeText(context, "Documento enviado a impresión", Toast.LENGTH_SHORT).show()
            },
            onGuardarPdf = {
                mostrarModalVisor = false
                Toast.makeText(context, "Descarga iniciada con éxito", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun RegistroClinicoSubBar(
    examenId: String = "OC-8921",
    onShare: () -> Unit,
    onPrint: () -> Unit
) {
    val expedienteTexto = if (examenId.startsWith("#")) examenId else "#$examenId"
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
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
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "REGISTRO CLÍNICO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SubtitleText
                    )
                    Text(
                        text = "Expediente $expedienteTexto",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir",
                        tint = DarkTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onPrint, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = "Imprimir",
                        tint = DarkTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HerramientasAccesibilidadCard(
    onAudio: () -> Unit,
    onFontMore: () -> Unit,
    onContrast: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessibilityNew,
                    contentDescription = null,
                    tint = DarkTeal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Herramientas de Visión y Asistencia Sonora",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAudio,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkTeal,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Escuchar Informe Completo 🔊",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onFontMore,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTeal),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkTeal.copy(alpha = 0.3f)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TextFields,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Aumentar Letra (+A)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onContrast,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTeal),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkTeal.copy(alpha = 0.3f)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Contrast,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Modo Contraste",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ExamenPrincipalCard() {
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
            // Encabezado Nombre Examen + Código RF09
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "OCT Macular y Fibras Nerviosas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = LightCyan
                ) {
                    Text(
                        text = "RF09",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = SubtitleText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "22 de Octubre, 2024 - 10:30 hrs",
                    fontSize = 12.sp,
                    color = SubtitleText
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = SubtitleText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Sede Central Norte",
                    fontSize = 12.sp,
                    color = SubtitleText
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Estado del informe (VALIDADO Y DISPONIBLE)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = GreenActiveBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = GreenActive,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "ESTADO DEL INFORME",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenActive
                        )
                        Text(
                            text = "VALIDADO Y DISPONIBLE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "Aprobado y sellado electrónicamente con validez médica",
                            fontSize = 11.sp,
                            color = SubtitleText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Imagen / Preview del escáner clínico OCT
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkTeal)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.isotipo),
                    contentDescription = "Vista preliminar OCT",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentScale = ContentScale.Fit
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 80f
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Corte Tomográfico B-Scan HD • Macula OD/OI",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Grilla de Metadatos Clínicos
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SoftCardBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Fila 1: Paciente
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
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Paciente",
                                fontSize = 12.sp,
                                color = SubtitleText
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Valentina Morales Soto",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = "18.421.902-3",
                                fontSize = 11.sp,
                                color = SubtitleText
                            )
                        }
                    }

                    HorizontalDivider(color = LightCyan)

                    // Fila 2: Ojo Evaluado
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
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ojo Evaluado",
                                fontSize = 12.sp,
                                color = SubtitleText
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = LightCyan
                        ) {
                            Text(
                                text = "Bilateral (OD e OI)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTeal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = LightCyan)

                    // Fila 3: Tecnólogo Resp.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tecnólogo Resp.",
                                fontSize = 12.sp,
                                color = SubtitleText
                            )
                        }
                        Text(
                            text = "T.M. Andrés Castillo V.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkText
                        )
                    }

                    HorizontalDivider(color = LightCyan)

                    // Fila 4: Médico Validador
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
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Médico Validador",
                                fontSize = 12.sp,
                                color = SubtitleText
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Dr. Roberto Silva M.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = "Firma validada",
                                tint = DarkTeal,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Resumen de Resultados y Hallazgos
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notes,
                    contentDescription = null,
                    tint = DarkTeal,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Resumen de Resultados y Hallazgos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SoftCardBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Espesor retiniano macular conservado en ambos ojos. Sin evidencia de edema quístico macular ni desprendimiento seroso. Complejo de células ganglionares dentro de límites de referencia normales para el grupo etario.",
                    fontSize = 13.sp,
                    color = DarkText,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }
    }
}

@Composable
fun DocumentoAsociadoCard(
    onVisualizar: () -> Unit,
    onDescargar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Documento Oficial Asociado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = LightCyan
                ) {
                    Text(
                        text = "PDF Clínico",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                        shape = RoundedCornerShape(10.dp),
                        color = RedPdfBg,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = RedPdfText,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Informe_OCT_OC-8921.pdf",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "3.8 MB • ",
                                fontSize = 11.sp,
                                color = SubtitleText
                            )
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = DarkTeal,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Firmado Digitalmente",
                                fontSize = 11.sp,
                                color = DarkTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onVisualizar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightCyan,
                        contentColor = DarkTeal
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Visualizar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onDescargar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkTeal,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Descargar PDF",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun AccionesComplementariasSection(
    onNotificar: () -> Unit,
    onSegundaOpinion: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "ACCIONES COMPLEMENTARIAS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SubtitleText,
            modifier = Modifier.padding(start = 4.dp)
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNotificar() }
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ForwardToInbox,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Notificar al Paciente por Correo / SMS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
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
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSegundaOpinion() }
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Solicitar Segunda Opinión Médica",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
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
    }
}

@Composable
fun AvisoCumplimientoLegal() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SoftCardBg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = DarkTeal,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Documento médico legal conforme a la Ley de Deberes y Derechos de los Pacientes. Válido para interconsultas y auditoría clínica Ocularis.",
                fontSize = 11.sp,
                color = SubtitleText,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun VisorDocumentoModal(
    onDismiss: () -> Unit,
    onImprimir: () -> Unit,
    onGuardarPdf: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onImprimir,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTeal)
                ) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Imprimir", fontSize = 12.sp)
                }

                Button(
                    onClick = onGuardarPdf,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkTeal, contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Guardar PDF", fontSize = 12.sp)
                }
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Preview,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Visor Seguro Ocularis",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = DarkText
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkTeal)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.isotipo),
                        contentDescription = "Previsualización PDF",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Informe_OCT_OC-8921.pdf",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
                Text(
                    text = "Páginas 1 de 2 • Firma del Dr. Silva activa",
                    fontSize = 11.sp,
                    color = SubtitleText
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}

@Preview(showBackground = true)
@Composable
fun DetalleExamenScreenPreview() {
    OcularisTheme {
        DetalleExamenScreen()
    }
}
