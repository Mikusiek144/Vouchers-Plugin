package me.mikusiek.voucherplugin.commands;

import me.mikusiek.voucherplugin.Main;
import me.mikusiek.voucherplugin.voucher.VoucherManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoucherCommand implements CommandExecutor {
    private final Main plugin;
    private final VoucherManager voucherManager;

    public VoucherCommand(Main plugin) {
        this.plugin = plugin;
        this.voucherManager = new VoucherManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("§cUżyj: /voucher <kod>");
                return false;
            }

            String code = args[0];

            if (voucherManager.isValidVoucher(code)) {
                if (voucherManager.isVoucherUsedByPlayer(code, player.getName())) {
                    player.sendMessage("§cJuż użyłeś tego vouchera: " + code);
                    return true;
                }
                if (!voucherManager.hasUsesRemaining(code)) {
                    player.sendMessage("§cVoucher " + code + " nie ma już dostępnych użyć.");
                    return true;
                }

                String commandToExecute = voucherManager.getCommandForVoucher(code);
                if (commandToExecute != null) {

                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), commandToExecute.replace("{player}", player.getName()));
                    voucherManager.markVoucherAsUsed(code, player.getName());
                    player.sendMessage("§aVoucher zrealizowany: " + code);
                } else {
                    player.sendMessage("§cBłąd: Nie znaleziono komendy przypisanej do tego vouchera.");
                }
            } else {
                player.sendMessage("§cNiepoprawny kod vouchera: " + code);
            }
            return true;
        }

        sender.sendMessage("Ta komenda może być używana tylko przez graczy!");
        return false;
    }
}
