package com.bobtheblueberry.villager_inv.event.listener;

import com.bobtheblueberry.villager_inv.VillagerInvPlugin;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class clears people from the open inventory mode to prevent memory leaks.
 *
 * @author Whitescan
 * @since 1.0.0
 */
@AllArgsConstructor
public final class PlayerQuitListener implements Listener {

    private final VillagerInvPlugin plugin;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getOpenInventoryMode().remove(event.getPlayer().getUniqueId());
    }

}
