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
import com.duoc.ocularis.ui.theme.*
import com.duoc.ocularis.viewmodel.AppViewModel
import kotlinx.coroutines.flow.flowOf

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

    // Estado reactivo desde la Base de Datos / InsForge
    val usuariosList by (vm?.usuarios ?: flowOf(emptyList())).collectAsState(initial = emptyList())

    val listaUsuarios = remember(usuariosList) {
        usuariosList.map { entity ->
            val rol = when (entity.rol.uppercase()) {
                "ADMIN" -> RolUsuario.ADMIN
                "PACIENTE" -> RolUsuario.PACIENTE
                else -> RolUsuario.ESPECIALISTA
            }
            val detalle = when (rol) {
                RolUsuario.ADMIN -> "Superusuario de Sistema"
                RolUsuario.PACIENTE -> "Paciente Registrado"
                RolUsuario.ESPECIALISTA -> "Especialista Ocularis"
            }
            UsuarioUiModel(
                id = entity.emailOrRut,
                nombre = if (rol == RolUsuario.ADMIN) "Administrador del Sistema"
                         else entity.emailOrRut.substringBefore("@").replace(".", " ").replace("_", " ")
                             .split(" ").joinToString(" ") { word -> word.replaceFirstChar { c -> c.uppercase() } },
                rol = rol,
                correoOrRut = entity.emailOrRut,
                detalle = detalle
            )
        }
    }

    var usuarioAEliminar by remember { mutableStateOf<UsuarioUiModel?>(null) }
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var filtroTexto by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf<RolUsuario?>(null) }

    // Campos para el modal de agregar usuario
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevoCorreo by remember { mutableStateOf("") }
    var nuevaPassword by remember { mutableStateOf("Ocularis2025!") }
    var nuevoDetalle by remember { mutableStateOf("") }
    var nuevoRol by remember { mutableStateOf(RolUsuario.ESPECIALISTA) }
    var isCreatingUser by remember { mutableStateOf(false) }

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
                        if (vm != null) {
                            vm.eliminarUsuario(usuario.correoOrRut) {
                                Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show()
                            }
                        }
                        usuarioAEliminar = null
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

    // Diálogo para Añadir Usuario con Integración InsForge
    if (mostrarDialogoAgregar) {
        AlertDialog(
            onDismissRequest = { if (!isCreatingUser) mostrarDialogoAgregar = false },
            title = {
                Text(
                    text = "Añadir Nuevo Usuario (InsForge)",
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
                        label = { Text("Correo Electrónico InsForge") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nuevaPassword,
                        onValueChange = { nuevaPassword = it },
                        label = { Text("Contraseña (Min. 8 car., Mayús, Minús, Núm, Espec.)") },
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
                    Text("Seleccionar Rol en la Base de Datos:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DarkTeal)
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
                        val email = nuevoCorreo.trim()
                        val nombre = nuevoNombre.trim()
                        val pass = nuevaPassword.trim()
                        if (nombre.isNotBlank() && email.isNotBlank()) {
                            if (vm != null) {
                                isCreatingUser = true
                                vm.registerUserWithInsForge(
                                    email = email,
                                    password = pass,
                                    rol = nuevoRol,
                                    onSuccess = { authId ->
                                        isCreatingUser = false
                                        mostrarDialogoAgregar = false
                                        nuevoNombre = ""
                                        nuevoCorreo = ""
                                        nuevoDetalle = ""
                                        Toast.makeText(context, "Usuario registrado en InsForge y DB local", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { err ->
                                        isCreatingUser = false
                                        Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                                    }
                                )
                            } else {
                                mostrarDialogoAgregar = false
                                nuevoNombre = ""
                                nuevoCorreo = ""
                                nuevoDetalle = ""
                                Toast.makeText(context, "Usuario añadido con éxito", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enabled = !isCreatingUser,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
                ) {
                    if (isCreatingUser) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Creando...", color = Color.White)
                    } else {
                        Text("Guardar", color = Color.White)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoAgregar = false },
                    enabled = !isCreatingUser
                ) {
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

            // Botón de eliminar
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
