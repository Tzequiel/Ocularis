package com.duoc.ocularis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.ocularis.viewmodel.SyncStatus

@Composable
fun SyncIndicator(
    status: SyncStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, contentColor, icon, text) = when (status) {
        SyncStatus.SINCRONIZADO -> Quad(
            Color(0xFFE8F5E9),
            Color(0xFF2E7D32),
            Icons.Default.CloudDone,
            "Sincronizado"
        )
        SyncStatus.SINCRONIZANDO -> Quad(
            Color(0xFFFFF3E0),
            Color(0xFFE65100),
            Icons.Default.Sync,
            "Sincronizando..."
        )
        SyncStatus.OFFLINE -> Quad(
            Color(0xFFFFEBEE),
            Color(0xFFC62828),
            Icons.Default.CloudOff,
            "Modo Offline"
        )
    }

    Box(
        modifier = modifier
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                color = contentColor,
                fontSize = 10.sp
            )
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
