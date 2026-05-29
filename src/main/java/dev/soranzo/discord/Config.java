package dev.soranzo.discord;

import dev.soranzo.CommandInfo;
import dev.soranzo.discord.exceptions.InvalidConfigException;

public record Config(
        String botToken,
        String guildId,
        String chatChannelId,
        String admChannelId,
        String logChannelId,
        String webHookUrl
) {
    public void validateConfig() throws InvalidConfigException {
        if (
                (botToken == null) || botToken.isEmpty()
                || (guildId == null) || guildId.isEmpty()
                || (chatChannelId == null) || chatChannelId.isEmpty()
                || (admChannelId == null) || admChannelId.isEmpty()
                || (logChannelId == null) || logChannelId.isEmpty()
                || (webHookUrl == null) || webHookUrl.isEmpty()
        ) {
            throw new InvalidConfigException("Invalid or incomplete configuration! Check config.yml");
        }
    }

}
