package com.duoc.ocularis.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.model.RolUsuario
import com.duoc.ocularis.viewmodel.AppViewModel

// Paleta de Colores solicitada
private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val GrayText = Color(0xFF666666)
private val BackgroundColor = Color(0xFFFAFAFA)

data class UsuarioUiModel(
    val id: String,
    val nombre: String,
    val rol: RolUsuario,
    val correoOrRut: String,
    val detalle: String // Especialidad o Previsión
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionUsuariosScreen(
    onNavigateBack: () -> Unit = {},
    vm: AppViewModel? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Estado con datos de prueba realistas
    val listaUsuarios = remember {
        mutableStateListOf(
            UsuarioUiModel("U01", "Dr. Roberto Silva", RolUsuario.ESPECIALISTA, "r.silva@ocularis.cl", "Retinólogo Clinico"),
            UsuarioUiModel("U02", "Dra. María Mendoza", RolUsuario.ESPECIALISTA, "m.mendoza@ocularis.cl", "Especialista en Glaucoma"),
            UsuarioUiModel("U03", "Carmen Rosa Fernández", RolUsuario.PACIENTE, "12.456.789-0", "Paciente - Fonasa B"),
            UsuarioUiModel("U04", "Juan Pablo Morales", RolUsuario.PACIENTE, "15.890.123-K", "Paciente - Isapre Banmédica"),
            UsuarioUiModel("U05", "Dra. Sofía Alarcón", RolUsuario.ESPECIALISTA, "s.alarcon@ocularis.cl", "Córnea y Cirugía Refractiva"),
            UsuarioUiModel("U06", "Administrador Central", RolUsuario.ADMIN, "admin@ocularis.cl", "Superusuario de Sistema"),
            UsuarioUiModel("U07", "Rodrigo Tapia", RolUsuario.PACIENTE, "18.345.678-4", "Paciente - Fonasa C")
        )
    }

    var usuarioAEliminar by remember { mutableStateOf<UsuarioUiModel?>(null) }
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var filtroTexto by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf<RolUsuario?>(null) }

    // Campos para el modal de agregar usuario
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevoCorreo by remember { mutableStateOf("") }
    var nuevoDetalle by remember { mutableStateOf("") }
    var nuevoRol by remember { mutableStateOf(RolUsuario.ESPECIALISTA) }

    val usuariosFiltrados = listaUsuarios.filter { usuario ->
        val coincideTexto = usuario.nombre.contains(filtroTexto, ignoreCase = true) ||
                usuario.correoOrRut.contains(filtroTexto, ignoreCase = true)
        val coincideRol = rolSeleccionado == null || usuario.rol == rolSeleccionado
        coincideTexto && coincideRol
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Gestión de Usuarios",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTeal
                        )
                        Text(
                            text = "${listaUsuarios.size} usuarios registrados en Ocularis",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoAgregar = true },
                containerColor = DarkTeal,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Usuario")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Añadir usuario", fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = BackgroundColor,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Buscador
            OutlinedTextField(
                value = filtroTexto,
                onValueChange = { filtroTexto = it },
                placeholder = { Text("Buscar por nombre, correo o RUT...", color = GrayText, fontSize = 14.sp) },
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

            // Filtros por Rol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = rolSeleccionado == null,
                    onClick = { rolSeleccionado = null },
                    label = { Text("Todos") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkTeal,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = rolSeleccionado == RolUsuario.ESPECIALISTA,
                    onClick = {
                        rolSeleccionado = if (rolSeleccionado == RolUsuario.ESPECIALISTA) null else RolUsuario.ESPECIALISTA
                    },
                    label = { Text("Especialistas") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkTeal,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = rolSeleccionado == RolUsuario.PACIENTE,
                    onClick = {
                        rolSeleccionado = if (rolSeleccionado == RolUsuario.PACIENTE) null else RolUsuario.PACIENTE
                    },
                    label = { Text("Pacientes") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkTeal,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de Usuarios
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(usuariosFiltrados, key = { it.id }) { usuario ->
                    UsuarioItemCard(
                        usuario = usuario,
                        onDeleteClick = { usuarioAEliminar = usuario }
                    )
                }
            }
        }
    }

    // Diálogo de Confirmación para Borrar Usuario
    usuarioAEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioAEliminar = null },
            title = {
                Text(
                    text = "Eliminar Usuario",
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
            },
            text = {
                Text("¿Está seguro de que desea borrar a ${usuario.nombre} (${usuario.rol.name}) del sistema Ocularis?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        listaUsuarios.remove(usuario)
                        usuarioAEliminar = null
                        Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Eliminar", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { usuarioAEliminar = null }) {
                    Text("Cancelar", color = GrayText)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Diálogo para Añadir Usuario
    if (mostrarDialogoAgregar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAgregar = false },
            title = {
                Text(
                    text = "Añadir Nuevo Usuario",
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label = { Text("Nombre Completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nuevoCorreo,
                        onValueChange = { nuevoCorreo = it },
                        label = { Text("Correo o RUT") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nuevoDetalle,
                        onValueChange = { nuevoDetalle = it },
                        label = { Text("Especialidad o Previsión") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Seleccionar Rol:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DarkTeal)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RolUsuario.values().forEach { rol ->
                            FilterChip(
                                selected = nuevoRol == rol,
                                onClick = { nuevoRol = rol },
                                label = { Text(rol.name, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DarkTeal,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nuevoNombre.isNotBlank() && nuevoCorreo.isNotBlank()) {
                            listaUsuarios.add(
                                UsuarioUiModel(
                                    id = "U${System.currentTimeMillis().toString().takeLast(4)}",
                                    nombre = nuevoNombre.trim(),
                                    rol = nuevoRol,
                                    correoOrRut = nuevoCorreo.trim(),
                                    detalle = if (nuevoDetalle.isNotBlank()) nuevoDetalle.trim() else "Registrado recientement"
                                )
                            )
                            mostrarDialogoAgregar = false
                            nuevoNombre = ""
                            nuevoCorreo = ""
                            nuevoDetalle = ""
                            Toast.makeText(context, "Usuario añadido con éxito", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
                ) {
                    Text("Guardar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoAgregar = false }) {
                    Text("Cancelar", color = GrayText)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun UsuarioItemCard(
    usuario: UsuarioUiModel,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono según Rol
            Surface(
                shape = CircleShape,
                color = when (usuario.rol) {
                    RolUsuario.ADMIN -> DarkTeal
                    RolUsuario.ESPECIALISTA -> LightCyan
                    RolUsuario.PACIENTE -> BackgroundColor
                },
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (usuario.rol) {
                            RolUsuario.ADMIN -> Icons.Default.AdminPanelSettings
                            RolUsuario.ESPECIALISTA -> Icons.Default.MedicalServices
                            RolUsuario.PACIENTE -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = when (usuario.rol) {
                            RolUsuario.ADMIN -> Color.White
                            RolUsuario.ESPECIALISTA -> DarkTeal
                            RolUsuario.PACIENTE -> DarkTeal
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Información del usuario
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = usuario.nombre,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${usuario.correoOrRut} • ${usuario.detalle}",
                    fontSize = 12.sp,
                    color = GrayText
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Badge de Rol
                Surface(
                    shape = RoundedCornerShape(50),
                    color = when (usuario.rol) {
                        RolUsuario.ADMIN -> DarkTeal.copy(alpha = 0.15f)
                        RolUsuario.ESPECIALISTA -> LightCyan
                        RolUsuario.PACIENTE -> Color(0xFFE0E0E0)
                    }
                ) {
                    Text(
                        text = usuario.rol.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Botón de eliminar (Icono de basurero solicitada)
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar Usuario",
                    tint = Color(0xFFD32F2F)
                )
            }
        }
    }
}
