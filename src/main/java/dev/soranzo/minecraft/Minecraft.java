package dev.soranzo.minecraft;

import dev.soranzo.CommandInfo;
import dev.soranzo.Jdream;
import dev.soranzo.discord.Discord;
import dev.soranzo.minecraft.exceptions.MinecraftNotInitializedException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class Minecraft {
    private static Minecraft instance = null;
    private Jdream jd;

    private Minecraft(Jdream plugin) {
        jd = plugin;
    }

    public void sendMessageToMinecraft(String username, String content) {
        Component message = Component.text("[Discord] ")
                .color(NamedTextColor.BLUE)
                .append(Component.text("<").color(NamedTextColor.WHITE))
                .append(Component.text(username).color(NamedTextColor.WHITE))
                .append(Component.text("> ").color(NamedTextColor.WHITE))
                .append(Component.text(content).color(NamedTextColor.WHITE));

        jd.getLogger().info("Content: " + content);

        Bukkit.getScheduler().runTask(jd, () -> {
            Bukkit.getServer().broadcast(message);
        });
    }

    public void sendCommandToMinecrat(CommandInfo preCommandInfo, Consumer<CommandInfo> callback) {

        Bukkit.getScheduler().runTask(jd, () -> {
            boolean success = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), preCommandInfo.command);

            CommandInfo commandInfo = new CommandInfo.Builder()
                    .senderName(preCommandInfo.senderName)
                    .senderAvatarUrl(preCommandInfo.senderAvatarUrl)
                    .command(preCommandInfo.command)
                    .commandSuccess(success)
                    .build();

            callback.accept(commandInfo);
        });


    }

    //Singleton getters
    public static Minecraft getInstance(Jdream plugin) {
        if (instance == null) instance = new Minecraft(plugin);
        return instance;
    }

    public static Minecraft getInstance() {
        if (instance == null) throw new MinecraftNotInitializedException("You must initialize this class first!");
        return instance;
    }
}
