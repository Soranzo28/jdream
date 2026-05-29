package dev.soranzo.discord;

import dev.soranzo.CommandInfo;
import dev.soranzo.chronointegration.ChronoStoreAPI;
import dev.soranzo.discord.utils.MakeEmbed;
import dev.soranzo.minecraft.Minecraft;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.command.Command;

import java.util.logging.Logger;

public class DiscordListener extends ListenerAdapter {

    private Minecraft mc;
    private Config config;
    private Discord dc;
    private ChronoStoreAPI chronoAPI;

    public DiscordListener(Config config, ChronoStoreAPI api) {
        mc = Minecraft.getInstance();
        this.config = config;
        dc = Discord.getInstance();
        chronoAPI = api;
    }

    @Override
    public void onReady(ReadyEvent event) {

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent message) {
        if (message.getAuthor().isBot()) return;

        String channelId = message.getChannel().getId();
        String senderName = message.getAuthor().getEffectiveName();
        String senderAvatarUrl = message.getAuthor().getAvatarUrl();
        String content = message.getMessage().getContentDisplay();

        Logger.getLogger("Jdream").info("Canal recebido: " + channelId);
        Logger.getLogger("Jdream").info("Canal adm config: " + config.admChannelId());

        // Message sent to minecraft server
        if (channelId.equals(config.chatChannelId())) {

            mc.sendMessageToMinecraft(senderName, content);

        //Message sent as command to minecraft server
        } else if (channelId.equals(config.admChannelId())) {


            CommandInfo commandInfo = new CommandInfo.Builder()
                    .senderName(senderName)
                    .command(content)
                    .senderAvatarUrl(senderAvatarUrl)
                    .build();
             mc.sendCommandToMinecrat(commandInfo, result -> {
                 dc.logCommandOutput(result);
             });

        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "leaderboard" -> handleLeaderboard(event);
            case "dump" -> handleDump(event);
        }
    }

    private void handleLeaderboard(SlashCommandInteractionEvent event) {
        MessageEmbed msg;
        if (chronoAPI == null) {
            msg = MakeEmbed.leaderboardUnavailable();
            event.getHook().sendMessageEmbeds(msg).queue();
        }
        event.deferReply().queue();

        msg = MakeEmbed.leaderboard(chronoAPI.getTopPlayers(10));
        event.getHook().sendMessageEmbeds(msg).queue();
    }

    private void handleDump(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
    }
}
