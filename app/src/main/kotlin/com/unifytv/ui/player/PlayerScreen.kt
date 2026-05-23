package com.unifytv.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.unifytv.data.MediaRepository
import com.unifytv.engine.channel.ChannelDefinition
import com.unifytv.engine.channel.ChannelScheduler
import com.unifytv.engine.channel.NowPlaying
import com.unifytv.ui.components.formatDuration
import com.unifytv.ui.theme.AccentGradient
import com.unifytv.ui.theme.Ink
import com.unifytv.ui.theme.PosterScrim
import com.unifytv.ui.theme.Teal
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    repository: MediaRepository,
    startChannelId: String,
    onExit: () -> Unit,
) {
    val channels by repository.channels.collectAsState()
    val catalog by repository.catalog.collectAsState()
    val scheduler = remember { ChannelScheduler() }

    var channelId by remember { mutableStateOf(startChannelId) }
    var nowPlaying by remember { mutableStateOf<NowPlaying?>(null) }
    var overlayVisible by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply { playWhenReady = true }
    }
    DisposableEffect(Unit) { onDispose { player.release() } }

    val sorted = channels.sortedBy { it.number }
    val channel: ChannelDefinition? = sorted.firstOrNull { it.id == channelId }

    fun surf(delta: Int) {
        if (sorted.isEmpty()) return
        val idx = sorted.indexOfFirst { it.id == channelId }.takeIf { it >= 0 } ?: 0
        channelId = sorted[(idx + delta).mod(sorted.size)].id
        overlayVisible = true
    }

    // Retune whenever the channel (or catalog) changes; advance at each program boundary.
    LaunchedEffect(channelId, channels, catalog) {
        val def = sorted.firstOrNull { it.id == channelId } ?: return@LaunchedEffect
        while (true) {
            val np = scheduler.nowPlaying(def, catalog, System.currentTimeMillis())
            nowPlaying = np
            if (np == null) {
                delay(3_000)
                continue
            }
            val url = np.program.item.playbackUrl
            if (url.isNotBlank()) {
                player.setMediaItem(ExoMediaItem.fromUri(url))
                player.prepare()
                player.seekTo(np.seekOffsetMs.coerceAtLeast(0L))
            } else {
                player.stop()
            }
            val remaining = np.program.endMs - System.currentTimeMillis()
            delay(remaining.coerceAtLeast(1_000L))
        }
    }

    // Auto-hide the channel overlay shortly after it appears.
    LaunchedEffect(overlayVisible, channelId) {
        if (overlayVisible) {
            delay(4_000)
            overlayVisible = false
        }
    }

    BackHandler(onBack = onExit)

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Box(
        Modifier
            .fillMaxSize()
            .background(Ink)
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.DirectionUp, Key.ChannelUp -> { surf(+1); true }
                    Key.DirectionDown, Key.ChannelDown -> { surf(-1); true }
                    Key.DirectionCenter, Key.Enter -> { overlayVisible = !overlayVisible; true }
                    else -> false
                }
            },
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    this.player = player
                }
            },
            modifier = Modifier.fillMaxSize(),
        )

        if (nowPlaying?.program?.item?.playbackUrl.isNullOrBlank()) {
            DemoNotice()
        }

        if (overlayVisible && channel != null) {
            ChannelOverlay(channel = channel, nowPlaying = nowPlaying)
        }
    }
}

@Composable
private fun DemoNotice() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            "Demo channel — connect a server to stream real video",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ChannelOverlay(channel: ChannelDefinition, nowPlaying: NowPlaying?) {
    Box(Modifier.fillMaxSize().background(PosterScrim)) {
        Column(Modifier.align(Alignment.BottomStart).padding(48.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(52.dp).clip(RoundedCornerShape(12.dp)).background(AccentGradient),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(channel.number.toString(), color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(16.dp))
                Text(channel.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            val program = nowPlaying?.program
            if (program != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(Teal))
                    Spacer(Modifier.width(8.dp))
                    Text("NOW", style = MaterialTheme.typography.labelLarge, color = Teal)
                }
                Spacer(Modifier.height(4.dp))
                Text(program.item.title, style = MaterialTheme.typography.titleLarge)
                Text(
                    formatDuration(program.durationMs),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            nowPlaying?.next?.let { next ->
                Spacer(Modifier.height(10.dp))
                Text(
                    "Next · ${next.item.title}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "▲ / ▼  change channel      ◀ back to guide",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
