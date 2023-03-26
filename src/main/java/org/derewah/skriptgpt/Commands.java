package org.derewah.skriptgpt;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

public class Commands implements CommandExecutor {

    private final SkriptGPT plugin;

    public Commands(SkriptGPT plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) args = new String[]{"help"};
        if (command.getName().equalsIgnoreCase("skriptgpt")) {
            if (sender.hasPermission("skriptgpt.reload")) {
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(LIGHT_PURPLE + "------------------ " + DARK_PURPLE + "Skript-GPT" + LIGHT_PURPLE + " ------------------");
                    sender.sendMessage(" ");
                    sender.sendMessage(LIGHT_PURPLE + "/skriptgpt help" + WHITE + " Show this help message.");
                    sender.sendMessage(LIGHT_PURPLE + "/skriptgpt reload" + WHITE + " Reload the config and the OpenAI key..");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    SkriptGPT.getInstance().reloadConfig();
                    sender.sendMessage(LIGHT_PURPLE + "["+DARK_PURPLE+"SkriptGPT"+LIGHT_PURPLE+"]"+GREEN+" Config reloaded.");
                    return false;
                } else {
                    sender.sendMessage(LIGHT_PURPLE + "["+DARK_PURPLE+"SkriptGPT"+LIGHT_PURPLE+"]"+RED+" Invalid command. /skriptgpt help");
                }
                return false;
            } else{
            sender.sendMessage(LIGHT_PURPLE + "["+DARK_PURPLE+"SkriptGPT"+LIGHT_PURPLE+"]"+RED+" You don't have the permission " + DARK_RED + "SkriptGPT.reload"+RED+".");
            }
            return false;
        }
        return false;
    }
}