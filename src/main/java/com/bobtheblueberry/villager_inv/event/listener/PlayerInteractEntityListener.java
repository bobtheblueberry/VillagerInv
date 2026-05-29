package com.bobtheblueberry.villager_inv.event.listener;

import com.bobtheblueberry.villager_inv.VillagerInvPlugin;
import com.bobtheblueberry.villager_inv.inventory.VillagerInventoryHolder;
import com.bobtheblueberry.villager_inv.inventory.VillagerInventoryRegistry;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * This class handles the open inventory mode, while interacting with villagers.
 *
 * @author Serge Humphrey
 * @since 1.0.0
 */
@AllArgsConstructor
public final class PlayerInteractEntityListener implements Listener {

    private final VillagerInvPlugin plugin;

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        // not a villager, ignore
        if (!(event.getRightClicked() instanceof Villager villager))
            return;

        final Player actor = event.getPlayer();

        // player doesn't have open inventory mode enabled, ignore
        if (!plugin.getOpenInventoryMode().contains(actor.getUniqueId()))
            return;

        // player has open inventory mode enabled, cancel the event and open the proxy inventory
        event.setCancelled(true);

        final VillagerInventoryHolder holder = new VillagerInventoryHolder(villager);
        final Inventory proxy = Bukkit.createInventory(holder, 9, plugin.getMessageService().get("villager_inventory_title"));
        holder.setInventory(proxy);

        VillagerInventoryRegistry.syncVillagerToProxy(villager, proxy);
        proxy.setItem(VillagerInventoryHolder.FILLER_SLOT, createFiller());

        plugin.getInventoryRegistry().register(holder);
        actor.openInventory(proxy);

    }

    private ItemStack createFiller() {
        final ItemStack filler = new ItemStack(Material.BARRIER);
        final ItemMeta meta = filler.getItemMeta();
        if (meta != null) {
            meta.displayName(plugin.getMessageService().get("villager_inventory_filler_icon_name"));
            filler.setItemMeta(meta);
        }
        return filler;
    }

}
