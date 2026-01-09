package fr.democraft.rcs.core;

import org.bukkit.plugin.java.JavaPlugin;

public class NotAPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().severe("================================");
        getLogger().severe(" SmartRCM cannot be run as a Bukkit/Spigot/Paper plugin!");
        getLogger().severe(" Please use it as a RustyConnector module.");
        getLogger().severe("================================");
        getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
    }
}
