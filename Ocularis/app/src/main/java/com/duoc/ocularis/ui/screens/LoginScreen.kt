package com.duoc.ocularis.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.R
import com.duoc.ocularis.model.RolUsuario
import com.duoc.ocularis.viewmodel.AppViewModel

// Paleta de Colores solicitada
private val DarkTeal = Color(0xFF147376)
private val LightCyan = Color(0xFFD9F8F7)
private val GrayText = Color(0xFF666666)
private val BackgroundColor = Color(0xFFFAFAFA)

@Composable
fun LoginScreen(
    onLoginAdmin: () -> Unit,
    onLoginEspecialista: () -> Unit,
    onLoginPaciente: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo Ocularis
            Surface(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(28.dp)),
                color = LightCyan
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Ocularis",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Etiqueta superior
            Surface(
                shape = RoundedCornerShape(50),
                color = LightCyan
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = DarkTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PORTAL OCULARIS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Título principal
            Text(
                text = "Ocularis",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTeal
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sistema de Gestión Oftalmológica\nSeleccione su perfil para ingresar",
                fontSize = 15.sp,
                color = GrayText,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Tarjeta de Ingreso Rápido con 3 Botones Grandes
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Acceso Rápido por Rol",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    // 1. Botón Admin
                    BotonIngresoRol(
                        texto = "Entrar como Admin",
                        subtexto = "Gestión global y métricas del sistema",
                        icono = Icons.Default.AdminPanelSettings,
                        containerColor = DarkTeal,
                        contentColor = Color.White,
                        onClick = onLoginAdmin
                    )

                    // 2. Botón Especialista
                    BotonIngresoRol(
                        texto = "Entrar como Especialista",
                        subtexto = "Atención médica, fichas y exámenes",
                        icono = Icons.Default.MedicalServices,
                        containerColor = LightCyan,
                        contentColor = DarkTeal,
                        onClick = onLoginEspecialista
                    )

                    // 3. Botón Paciente
                    BotonIngresoRol(
                        texto = "Entrar como Paciente",
                        subtexto = "Consulta de citas y resultados de exámenes",
                        icono = Icons.Default.Person,
                        containerColor = BackgroundColor,
                        contentColor = DarkTeal,
                        borderColor = DarkTeal.copy(alpha = 0.3f),
                        onClick = onLoginPaciente
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Pie de página
            Text(
                text = "Ocularis Health Tech v1.0",
                fontSize = 12.sp,
                color = GrayText
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BotonIngresoRol(
    texto: String,
    subtexto: String,
    icono: ImageVector,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = borderColor?.let { BorderStroke(1.dp, it) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(12.dp),
                color = contentColor.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = texto,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = subtexto,
                    fontSize = 11.sp,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// Overload para mantener compatibilidad si se le pasa onLogin o AppViewModel
@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    vm: AppViewModel? = null
) {
    LoginScreen(
        onLoginAdmin = {
            vm?.seleccionarRol(RolUsuario.ADMIN)
            onLogin()
        },
        onLoginEspecialista = {
            vm?.seleccionarRol(RolUsuario.ESPECIALISTA)
            onLogin()
        },
        onLoginPaciente = {
            vm?.seleccionarRol(RolUsuario.PACIENTE)
            onLogin()
        }
    )
}
