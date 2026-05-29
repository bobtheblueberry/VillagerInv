package com.bobtheblueberry.villager_inv.event.listener;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This class handles the villager-drop-items-on-death feature if enabled in the config.
 *
 * @author Serge Humphrey
 * @since 1.0.0
 */
public final class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        // not a villager, ignore
        if (!(event.getEntity() instanceof Villager villager))
            return;

        // drop inventory content
        for (ItemStack itemStack : villager.getInventory().getContents())
            if (itemStack != null)
                villager.getWorld().dropItemNaturally(villager.getLocation(), itemStack);

    }

}
