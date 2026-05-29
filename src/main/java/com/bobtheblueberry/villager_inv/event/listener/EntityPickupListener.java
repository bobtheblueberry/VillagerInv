package com.bobtheblueberry.villager_inv.event.listener;

import com.bobtheblueberry.villager_inv.VillagerInvPlugin;
import com.bobtheblueberry.villager_inv.inventory.VillagerInventoryHolder;
import com.bobtheblueberry.villager_inv.inventory.VillagerInventoryRegistry;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * This class handles the villager inventory change mirroring (item pickup) back into any open proxy inventory so the
 * viewer always sees the current contents.
 *
 * @author Whitescan
 * @since 1.0.0
 */
@AllArgsConstructor
public final class EntityPickupListener implements Listener {

    private final VillagerInvPlugin plugin;

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Villager villager))
            return;

        final var proxies = plugin.getInventoryRegistry().getOpenProxies(villager.getUniqueId());
        if (proxies.isEmpty())
            return;

        // run next tick so the villager has actually received the item
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (VillagerInventoryHolder holder : proxies) {
                VillagerInventoryRegistry.syncVillagerToProxy(villager, holder.getInventory());
            }
        });
    }

}
