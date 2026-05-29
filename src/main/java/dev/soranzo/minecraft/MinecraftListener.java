package dev.soranzo.minecraft;

import dev.soranzo.discord.Discord;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class MinecraftListener implements Listener {
    Discord dc = Discord.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        String name = event.getPlayer().getName();
        dc.logJoinQuitEvent(name, uuid, true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        String name = event.getPlayer().getName();
        dc.logJoinQuitEvent(name, uuid, false);
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent msg) {

        // Get infos about sender the message as string
        String sender_name = msg.getPlayer().getName();
        UUID sender_uuid = msg.getPlayer().getUniqueId();
        String message = PlainTextComponentSerializer.plainText().serialize(msg.message());

        String sender_uuid_formated = sender_uuid.toString().replace("-", "");

        //Sends to discord
        dc.sendToDiscord(sender_name, sender_uuid_formated, message);
    }
}
