package com.bobtheblueberry.villager_inv.command;

import com.bobtheblueberry.villager_inv.VillagerInvPlugin;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class handles the /villagerinv toggle command, which allows permitted users to access villager inventories.
 *
 * @author Serge Humphrey
 * @since 1.0.0
 */
@AllArgsConstructor
public final class VillagerInvCommand implements TabExecutor {

    private final VillagerInvPlugin plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!sender.hasPermission("villagerinv.use")) {
            sender.sendMessage(plugin.getMessageService().get("no_permission"));
            return true;
        }

        if (!(sender instanceof Player actor)) {
            sender.sendMessage(plugin.getMessageService().get("player_only"));
            return true;
        }

        if (plugin.getOpenInventoryMode().contains(actor.getUniqueId())) {
            plugin.getOpenInventoryMode().remove(actor.getUniqueId());
            actor.sendMessage(plugin.getMessageService().get("open_inventory_mode_toggled_off"));
            return true;
        }

        plugin.getOpenInventoryMode().add(actor.getUniqueId());
        actor.sendMessage(plugin.getMessageService().get("open_inventory_mode_toggled_on"));
        return true;

    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }

}
