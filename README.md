# Jdream

A [Paper](https://papermc.io/) plugin that bridges a Minecraft server with a Discord server: chat flows both ways, join/quit events show up as embeds, and a Discord admin channel can run server console commands remotely.

## Features

- **Chat bridge**: Minecraft chat messages are relayed to a Discord channel via webhook (with the player's name and avatar), and messages sent in that Discord channel are broadcast back into the Minecraft chat.
- **Skin-aware avatars**: if [SkinsRestorer](https://www.spigotmc.org/resources/skinsrestorer.2124/) is installed, the Discord webhook avatar uses the player's actual restored skin instead of their vanilla Mojang skin. Falls back to a username-based avatar otherwise.
- **Join/quit embeds**: player join and quit events are posted to the chat channel as Discord embeds.
- **Remote console commands**: messages sent in a configured admin channel are executed as server console commands, with a success/error embed reply showing the output.
- **Slash commands**: `/leaderboard` shows the top active players (requires the optional [ChronoStore](https://github.com/Soranzo28) integration).

## Requirements

- Java 25
- A [Paper](https://papermc.io/) server compatible with the `26.1.2` API

### Optional integrations

- [SkinsRestorer](https://www.spigotmc.org/resources/skinsrestorer.2124/) — enables skin-accurate Discord avatars.
- ChronoStore — enables the `/leaderboard` slash command.

Both are soft dependencies: Jdream works without them, just with the related feature disabled.

## Configuration

On first run, Jdream generates `plugins/Jdream/config.yml`:

```yaml
token: ""           # Discord bot token
guild-id: ""         # Discord server (guild) ID
chat-channel-id: ""  # channel mirrored to/from Minecraft chat
adm-channel-id: ""   # channel used to run remote console commands
log-channel-id: ""   # channel used for join/quit logs
webhook-url: ""      # webhook URL used to post Minecraft chat messages
```

All fields are required — the plugin refuses to enable if any of them is missing. `config.yml` is gitignored on purpose: never commit your bot token or webhook URL.

## Build

```bash
mvn package
```

The generated jar will be at `target/jdream-1.0-SNAPSHOT.jar`. Copy it into the server's `plugins/` folder.

## Project structure

```
src/main/java/dev/soranzo/
├── discord/      # Discord-side integration (bot, webhook, listeners, embeds)
├── minecraft/    # Minecraft-side integration (chat broadcast, command dispatch)
├── Jdream.java   # Plugin entry point
└── CommandInfo.java
```

## Stack

- [Paper API](https://papermc.io/)
- [JDA](https://github.com/discord-jda/JDA) for the Discord bot
- [discord-webhooks](https://github.com/MinnDevelopment/discord-webhooks) for chat relaying
