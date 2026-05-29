package com.bobtheblueberry.villager_inv.inventory;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks open villager inventory proxies so we can keep them in sync with the underlying villager
 * inventory while it is being viewed.
 *
 * @author Whitescan
 * @since 1.0.0
 */
public final class VillagerInventoryRegistry {

    private final Map<UUID, Set<VillagerInventoryHolder>> openProxies = new ConcurrentHashMap<>();

    public void register(VillagerInventoryHolder holder) {
        openProxies.computeIfAbsent(holder.getVillager().getUniqueId(), k -> new HashSet<>()).add(holder);
    }

    public void unregister(VillagerInventoryHolder holder) {
        openProxies.computeIfPresent(holder.getVillager().getUniqueId(), (k, set) -> {
            set.remove(holder);
            return set.isEmpty() ? null : set;
        });
    }

    public Set<VillagerInventoryHolder> getOpenProxies(UUID villagerId) {
        return openProxies.getOrDefault(villagerId, Collections.emptySet());
    }

    public void clear() {
        openProxies.clear();
    }

    /**
     * Copies the villager's 8 inventory slots into the proxy's first 8 slots, preserving the filler.
     */
    public static void syncVillagerToProxy(Villager villager, Inventory proxy) {
        final Inventory source = villager.getInventory();
        for (int i = 0; i < VillagerInventoryHolder.VILLAGER_SLOTS; i++) {
            proxy.setItem(i, source.getItem(i));
        }
    }

    /**
     * Copies the proxy's first 8 slots back into the villager's inventory.
     */
    public static void syncProxyToVillager(Inventory proxy, Villager villager) {
        final Inventory target = villager.getInventory();
        for (int i = 0; i < VillagerInventoryHolder.VILLAGER_SLOTS; i++) {
            final ItemStack item = proxy.getItem(i);
            target.setItem(i, item == null ? null : item.clone());
        }
    }

}
