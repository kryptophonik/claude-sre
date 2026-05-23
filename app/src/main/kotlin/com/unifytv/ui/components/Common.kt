package com.unifytv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.unifytv.engine.channel.ChannelDefinition
import com.unifytv.engine.model.MediaItem
import com.unifytv.ui.theme.AccentGradient
import com.unifytv.ui.theme.Cloud
import com.unifytv.ui.theme.Plum
import java.util.Locale
import java.util.concurrent.TimeUnit

/** Full-screen ambient gradient backdrop. */
@Composable
fun ScreenBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier.fillMaxSize().background(com.unifytv.ui.theme.AppBackground)) { content() }
}

/** Poster art that gracefully falls back to a tinted block with the title. */
@Composable
fun PosterArt(item: MediaItem?, modifier: Modifier = Modifier) {
    val url = item?.posterUrl
    if (url.isNullOrBlank()) {
        Box(modifier.background(Plum)) {
            Text(
                text = item?.title.orEmpty(),
                color = Cloud,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxSize(),
            )
        }
    } else {
        AsyncImage(model = url, contentDescription = item?.title, modifier = modifier)
    }
}

/** A small gradient chip standing in for a per-channel logo. */
@Composable
fun ChannelBadge(channel: ChannelDefinition, modifier: Modifier = Modifier) {
    Box(modifier.background(AccentGradient)) {
        Text(text = channel.number.toString(), color = Color.Black)
    }
}

fun formatDuration(ms: Long): String {
    val totalMin = TimeUnit.MILLISECONDS.toMinutes(ms)
    val h = totalMin / 60
    val m = totalMin % 60
    return if (h > 0) String.format(Locale.US, "%dh %02dm", h, m) else "${m}m"
}

fun formatClock(epochMs: Long): String {
    val cal = java.util.Calendar.getInstance().apply { timeInMillis = epochMs }
    return String.format(
        Locale.US, "%02d:%02d",
        cal.get(java.util.Calendar.HOUR_OF_DAY),
        cal.get(java.util.Calendar.MINUTE),
    )
}
