package com.bobtheblueberry.villager_inv;

import com.bobtheblueberry.villager_inv.command.VillagerInvCommand;
import com.bobtheblueberry.villager_inv.event.listener.*;
import com.bobtheblueberry.villager_inv.inventory.VillagerInventoryRegistry;
import com.bobtheblueberry.villager_inv.service.MessageService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * This is the plugins main class.
 *
 * @author Serge Humphrey
 * @since 1.0.0
 */
@Getter
public final class VillagerInvPlugin extends JavaPlugin {

    private MessageService messageService;

    private final Set<UUID> openInventoryMode = new HashSet<>();

    private final VillagerInventoryRegistry inventoryRegistry = new VillagerInventoryRegistry();

    @Setter
    private Locale locale;

    @Setter
    private boolean dropOnDeathEnabled;

    public void onEnable() {

        loadConfig();

        this.messageService = new MessageService(getLocale());

        registerEventListeners();

        registerCommands();

    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("villagerinv")).setExecutor(new VillagerInvCommand(this));
    }

    private void registerEventListeners() {

        final PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new InventoryActionListener(this), this);
        pm.registerEvents(new PlayerInteractEntityListener(this), this);
        pm.registerEvents(new EntityPickupListener(this), this);

        if (isDropOnDeathEnabled())
            pm.registerEvents(new EntityDeathListener(), this);

    }

    private void loadConfig() {

        saveDefaultConfig();

        try {
            this.locale = Locale.forLanguageTag(getConfig().getString("language", "en"));
        } catch (NullPointerException e) {
            getLogger().warning("Configured language tag is invalid - falling back to English");
            setLocale(Locale.ENGLISH);
        }

        this.dropOnDeathEnabled = getConfig().getBoolean("villager-drop-inventory-on-death", false);

    }

    public void onDisable() {
        getOpenInventoryMode().clear();
        inventoryRegistry.clear();
    }

}
