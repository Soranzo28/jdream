package dev.soranzo.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import dev.soranzo.CommandInfo;
import dev.soranzo.Jdream;
import dev.soranzo.discord.exceptions.*;
import dev.soranzo.discord.utils.MakeEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.skinsrestorer.api.PropertyUtils;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.property.SkinProperty;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.Optional;
import java.util.UUID;

public class Discord {

    private final Jdream jd;
    private final JDA jda;
    private final WebhookClient webhook;
    private OkHttpClient httpClient;
    private SkinsRestorer skinsRestorer;


    private static Discord instance = null;
    private Config config = null;

    private Discord(Jdream plugin) throws InvalidConfigException{
        instance = this;
        jd = plugin;
        plugin.saveDefaultConfig();
        config = new Config(
                jd.getConfig().getString("token"),
                jd.getConfig().getString("guild-id"),
                jd.getConfig().getString("chat-channel-id"),
                jd.getConfig().getString("adm-channel-id"),
                jd.getConfig().getString("log-channel-id"),
                jd.getConfig().getString("webhook-url")
        );

        config.validateConfig();

        // Java discord API setup
        jda = JDABuilder.createDefault(config.botToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DiscordListener(config, jd.chronoApi))
                .build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new InvalidConfigException("Unable to connect to the bot");
        }

        //Webhook setup
        httpClient = new OkHttpClient();
        webhook = new WebhookClientBuilder(config.webHookUrl())
                .setHttpClient(httpClient)
                .build();


        Guild guild = jda.getGuildById(config.guildId());
        if (guild == null) {
            jd.getLogger().severe("Guild não encontrada, comandos não registrados.");
            return;
        }

        guild.upsertCommand("leaderboard", "Mostra top jogadores ativos do servidor").queue();
        guild.upsertCommand("dump", "Dump da database").queue();

        if (Bukkit.getPluginManager().getPlugin("SkinsRestorer") != null) {
            skinsRestorer = SkinsRestorerProvider.get();
            jd.getLogger().info("SkinsRestorer conectado.");
        }
    }

    // Minecraft message -> Discord (chat channel)
    public void sendToDiscord(String username, String user_uuid, String content) {
        String avatarUrl = getAvatarUrl(username, user_uuid);

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(username)
                .setAvatarUrl(avatarUrl)
                .setContent(content);

        webhook.send(builder.build());
    }

    private String getAvatarUrl(String username, String user_uuid) {
        if (skinsRestorer != null) {
            try {
                Optional<SkinProperty> skin = skinsRestorer
                        .getPlayerStorage()
                        .getSkinForPlayer(UUID.fromString(user_uuid), username);

                if (skin.isPresent()) {
                    String textureHash = PropertyUtils.getSkinTextureHash(skin.get());

                    if (textureHash != null && !textureHash.isBlank()) {
                        return "https://mc-heads.net/avatar/" + textureHash + "/128";
                    }
                }
            } catch (Exception e) {
                jd.getLogger().warning("Erro ao buscar skin do SkinsRestorer para " + username + ": " + e.getMessage());
            }
        }

        return "https://mc-heads.net/avatar/" + username + "/128";
    }


    // Command output -> Discord (adm channel)
    public void logCommandOutput(CommandInfo commandInfo) {
        MessageEmbed msg;

        if (commandInfo.commandSucess){
            msg = MakeEmbed.successMessage(commandInfo);
        } else {
            msg = MakeEmbed.errorMessage(commandInfo);
        }

        TextChannel channel = jda.getTextChannelById(config.admChannelId());
        channel.sendMessageEmbeds(msg).queue();
    }

    public void logJoinQuitEvent(String username, String uuid, boolean joined) {
        MessageEmbed msg;
        if (joined) {
            msg = MakeEmbed.joinMessage(username, uuid);
        } else{
            msg = MakeEmbed.quitMessage(username, uuid);
        }
        TextChannel channel = jda.getTextChannelById(config.chatChannelId());
        channel.sendMessageEmbeds(msg).queue();
    }

    // Singleton getters
    public static Discord getInstance(Jdream plugin) throws InvalidConfigException{
        if (instance == null) instance = new Discord(plugin);
        return instance;
    }

    public static Discord getInstance() {
        if (instance == null) throw new DiscordNotInitializedException("You must initialize this class first!");
        else return instance;
    }
}
