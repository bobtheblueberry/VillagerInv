package com.bobtheblueberry.villager_inv.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Holder for the 9-slot proxy inventory that mirrors a villager's 8-slot inventory.
 * Slot 8 is a non-interactive filler so the layout matches a single-row chest.
 *
 * @author Whitescan
 * @since 1.0.0
 */
public final class VillagerInventoryHolder implements InventoryHolder {

    public static final int VILLAGER_SLOTS = 8;
    public static final int FILLER_SLOT = 8;

    @Getter
    private final Villager villager;

    @Setter
    private Inventory inventory;

    public VillagerInventoryHolder(Villager villager) {
        this.villager = villager;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

}
