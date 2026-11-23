package com.duoc.ocularis.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.R
import com.duoc.ocularis.data.entity.ConsultaMedicaEntity
import com.duoc.ocularis.data.entity.ExamenEntity
import com.duoc.ocularis.ui.theme.*
import com.duoc.ocularis.viewmodel.AppViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtencionScreen(
    vm: AppViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onGuardarAtencion: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val pacientesDb by vm.pacientes.collectAsState()

    // --- ESTADOS INTERACTIVOS DE LA PANTALLA ---
    var busquedaPacienteQuery by remember { mutableStateOf("") }
    var rutBusqueda by remember { mutableStateOf("") }
    var pacienteNombre by remember { mutableStateOf("") }
    var pacienteFicha by remember { mutableStateOf("") }
    var pacienteEncontrado by remember { mutableStateOf(false) }
    var expandedPaciente by remember { mutableStateOf(false) }

    // Selector de Fecha
    var fechaAtencion by remember { mutableStateOf("Hoy, 24 de Octubre 2024") }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val meses = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
            fechaAtencion = "$dayOfMonth de ${meses[month]} $year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Selector de Sucursal
    var sucursalSeleccionada by remember { mutableStateOf("Clínica Ocularis - Sede Central Norte") }
    var expandedSucursal by remember { mutableStateOf(false) }
    val opcionesSucursal = listOf(
        "Clínica Ocularis - Sede Central Norte",
        "Clínica Ocularis - Sede Providencia",
        "Clínica Ocularis - Sede Viña del Mar"
    )

    // Selector de Médico
    var medicoSeleccionado by remember { mutableStateOf("Dr. Roberto Silva (Oftalmología General)") }
    var expandedMedico by remember { mutableStateOf(false) }
    val opcionesMedico = listOf(
        "Dr. Roberto Silva (Oftalmología General)",
        "Dra. Carolina Soto (Retina / Macula)",
        "Dr. Felipe Morales (Glaucoma)",
        "Dra. Andrea Ramos (Catarata)"
    )

    // Motivo de Consulta
    var motivoConsulta by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Nueva Atención",
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
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            // --- Banner "Flujo Clínico" ---
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = LightCyan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = DarkTeal.copy(alpha = 0.2f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(DarkTeal))
                                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(DarkTeal))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("FLUJO CLÍNICO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DarkTeal)
                            Text("Ingreso de Prestación Of...", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = DarkTeal.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "En Progreso",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Formulario Interactivos ---

            // 1. Buscador Autocomplete de Pacientes
            FormLabel(Icons.Default.Badge, "Buscar y Seleccionar Paciente de la BD *")
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = busquedaPacienteQuery,
                    onValueChange = { query ->
                        busquedaPacienteQuery = query
                        expandedPaciente = query.isNotBlank()
                    },
                    placeholder = { Text("Escribe nombre, RUT o ficha del paciente...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DarkTeal) },
                    trailingIcon = {
                        if (busquedaPacienteQuery.isNotBlank()) {
                            IconButton(onClick = {
                                busquedaPacienteQuery = ""
                                pacienteEncontrado = false
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = GrayText)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkTeal,
                        unfocusedBorderColor = LightCyan,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
                )

                val filteredPacientes = pacientesDb.filter { p ->
                    busquedaPacienteQuery.isBlank() ||
                    p.nombre.contains(busquedaPacienteQuery, ignoreCase = true) ||
                    p.rut.contains(busquedaPacienteQuery, ignoreCase = true) ||
                    p.ficha.contains(busquedaPacienteQuery, ignoreCase = true)
                }

                if (expandedPaciente && filteredPacientes.isNotEmpty()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 6.dp,
                        border = BorderStroke(1.dp, LightCyan)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                                .verticalScroll(rememberScrollState())
                                .padding(4.dp)
                        ) {
                            filteredPacientes.forEach { paciente ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            busquedaPacienteQuery = paciente.nombre
                                            rutBusqueda = paciente.rut
                                            pacienteNombre = paciente.nombre
                                            pacienteFicha = "Ficha: ${paciente.ficha} · ${paciente.edad} años · ${paciente.prevision}"
                                            pacienteEncontrado = true
                                            expandedPaciente = false
                                        },
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = paciente.nombre,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "RUT: ${paciente.rut} • Ficha: ${paciente.ficha}",
                                            fontSize = 12.sp,
                                            color = GrayText
                                        )
                                    }
                                }
                                HorizontalDivider(color = BackgroundColor)
                            }
                        }
                    }
                }
            }

            if (pacienteEncontrado) {
                Spacer(modifier = Modifier.height(8.dp))
                // Tarjeta de Paciente Seleccionado
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightCyan,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(pacienteNombre, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                        Text(pacienteFicha, fontSize = 12.sp, color = GrayText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Fecha de Atención (Abre Calendario)
            FormLabel(Icons.Default.CalendarToday, "Fecha de Atención *")
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { datePickerDialog.show() },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, LightCyan)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(fechaAtencion, fontSize = 15.sp, color = GrayText)
                    Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar Fecha", tint = DarkTeal)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Sucursal con Menú Desplegable
            FormLabel(Icons.Default.LocalHospital, "Sucursal Oftalmológica *")
            Box(modifier = Modifier.padding(top = 8.dp)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedSucursal = true },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, LightCyan)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(sucursalSeleccionada, fontSize = 15.sp, color = GrayText)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = GrayText)
                    }
                }

                DropdownMenu(
                    expanded = expandedSucursal,
                    onDismissRequest = { expandedSucursal = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    opcionesSucursal.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                sucursalSeleccionada = opcion
                                expandedSucursal = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Médico con Menú Desplegable
            FormLabel(Icons.Default.MedicalServices, "Médico / Oftalmólogo a cargo *")
            Box(modifier = Modifier.padding(top = 8.dp)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedMedico = true },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, LightCyan)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(medicoSeleccionado, fontSize = 15.sp, color = GrayText)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = GrayText)
                    }
                }

                DropdownMenu(
                    expanded = expandedMedico,
                    onDismissRequest = { expandedMedico = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    opcionesMedico.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                medicoSeleccionado = opcion
                                expandedMedico = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Motivo de Consulta Editable
            FormLabel(Icons.Default.Visibility, "Motivo de la consulta / Examen requerido *")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = motivoConsulta,
                onValueChange = { motivoConsulta = it },
                placeholder = {
                    Text(
                        text = "Ej. Control glaucoma, agudeza visual, campimetría computarizada...",
                        fontSize = 14.sp,
                        color = Color(0xFF999999)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkTeal,
                    unfocusedBorderColor = LightCyan,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chips rápidos interactivos
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickChip("+ Glaucoma") {
                    motivoConsulta = if (motivoConsulta.isBlank()) "Control glaucoma" else "$motivoConsulta, Control glaucoma"
                }
                QuickChip("+ Refracción") {
                    motivoConsulta = if (motivoConsulta.isBlank()) "Refracción y agudeza visual" else "$motivoConsulta, Refracción y agudeza visual"
                }
                QuickChip("+ Fondo de Ojo") {
                    motivoConsulta = if (motivoConsulta.isBlank()) "Fondo de Ojo" else "$motivoConsulta, Fondo de Ojo"
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- Botones Inferiores ---
            Button(
                onClick = {
                    if (rutBusqueda.isBlank()) {
                        Toast.makeText(context, "Por favor seleccione un paciente válido de la base de datos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val consultaId = "CON-${System.currentTimeMillis()}"
                    val examenId = "EX-${System.currentTimeMillis()}"
                    vm.registrarConsultaYExamen(
                        consulta = ConsultaMedicaEntity(
                            id = consultaId,
                            pacienteRut = rutBusqueda,
                            medicoNombre = medicoSeleccionado,
                            fechaHora = fechaAtencion,
                            tipoAtencion = "Control de Seguimiento",
                            motivoConsulta = motivoConsulta.ifBlank { "Revisión Oftalmológica General" },
                            diagnostico = "Atendido en $sucursalSeleccionada",
                            indicacionesReceta = "Prescripción médica registrada en sistema Ocularis."
                        ),
                        examen = ExamenEntity(
                            id = examenId,
                            pacienteRut = rutBusqueda,
                            pacienteNombre = pacienteNombre,
                            tipoExamen = if (motivoConsulta.isNotBlank()) motivoConsulta else "Examen General",
                            fecha = fechaAtencion,
                            estado = "Validado",
                            ojoEvaluado = "Bilateral OD/OI",
                            observacion = "Evaluación completada correctamente."
                        ),
                        onComplete = {
                            onGuardarAtencion()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Atención", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightCyan),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Cancelar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkTeal)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// Componentes reutilizables
@Composable
fun FormLabel(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DarkTeal,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DarkTeal
        )
    }
}

@Composable
fun QuickChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = LightCyan,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = DarkTeal,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
