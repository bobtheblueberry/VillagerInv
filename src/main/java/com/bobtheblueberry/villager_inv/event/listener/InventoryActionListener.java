package com.bobtheblueberry.villager_inv.event.listener;

import com.bobtheblueberry.villager_inv.VillagerInvPlugin;
import com.bobtheblueberry.villager_inv.inventory.VillagerInventoryHolder;
import com.bobtheblueberry.villager_inv.inventory.VillagerInventoryRegistry;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

/**
 * This class handles the villager inventory read and write permissions and keeps the proxy
 * inventory in sync with the underlying villager inventory.
 *
 * @author Serge Humphrey
 * @since 1.0.0
 */
@AllArgsConstructor
public final class InventoryActionListener implements Listener {

    private final VillagerInvPlugin plugin;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        final Inventory top = event.getView().getTopInventory();
        if (!(top.getHolder() instanceof VillagerInventoryHolder holder))
            return;

        final HumanEntity human = event.getWhoClicked();

        if (!human.hasPermission("villagerinv.edit")) {
            human.sendMessage(plugin.getMessageService().get("no_permission"));
            event.setCancelled(true);
            return;
        }

        // block the filler slot
        if (event.getClickedInventory() == top && event.getSlot() == VillagerInventoryHolder.FILLER_SLOT) {
            event.setCancelled(true);
            human.closeInventory();
            return;
        }

        // block shift-clicking items into the filler slot (or any action that could end up there)
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
                && event.getClickedInventory() != top
                && top.getItem(VillagerInventoryHolder.FILLER_SLOT) != null
                && firstEmptyVillagerSlot(top) == -1) {
            event.setCancelled(true);
            return;
        }

        // sync proxy -> villager after the click is processed
        Bukkit.getScheduler().runTask(plugin,
                () -> VillagerInventoryRegistry.syncProxyToVillager(top, holder.getVillager()));
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {

        final Inventory top = event.getView().getTopInventory();
        if (!(top.getHolder() instanceof VillagerInventoryHolder holder))
            return;

        final HumanEntity human = event.getWhoClicked();

        if (!human.hasPermission("villagerinv.edit")) {
            human.sendMessage(plugin.getMessageService().get("no_permission"));
            event.setCancelled(true);
            return;
        }

        // prevent dropping anything onto the filler slot
        if (event.getRawSlots().contains(VillagerInventoryHolder.FILLER_SLOT)) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTask(plugin,
                () -> VillagerInventoryRegistry.syncProxyToVillager(top, holder.getVillager()));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (!(event.getInventory().getHolder() instanceof VillagerInventoryHolder holder))
            return;

        // final sync, then unregister if no viewers remain
        VillagerInventoryRegistry.syncProxyToVillager(event.getInventory(), holder.getVillager());

        if (event.getInventory().getViewers().size() <= 1)
            plugin.getInventoryRegistry().unregister(holder);

    }

    private static int firstEmptyVillagerSlot(Inventory proxy) {
        for (int slot = 0; slot < VillagerInventoryHolder.VILLAGER_SLOTS; slot++)
            if (proxy.getItem(slot) == null) return slot;
        return -1;
    }

}
