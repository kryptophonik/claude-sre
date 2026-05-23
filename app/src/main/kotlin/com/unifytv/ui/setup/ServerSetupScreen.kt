package com.unifytv.ui.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.unifytv.data.MediaRepository
import com.unifytv.engine.model.MediaServerKind
import com.unifytv.engine.model.ServerConfig
import com.unifytv.ui.components.ScreenBackground
import com.unifytv.ui.theme.Cloud
import com.unifytv.ui.theme.Coral
import com.unifytv.ui.theme.Glass
import com.unifytv.ui.theme.GlassStrong
import com.unifytv.ui.theme.Teal
import com.unifytv.ui.theme.Violet
import kotlinx.coroutines.launch
import java.util.UUID

private sealed interface Status {
    data object Idle : Status
    data object Connecting : Status
    data class Success(val count: Int) : Status
    data class Error(val message: String) : Status
}

@Composable
fun ServerSetupScreen(repository: MediaRepository, onDone: () -> Unit) {
    var kind by remember { mutableStateOf(MediaServerKind.JELLYFIN) }
    var name by remember { mutableStateOf("") }
    var baseUrl by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<Status>(Status.Idle) }
    val scope = rememberCoroutineScope()

    ScreenBackground {
        Column(Modifier.fillMaxSize().padding(48.dp)) {
            Text("Connect a media server", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Import libraries from Jellyfin, Emby or Plex. Everything is merged into one channel line-up.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MediaServerKind.entries.forEach { option ->
                    KindChip(label = option.name, selected = kind == option) { kind = option }
                }
            }
            Spacer(Modifier.height(24.dp))

            Field("Display name", name, KeyboardType.Text) { name = it }
            Field("Server URL (http://host:port)", baseUrl, KeyboardType.Uri) { baseUrl = it }
            Field(
                if (kind == MediaServerKind.PLEX) "X-Plex-Token" else "API key",
                token,
                KeyboardType.Text,
            ) { token = it }
            if (kind != MediaServerKind.PLEX) {
                Field("User ID", userId, KeyboardType.Text) { userId = it }
            }

            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        status = Status.Connecting
                        val config = ServerConfig(
                            id = UUID.randomUUID().toString(),
                            kind = kind,
                            name = name.ifBlank { kind.name },
                            baseUrl = baseUrl.trim(),
                            accessToken = token.trim(),
                            userId = userId.trim().ifBlank { null },
                        )
                        scope.launch {
                            repository.connectAndImport(config)
                                .onSuccess { status = Status.Success(it) }
                                .onFailure { status = Status.Error(it.message ?: "Connection failed") }
                        }
                    },
                ) { Text("Connect & import") }

                Button(onClick = onDone) { Text("Back to guide") }
            }

            Spacer(Modifier.height(20.dp))
            when (val s = status) {
                Status.Idle -> Unit
                Status.Connecting -> Text("Connecting…", color = Teal)
                is Status.Success -> Text("Imported ${s.count} items. Channels updated.", color = Teal)
                is Status.Error -> Text(s.message, color = Coral)
            }
        }
    }
}

@Composable
private fun KindChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(label, color = if (selected) Violet else Cloud, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun Field(label: String, value: String, keyboardType: KeyboardType, onChange: (String) -> Unit) {
    Column(Modifier.padding(bottom = 14.dp).width(620.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Glass)
                .border(1.dp, GlassStrong, RoundedCornerShape(10.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Cloud),
                cursorBrush = SolidColor(Teal),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
