package com.btbb.villageinv;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;


public class NPCInvPlugin extends JavaPlugin {

	NPCEventHandler eventHandler;

	HashMap<UUID, Villager> invMap;
	HashMap<UUID, Boolean> enableMap;
	
	boolean dropOnDeath;
	
	public void onEnable() {
		eventHandler = new NPCEventHandler(this);
		getServer().getPluginManager().registerEvents(eventHandler, this);
		invMap = new HashMap<UUID, Villager>();
		enableMap = new HashMap<UUID,Boolean>();

		getCommand("npcinv").setExecutor(new NPCCommands(this));
		
		//enable npc drop items on death
		String key = "npc-drop-on-death";

		if (!getConfig().contains(key))
		{
			getConfig().set(key, false);
			saveConfig();
		}
		dropOnDeath = getConfig().getBoolean(key, false);
	}


	public void onDisable() {
		invMap.clear();
		enableMap.clear();
	}

}
