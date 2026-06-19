package dev.soranzo.minecraft;

import dev.soranzo.discord.Discord;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        String sender_uuid = msg.getPlayer().getUniqueId().toString();
        String message = PlainTextComponentSerializer.plainText().serialize(msg.message());

        //Sends to discord
        dc.sendToDiscord(sender_name, sender_uuid, message);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Component message = event.message();
        if (message == null) return; // hidden advancements/recipes don't have an announce message

        String uuid = event.getPlayer().getUniqueId().toString();
        String name = event.getPlayer().getName();
        String text = PlainTextComponentSerializer.plainText().serialize(message);

        dc.logAdvancement(name, uuid, text);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Component deathMessage = event.deathMessage();
        if (deathMessage == null) return;

        Player victim = event.getEntity();
        String uuid = victim.getUniqueId().toString();
        String name = victim.getName();
        String text = PlainTextComponentSerializer.plainText().serialize(deathMessage);
        boolean killedByPlayer = victim.getKiller() != null;

        dc.logPlayerDeath(name, uuid, text, killedByPlayer);
    }
}
