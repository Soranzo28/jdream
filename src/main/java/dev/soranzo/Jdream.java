package dev.soranzo;

import dev.soranzo.chronointegration.*;
import dev.soranzo.discord.Discord;
import dev.soranzo.minecraft.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import dev.soranzo.minecraft.MinecraftListener;

public class Jdream extends JavaPlugin {

    private Discord dc;
    private Minecraft mc;
    public ChronoStoreAPI chronoApi = null;


    @Override
    public void onEnable() {

        // Checks for chronostore integration
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ChronoStore");
        if (plugin instanceof ChronoStoreAPI) {
            chronoApi = (ChronoStoreAPI) plugin;
        }

        //Initializes discord
        try {
            mc = Minecraft.getInstance(this);
            dc = Discord.getInstance(this);
        } catch (Exception ex) {
            getLogger().severe(ex.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Listener register
        getServer().getPluginManager().registerEvents(new MinecraftListener(), this);

        //Logger
        getLogger().info("Activated!");
    }

    @Override
    public void onDisable() {
        //Logger
        getLogger().info("Deactivated!");
    }
}
