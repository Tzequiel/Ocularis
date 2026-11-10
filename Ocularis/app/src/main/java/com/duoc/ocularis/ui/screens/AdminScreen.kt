package com.duoc.ocularis.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.viewmodel.AppViewModel

private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val LightCyanBg = Color(0xFFEBF7F7)
private val SoftCardBg = Color(0xFFF3FAF9)
private val DarkText = Color(0xFF112929)
private val SubtitleText = Color(0xFF527173)

@Composable
fun AdminScreen(
    vm: AppViewModel? = null,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current

    var nuevoEmail by remember { mutableStateOf("") }
    var nuevaPassword by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var listaErroresPassword by remember { mutableStateOf<List<String>>(emptyList()) }
    var successMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkTeal)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
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
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Panel Administrador - Crear Usuario",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        containerColor = LightCyanBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = LightCyan,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    tint = DarkTeal,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Nuevo Usuario Clínico",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = "Ingrese credenciales con validación de seguridad",
                                fontSize = 12.sp,
                                color = SubtitleText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Correo / Usuario
                    Text(
                        text = "Correo o Usuario Clínico",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkTeal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = nuevoEmail,
                        onValueChange = {
                            nuevoEmail = it
                            errorMessage = ""
                            successMessage = ""
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = DarkTeal)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkTeal,
                            unfocusedBorderColor = DarkTeal.copy(alpha = 0.25f),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contraseña
                    Text(
                        text = "Contraseña",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkTeal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = nuevaPassword,
                        onValueChange = {
                            nuevaPassword = it
                            errorMessage = ""
                            listaErroresPassword = emptyList()
                            successMessage = ""
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = DarkTeal)
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = DarkTeal
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkTeal,
                            unfocusedBorderColor = DarkTeal.copy(alpha = 0.25f),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Confirmar Contraseña
                    Text(
                        text = "Confirmar Contraseña",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkTeal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = confirmarPassword,
                        onValueChange = {
                            confirmarPassword = it
                            errorMessage = ""
                            successMessage = ""
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = DarkTeal)
                        },
                        trailingIcon = {
                            IconButton(onClick = { isConfirmVisible = !isConfirmVisible }) {
                                Icon(
                                    imageVector = if (isConfirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = DarkTeal
                                )
                            }
                        },
                        visualTransformation = if (isConfirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkTeal,
                            unfocusedBorderColor = DarkTeal.copy(alpha = 0.25f),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (listaErroresPassword.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "La contraseña debe cumplir con:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                listaErroresPassword.forEach { req ->
                                    Text(
                                        text = "• $req",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }

                    if (successMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE2F7F2),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = successMessage,
                                color = Color(0xFF10A37F),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (isLoading) return@Button
                            val user = nuevoEmail.trim()
                            val pass = nuevaPassword.trim()
                            val conf = confirmarPassword.trim()
                            val errores = mutableListOf<String>()

                            if (user.isBlank() || pass.isBlank() || conf.isBlank()) {
                                errorMessage = "Por favor complete todos los campos."
                                listaErroresPassword = emptyList()
                            } else if (pass != conf) {
                                errorMessage = "Las contraseñas no coinciden."
                                listaErroresPassword = emptyList()
                            } else {
                                if (pass.length < 8) {
                                    errores.add("Tener al menos 8 caracteres (actualmente tiene ${pass.length})")
                                }
                                if (!pass.any { it.isLowerCase() }) {
                                    errores.add("Contener al menos una letra minúscula (a-z)")
                                }
                                if (!pass.any { it.isUpperCase() }) {
                                    errores.add("Contener al menos una letra mayúscula (A-Z)")
                                }
                                if (!pass.any { it.isDigit() }) {
                                    errores.add("Contener al menos un número (0-9)")
                                }
                                if (!pass.any { !it.isLetterOrDigit() }) {
                                    errores.add("Contener al menos un carácter especial (!@#$%^&*...)")
                                }

                                if (errores.isNotEmpty()) {
                                    errorMessage = ""
                                    listaErroresPassword = errores
                                } else {
                                    errorMessage = ""
                                    listaErroresPassword = emptyList()

                                    if (vm != null) {
                                        isLoading = true
                                        vm.registerUserWithInsForge(
                                            email = user,
                                            password = pass,
                                            onSuccess = { authId ->
                                                isLoading = false
                                                successMessage = "¡Usuario registrado exitosamente en InsForge! (ID Auth: $authId)"
                                                Toast.makeText(context, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                                                nuevoEmail = ""
                                                nuevaPassword = ""
                                                confirmarPassword = ""
                                            },
                                            onError = { err ->
                                                isLoading = false
                                                errorMessage = err
                                            }
                                        )
                                    } else {
                                        successMessage = "Usuario creado exitosamente en InsForge."
                                        Toast.makeText(context, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                                        nuevoEmail = ""
                                        nuevaPassword = ""
                                        confirmarPassword = ""
                                    }
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Registrando...",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        } else {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Crear Usuario",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
