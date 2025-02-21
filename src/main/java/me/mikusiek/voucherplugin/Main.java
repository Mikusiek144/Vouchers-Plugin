package me.mikusiek.voucherplugin;

import org.bukkit.plugin.java.JavaPlugin;
import me.mikusiek.voucherplugin.commands.VoucherCommand;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("voucher").setExecutor(new VoucherCommand(this));
    }

    @Override
    public void onDisable() {
    }
}
