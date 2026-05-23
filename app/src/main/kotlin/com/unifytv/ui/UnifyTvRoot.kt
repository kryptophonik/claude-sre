package com.unifytv.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.unifytv.data.MediaRepository
import com.unifytv.ui.guide.GuideScreen
import com.unifytv.ui.player.PlayerScreen
import com.unifytv.ui.setup.ServerSetupScreen

/** Lightweight in-memory navigation between the three top-level destinations. */
sealed interface Destination {
    data object Guide : Destination
    data class Player(val channelId: String) : Destination
    data object Setup : Destination
}

@Composable
fun UnifyTvRoot(repository: MediaRepository) {
    var destination by rememberSaveable(
        stateSaver = DestinationSaver,
    ) { mutableStateOf<Destination>(Destination.Guide) }

    when (val dest = destination) {
        Destination.Guide -> GuideScreen(
            repository = repository,
            onPlayChannel = { destination = Destination.Player(it) },
            onAddServer = { destination = Destination.Setup },
        )

        is Destination.Player -> PlayerScreen(
            repository = repository,
            startChannelId = dest.channelId,
            onExit = { destination = Destination.Guide },
        )

        Destination.Setup -> ServerSetupScreen(
            repository = repository,
            onDone = { destination = Destination.Guide },
        )
    }
}

private val DestinationSaver = androidx.compose.runtime.saveable.Saver<Destination, String>(
    save = { dest ->
        when (dest) {
            Destination.Guide -> "guide"
            Destination.Setup -> "setup"
            is Destination.Player -> "player:${dest.channelId}"
        }
    },
    restore = { value ->
        when {
            value == "setup" -> Destination.Setup
            value.startsWith("player:") -> Destination.Player(value.removePrefix("player:"))
            else -> Destination.Guide
        }
    },
)
