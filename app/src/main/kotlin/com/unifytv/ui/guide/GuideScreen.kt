package com.unifytv.ui.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Card
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.unifytv.data.MediaRepository
import com.unifytv.engine.channel.Guide
import com.unifytv.engine.channel.GuideBuilder
import com.unifytv.engine.channel.GuideRow
import com.unifytv.engine.channel.Program
import com.unifytv.ui.components.ScreenBackground
import com.unifytv.ui.components.formatClock
import com.unifytv.ui.components.formatDuration
import com.unifytv.ui.theme.AccentGradient
import com.unifytv.ui.theme.Glass
import com.unifytv.ui.theme.Teal
import com.unifytv.ui.theme.Violet
import kotlinx.coroutines.delay

@Composable
fun GuideScreen(
    repository: MediaRepository,
    onPlayChannel: (String) -> Unit,
    onAddServer: () -> Unit,
) {
    val channels by repository.channels.collectAsState()
    val catalog by repository.catalog.collectAsState()

    val guide by produceState<Guide?>(initialValue = null, channels, catalog) {
        val builder = GuideBuilder()
        while (true) {
            value = builder.build(channels, catalog, System.currentTimeMillis())
            delay(30_000)
        }
    }

    ScreenBackground {
        Column(Modifier.fillMaxSize().padding(40.dp)) {
            GuideHeader(onAddServer = onAddServer)
            Spacer(Modifier.height(28.dp))

            val rows = guide?.rows.orEmpty()
            if (rows.isEmpty()) {
                EmptyGuide(onAddServer)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    items(rows, key = { it.channel.id }) { row ->
                        ChannelRow(row = row, onPlay = { onPlayChannel(row.channel.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun GuideHeader(onAddServer: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(16.dp).clip(CircleShape).background(AccentGradient))
        Spacer(Modifier.width(12.dp))
        Text("Unify", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("TV", style = MaterialTheme.typography.headlineMedium, color = Teal, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(16.dp))
        Text(
            "Live channels from your library",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.weight(1f))
        Button(onClick = onAddServer) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Add server")
        }
    }
}

@Composable
private fun ChannelRow(row: GuideRow, onPlay: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ChannelLabel(number = row.channel.number, name = row.channel.name)
        Spacer(Modifier.width(16.dp))
        if (row.programs.isEmpty()) {
            Text(
                "Off air",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(row.programs, key = { it.startMs }) { program ->
                    val isLive = row.nowPlaying?.program?.startMs == program.startMs
                    ProgramCard(program = program, isLive = isLive, onClick = onPlay)
                }
            }
        }
    }
}

@Composable
private fun ChannelLabel(number: Int, name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Glass)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Box(
            Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(AccentGradient),
            contentAlignment = Alignment.Center,
        ) {
            Text(number.toString(), color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Text(
            name,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ProgramCard(program: Program, isLive: Boolean, onClick: () -> Unit) {
    val widthDp = (program.durationMs / 60_000L * 6L).coerceIn(150L, 460L).dp
    Card(onClick = onClick, modifier = Modifier.width(widthDp).height(96.dp)) {
        Column(Modifier.fillMaxSize().padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isLive) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(Teal))
                    Spacer(Modifier.width(6.dp))
                    Text("LIVE", style = MaterialTheme.typography.labelLarge, color = Teal)
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    "${formatClock(program.startMs)} · ${formatDuration(program.durationMs)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                program.item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.weight(1f))
            if (isLive) LiveProgress(program)
        }
    }
}

@Composable
private fun LiveProgress(program: Program) {
    val fraction by produceState(0f, program.startMs) {
        while (true) {
            val now = System.currentTimeMillis()
            value = ((now - program.startMs).toFloat() / program.durationMs).coerceIn(0f, 1f)
            delay(1_000)
        }
    }
    Box(
        Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)).background(Glass),
    ) {
        Box(
            Modifier.fillMaxWidth(fraction).height(4.dp).clip(RoundedCornerShape(2.dp)).background(Violet),
        )
    }
}

@Composable
private fun EmptyGuide(onAddServer: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("No channels yet", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            "Connect a Jellyfin, Emby or Plex server to build your line-up.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onAddServer) { Text("Add a server") }
    }
}
