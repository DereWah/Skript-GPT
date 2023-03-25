package org.derewah.skriptgpt;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkriptGPT extends JavaPlugin {

    static SkriptGPT instance;
    SkriptAddon addon;
    public static FileConfiguration config;
    public void onEnable(){
        instance = this;
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("org.derewah.skriptgpt");
        } catch(IOException e){
            e.printStackTrace();
        }

        if (!getDataFolder().exists()) {
            saveDefaultConfig();
        }

        config = getConfig();


        Bukkit.getLogger().info("[SkriptGPT] has been enabled!");

    }

    public static SkriptGPT getInstance(){

        return instance;
    }

    public SkriptAddon getAddonInstance(){
        return addon;
    }


}
