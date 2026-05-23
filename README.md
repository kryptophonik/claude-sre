# UnifyTV

Turn your **Jellyfin, Emby and Plex** libraries into always-on, channel-surfable
**virtual live TV** — for **Android TV**. Like dizqueTV/ErsatzTV, but built as a
native, leanback-first app with a deliberately polished interface.

UnifyTV imports every connected server into one shared catalog, then lays your
movies and shows out on a broadcast clock so there is always something "on now".
Flip through channels, watch the live progress bar tick, and jump in mid-program
exactly like real TV.

## Why it's different

- **One line-up across servers.** Jellyfin, Emby and Plex content is merged into a
  single catalog and can share a channel.
- **Real broadcast model.** Channels are deterministic: a playlist anchored to a
  start time, looping forever. Any client computes the same "now playing" with no
  shared server state.
- **Aesthetic-first TV UI.** A dark indigo theme with electric-violet/teal accents,
  an EPG-style guide grid, and a cinematic channel-surf overlay.

## Project layout

```
:engine   Pure-Kotlin (JVM) core — no Android dependencies, fully unit-tested
          ├─ model/      MediaItem, MediaLibrary, ServerConfig, kinds
          ├─ server/     MediaServerClient + Jellyfin/Emby + Plex (Ktor) + Fake
          └─ channel/    ChannelScheduler, GuideBuilder, AutoChannelFactory
:app      Android TV app — Jetpack Compose for TV + Media3 (ExoPlayer)
          ├─ ui/theme    Design system (color, type, theme)
          ├─ ui/guide    EPG guide grid
          ├─ ui/player   Live player + channel surfing
          └─ ui/setup    Add-a-server flow
```

The heart of the product — the scheduling/guide engine — lives in `:engine` and is
covered by unit tests (`ChannelSchedulerTest`, `MapperTest`). The Android layer is a
thin, replaceable shell over it.

## Building

Requires Android Studio (Ladybug+) with the Android SDK and JDK 17.

```bash
./gradlew :engine:test        # run the engine unit tests (no SDK needed)
./gradlew :app:assembleDebug  # build the Android TV APK
```

Then deploy to an Android TV device/emulator (API 23+) via Android Studio.

## Connecting a server

In the app: **Add server** → pick Jellyfin / Emby / Plex → enter the server URL plus:

- **Jellyfin / Emby:** an API key and your user ID.
- **Plex:** your `X-Plex-Token`.

UnifyTV imports the libraries, auto-generates channels (per-series binge channels and
per-genre shuffle channels), and drops you into the guide. Before any server is
connected a built-in demo line-up is shown so the UI is never empty.

## Status

- `:engine` — implemented and unit-tested.
- `:app` — implemented; build it in Android Studio (the UI module needs the Android
  SDK + Google's Maven, so it isn't compiled in headless CI environments).

## License

See [LICENSE](LICENSE).
