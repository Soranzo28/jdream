package dev.soranzo.discord.utils;

import dev.soranzo.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import dev.soranzo.chronointegration.PlayerRanking;

import java.awt.*;
import java.util.List;
import java.time.Instant;

public class MakeEmbed {

    public static MessageEmbed noOutputMessage(CommandInfo info) {
        return new EmbedBuilder()
                .setTitle("✔️ Comando Executado")
                .setDescription("*Nenhuma resposta retornada.*")
                .setColor(new Color(149, 165, 166))
                .addField("Comando", "`" + info.command + "`", true)
                .addField("Executado por", info.senderName, true)
                .setAuthor(info.senderName, null, info.senderAvatarUrl)
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }

    public static MessageEmbed errorMessage(CommandInfo info) {
        return new EmbedBuilder()
                .setTitle("❌ Erro ao Executar Comando")
                .setColor(new Color(237, 66, 69))
                .addField("Comando", "`" + info.command + "`", true)
                .addField("Executado por", info.senderName, true)
                .setAuthor(info.senderName, null, info.senderAvatarUrl)
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }

    public static MessageEmbed successMessage(CommandInfo info) {
        return new EmbedBuilder()
                .setTitle("✅ Comando Executado")
                .setColor(new Color(46, 204, 113))
                .addField("Comando", "`" + info.command + "`", true)
                .addField("Executado por", info.senderName, true)
                .setAuthor(info.senderName, null, info.senderAvatarUrl)
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }

    public static MessageEmbed joinMessage(String username, String avatarUrl) {
        return new EmbedBuilder()
                .setTitle("✅ Jogador Entrou")
                .setColor(new Color(46, 204, 113))
                .setThumbnail(avatarUrl)
                .setDescription("**" + username + "** entrou no servidor")
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }

    public static MessageEmbed quitMessage(String username, String avatarUrl) {
        return new EmbedBuilder()
                .setTitle("❌ Jogador Saiu")
                .setColor(new Color(231, 76, 60))
                .setThumbnail(avatarUrl)
                .setDescription("**" + username + "** saiu do servidor")
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }

    public static MessageEmbed advancementMessage(String advancementText, String avatarUrl) {
        return new EmbedBuilder()
                .setTitle("🏆 Conquista Desbloqueada")
                .setColor(new Color(241, 196, 15))
                .setThumbnail(avatarUrl)
                .setDescription(advancementText)
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }

    public static MessageEmbed killMessage(String deathText, String avatarUrl) {
        return new EmbedBuilder()
                .setTitle("⚔️ PvP")
                .setColor(new Color(231, 76, 60))
                .setThumbnail(avatarUrl)
                .setDescription(deathText)
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }

    public static MessageEmbed deathMessage(String deathText, String avatarUrl) {
        return new EmbedBuilder()
                .setTitle("💀 Jogador Morreu")
                .setColor(new Color(149, 165, 166))
                .setThumbnail(avatarUrl)
                .setDescription(deathText)
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }

    public static MessageEmbed leaderboardUnavailable() {
        return new EmbedBuilder()
                .setTitle("⚠️ Leaderboard Indisponível")
                .setDescription("A integração com o ChronoStore não está ativa.\nVerifique se o plugin está instalado e configurado corretamente.")
                .setColor(new Color(229, 57, 53))
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .build();
    }

    public static MessageEmbed leaderboard(List<PlayerRanking> players) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            PlayerRanking p = players.get(i);
            long totalSeconds = p.totalTimePlayed();
            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            sb.append(i + 1).append(". **").append(p.name()).append("** — ")
                    .append(hours).append("h ").append(minutes).append("min\n");
        }
        return new EmbedBuilder()
                .setTitle("🏆 Top Jogadores")
                .setColor(new Color(255, 215, 0))
                .setDescription(sb.toString())
                .setFooter("Jdream", "https://i.imgur.com/9v8Wg7e.jpeg")
                .setTimestamp(Instant.now())
                .build();
    }
}
