package com.duoc.ocularis.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.speech.tts.TextToSpeech
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.R
import com.duoc.ocularis.ui.theme.*
import com.duoc.ocularis.viewmodel.AppViewModel
import kotlinx.coroutines.flow.flowOf
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleExamenScreen(
    examenId: String = "EX-001",
    vm: AppViewModel? = null,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var mostrarModalVisor by remember { mutableStateOf(false) }
    var archivoPdfGenerado by remember { mutableStateOf<File?>(null) }
    var mostrarVisorPdfRenderer by remember { mutableStateOf(false) }

    val examenesDB by (vm?.examenes ?: flowOf(emptyList())).collectAsState(initial = emptyList())
    val examen = examenesDB.find { it.id.equals(examenId, ignoreCase = true) } ?: examenesDB.firstOrNull()

    val tituloExamen = examen?.tipoExamen ?: "Examen Oftalmológico"
    val fechaExamen = examen?.fecha ?: "Fecha no disponible"
    val estadoExamen = examen?.estado ?: "Validado"
    val ojoEval = examen?.ojoEvaluado ?: "Bilateral (OD e OI)"
    val pacienteNom = examen?.pacienteNombre ?: "Paciente Ocularis"
    val pacienteRutStr = examen?.pacienteRut ?: "15.782.110-K"
    val observacionExamen = examen?.observacion.takeIf { !it.isNullOrBlank() } ?: "Sin observaciones clínicas registradas para este estudio."
    val expedienteId = examen?.id ?: examenId

    // Generar PDF automáticamente al cargar el examen
    LaunchedEffect(expedienteId) {
        val file = generarYGuardarPdf(context, expedienteId, tituloExamen, pacienteNom, pacienteRutStr, fechaExamen, observacionExamen)
        archivoPdfGenerado = file
    }

    // Motor de Síntesis de Voz Nativo (TextToSpeech)
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = java.util.Locale.forLanguageTag("es-ES")
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Detalle Del Examen",
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
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = DarkTeal
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
            // Sub-barra de acciones superiores (Compartir e Imprimir Nativo)
            item {
                RegistroClinicoSubBar(
                    examenId = expedienteId,
                    onShare = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Informe Clínico Ocularis - Expediente #$expedienteId\n" +
                                "Examen: $tituloExamen\n" +
                                "Paciente: $pacienteNom ($pacienteRutStr)\n" +
                                "Estado: $estadoExamen\n" +
                                "Médico Validador: Dr. Roberto Silva M."
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Compartir Examen Clínico Ocularis"))
                    },
                    onTtsSpeak = {
                        val textoLectura = "Informe clínico del examen $tituloExamen para el paciente $pacienteNom, RUT $pacienteRutStr. Estado: $estadoExamen. Hallazgos: $observacionExamen"
                        tts?.speak(textoLectura, TextToSpeech.QUEUE_FLUSH, null, null)
                        Toast.makeText(context, "Leyendo informe en voz alta...", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Tarjeta de Datos Principales del Examen
            item {
                ExamenHeaderCard(
                    titulo = tituloExamen,
                    fecha = fechaExamen,
                    estado = estadoExamen,
                    ojo = ojoEval
                )
            }

            // Información del Paciente Asociado
            item {
                PacienteAsociadoCard(
                    nombre = pacienteNom,
                    rut = pacienteRutStr
                )
            }

            // Documento Oficial Asociado (PDF Real + PdfRenderer)
            item {
                DocumentoAsociadoCard(
                    onVisualizar = {
                        if (archivoPdfGenerado != null && archivoPdfGenerado!!.exists()) {
                            mostrarVisorPdfRenderer = true
                        } else {
                            val file = generarYGuardarPdf(context, expedienteId, tituloExamen, pacienteNom, pacienteRutStr, fechaExamen, observacionExamen)
                            archivoPdfGenerado = file
                            if (file != null) {
                                mostrarVisorPdfRenderer = true
                            } else {
                                Toast.makeText(context, "Error al generar el documento PDF", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onDescargar = {
                        val file = archivoPdfGenerado ?: generarYGuardarPdf(context, expedienteId, tituloExamen, pacienteNom, pacienteRutStr, fechaExamen, observacionExamen)
                        if (file != null && file.exists()) {
                            Toast.makeText(context, "PDF guardado en Descargas: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Error al descargar el PDF", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            // Acciones Complementarias
            item {
                AccionesComplementariasSection(
                    onNotificar = {
                        Toast.makeText(context, "Notificación enviada al paciente por correo y SMS.", Toast.LENGTH_SHORT).show()
                    },
                    onSegundaOpinion = {
                        Toast.makeText(context, "Expediente derivado al comité de retina y glaucoma.", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Aviso Legal y Cumplimiento Normativo
            item {
                AvisoCumplimientoLegal()
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (mostrarModalVisor) {
        VisorDocumentoModal(
            onDismiss = { mostrarModalVisor = false },
            onImprimir = {
                mostrarModalVisor = false
                val file = archivoPdfGenerado ?: generarYGuardarPdf(context, expedienteId, tituloExamen, pacienteNom, pacienteRutStr, fechaExamen, observacionExamen)
                if (file != null) {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, android.net.Uri.fromFile(file))
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Compartir o Imprimir PDF"))
                } else {
                    Toast.makeText(context, "Error al preparar documento", Toast.LENGTH_SHORT).show()
                }
            },
            onGuardarPdf = {
                mostrarModalVisor = false
                val file = archivoPdfGenerado ?: generarYGuardarPdf(context, expedienteId, tituloExamen, pacienteNom, pacienteRutStr, fechaExamen, observacionExamen)
                if (file != null) {
                    Toast.makeText(context, "PDF guardado correctamente en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error al generar el archivo", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (mostrarVisorPdfRenderer && archivoPdfGenerado != null) {
        PdfRendererDialog(
            file = archivoPdfGenerado!!,
            onDismiss = { mostrarVisorPdfRenderer = false },
            onGuardar = {
                Toast.makeText(context, "Documento guardado y disponible", Toast.LENGTH_SHORT).show()
                mostrarVisorPdfRenderer = false
            }
        )
    }
}

// Función auxiliar para generar un PDF real usando PdfDocument
fun generarYGuardarPdf(
    context: Context,
    expedienteId: String,
    titulo: String,
    paciente: String,
    rut: String,
    fecha: String,
    obs: String
): File? {
    try {
        val pdfDocument = PdfDocument()
        val paint = android.graphics.Paint().apply {
            textSize = 14f
            color = android.graphics.Color.BLACK
        }
        val titlePaint = android.graphics.Paint().apply {
            textSize = 20f
            isFakeBoldText = true
            color = android.graphics.Color.parseColor("#147376")
        }
        val subtitlePaint = android.graphics.Paint().apply {
            textSize = 12f
            color = android.graphics.Color.parseColor("#527173")
        }

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        canvas.drawText("OCULARIS - OFTALMOLOGÍA CLÍNICA", 40f, 50f, titlePaint)
        canvas.drawText("Informe Médico Oficial - Expediente #$expedienteId", 40f, 80f, subtitlePaint)
        
        canvas.drawLine(40f, 95f, 555f, 95f, android.graphics.Paint().apply { color = android.graphics.Color.parseColor("#147376"); strokeWidth = 2f })

        canvas.drawText("Paciente: $paciente", 40f, 130f, paint)
        canvas.drawText("RUT: $rut", 40f, 160f, paint)
        canvas.drawText("Examen: $titulo", 40f, 190f, paint)
        canvas.drawText("Fecha: $fecha", 40f, 220f, paint)
        canvas.drawText("Estado: Validado • Dr. Roberto Silva M.", 40f, 250f, paint)

        canvas.drawLine(40f, 270f, 555f, 270f, android.graphics.Paint().apply { color = android.graphics.Color.LTGRAY; strokeWidth = 1f })

        canvas.drawText("Resumen de Resultados y Hallazgos:", 40f, 310f, titlePaint)
        
        val yStart = 340f
        var currentY = yStart
        val words = obs.split(" ")
        var line = ""
        for (word in words) {
            val testLine = if (line.isEmpty()) word else "$line $word"
            if (paint.measureText(testLine) > 500f) {
                canvas.drawText(line, 40f, currentY, paint)
                line = word
                currentY += 22f
            } else {
                line = testLine
            }
        }
        if (line.isNotEmpty()) {
            canvas.drawText(line, 40f, currentY, paint)
        }

        canvas.drawText("Firma Electrónica Avanzada - Dr. Roberto Silva M.", 40f, 750f, subtitlePaint)
        canvas.drawText("Documento válido según Ley de Deberes y Derechos del Paciente.", 40f, 780f, subtitlePaint)

        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Informe_${expedienteId}.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()
        return file
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

// Diálogo Visor utilizando nativamente PdfRenderer
@Composable
fun PdfRendererDialog(
    file: File,
    onDismiss: () -> Unit,
    onGuardar: () -> Unit
) {
    var bitmaps by remember { mutableStateOf<List<Bitmap>>(emptyList()) }

    DisposableEffect(file) {
        try {
            val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fileDescriptor)
            val list = mutableListOf<Bitmap>()
            for (i in 0 until renderer.pageCount) {
                val page = renderer.openPage(i)
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                list.add(bitmap)
                page.close()
            }
            renderer.close()
            bitmaps = list
        } catch (e: Exception) {
            e.printStackTrace()
        }
        onDispose {}
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onGuardar,
                colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = GrayText)
            }
        },
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Visor PDF (PdfRenderer)", fontWeight = FontWeight.Bold, color = DarkTeal, fontSize = 16.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = DarkText)
                }
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                if (bitmaps.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DarkTeal)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(bitmaps.size) { index ->
                            Image(
                                bitmap = bitmaps[index].asImageBitmap(),
                                contentDescription = "Página ${index + 1}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}

@Composable
fun RegistroClinicoSubBar(
    examenId: String,
    onShare: () -> Unit,
    onTtsSpeak: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightCyan,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = DarkTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Expediente Oficial",
                        fontSize = 11.sp,
                        color = SubtitleText
                    )
                    Text(
                        text = "#$examenId",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Botón de Síntesis de Voz (TextToSpeech)
                IconButton(
                    onClick = onTtsSpeak,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(LightCyan)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Lectura por voz",
                        tint = DarkTeal,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Botón Compartir
                IconButton(
                    onClick = onShare,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(LightCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir Expediente",
                        tint = DarkTeal,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExamenHeaderCard(
    titulo: String,
    fecha: String,
    estado: String,
    ojo: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = SubtitleText,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = fecha,
                            fontSize = 12.sp,
                            color = SubtitleText
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = GreenActiveBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(GreenActive)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = estado,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenActive
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = LightCyan)
            Spacer(modifier = Modifier.height(14.dp))

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
                Text(
                    text = ojo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
    }
}

@Composable
fun PacienteAsociadoCard(
    nombre: String,
    rut: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "PACIENTE ASOCIADO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SubtitleText
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = LightCyan,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = nombre.split(" ").let { parts ->
                                if (parts.size >= 2) "${parts[0].take(1)}${parts[1].take(1)}" else nombre.take(2)
                            }.uppercase(),
                            color = DarkTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row {
                        Text(
                            text = "RUT: $rut",
                            fontSize = 12.sp,
                            color = SubtitleText
                        )
                    }
                }
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
                            text = "Informe_Clinico_Ocularis.pdf",
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
                        contentDescription = "Previsualización PDF real con PdfRenderer",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Informe_Clinico_Ocularis.pdf",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
                Text(
                    text = "Generado dinámicamente y firmado digitalmente",
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
