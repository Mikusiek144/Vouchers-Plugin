package me.mikusiek.voucherplugin.voucher;

import me.mikusiek.voucherplugin.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoucherManager {
    private final Main plugin;
    private final HashMap<String, String> usedVouchers;
    private final HashMap<String, Integer> voucherUses;
    public VoucherManager(Main plugin) {
        this.plugin = plugin;
        this.usedVouchers = new HashMap<>();
        this.voucherUses = new HashMap<>();

        loadVoucherUses();
    }

    private void loadVoucherUses() {
        for (String code : plugin.getConfig().getConfigurationSection("voucher-uses").getKeys(false)) {
            int uses = plugin.getConfig().getInt("voucher-uses." + code);
            voucherUses.put(code, uses);
        }
    }

    public boolean isValidVoucher(String code) {
        List<String> vouchers = plugin.getConfig().getStringList("vouchers");
        return vouchers.contains(code);
    }

    public String getCommandForVoucher(String code) {
        return plugin.getConfig().getString("voucher-commands." + code);
    }

    public boolean isVoucherUsedByPlayer(String code, String playerName) {
        return usedVouchers.containsKey(playerName) && usedVouchers.get(playerName).equals(code);
    }

    public void markVoucherAsUsed(String code, String playerName) {
        usedVouchers.put(playerName, code);

        if (voucherUses.containsKey(code)) {
            int remainingUses = voucherUses.get(code) - 1;
            voucherUses.put(code, remainingUses);
        }
    }

    public boolean canUseVoucher(String code) {
        return voucherUses.containsKey(code) && voucherUses.get(code) > 0;
    }

    public boolean hasUsesRemaining(String code) {
        return voucherUses.getOrDefault(code, 0) > 0;
    }
}
