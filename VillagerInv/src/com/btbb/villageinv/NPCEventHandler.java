package com.btbb.villageinv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NPCEventHandler implements Listener {
	NPCInvPlugin plugin;

	public NPCEventHandler(NPCInvPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onVillagerDied(EntityDeathEvent e) {
		if (!plugin.dropOnDeath)
			return;
		LivingEntity ent = e.getEntity();
		if (!(ent instanceof Villager))
			return;
		Villager v = (Villager) ent;
		for (ItemStack is : v.getInventory().getContents())
			if (is != null)
			v.getWorld().dropItemNaturally(v.getLocation(), is);
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		if (player == null)
			return;

		Boolean enable = plugin.enableMap.get(player.getUniqueId());
		if (enable == null || !enable.booleanValue())
			return;
		Entity vil = e.getRightClicked();
		if (vil == null || vil.getType() != EntityType.VILLAGER)
			return;
		e.setCancelled(true);
		Villager v = (Villager) vil;
		Inventory i = v.getInventory();
		Inventory ni = Bukkit.createInventory(player, 9, v.getName() + "'s Inventory");
		for (ItemStack item : i.getContents()) {
			if (item != null)
				ni.addItem(item);
		}
		// last slot
		ItemStack lastItem = new ItemStack(Material.BARRIER);
		ni.setItem(8, lastItem);
		player.openInventory(ni);
		plugin.invMap.put(player.getUniqueId(), v);
	}

	@EventHandler
	public void onClick(InventoryClickEvent evt) {
		Inventory v = evt.getInventory();
		if (v == null)
			return;
		if (v.getSize() != 9)
			return;
		HumanEntity human = evt.getWhoClicked();
		final int slot = evt.getSlot();
		// if they in clicked their own inventory
		if (slot > 8 || slot < 0)
			return;
		Villager vil = plugin.invMap.get(human.getUniqueId());
		if (vil == null)
			return;
		if (evt.getSlot() == 8) {
			evt.setCancelled(true);
			return;
		}

		if (!human.hasPermission("npcinv.edit")) {
			human.sendMessage(ChatColor.RED + "You do not have permission to edit");
			evt.setCancelled(true);
			return;
		}
		Bukkit.getScheduler().runTask(plugin, new InvUpdater(v, vil.getInventory(), (Player) human));

	}

	@EventHandler
	public void onDrag(InventoryDragEvent evt) {
		Inventory v = evt.getInventory();
		if (v == null)
			return;
		if (v.getSize() != 9)
			return;
		HumanEntity human = evt.getWhoClicked();
		Villager vil = plugin.invMap.get(human.getUniqueId());
		if (vil == null)
			return;
		if (!human.hasPermission("npcinv.edit")) {
			human.sendMessage(ChatColor.RED + "You do not have permission to edit");
			evt.setCancelled(true);
			return;
		}
		Bukkit.getScheduler().runTask(plugin, new InvUpdater(v, vil.getInventory(), (Player) human));
	}

	@EventHandler
	public void onCose(InventoryCloseEvent evt) {
		if (evt.getPlayer() == null)
			return;
		plugin.invMap.remove(evt.getPlayer());
	}

	private class InvUpdater implements Runnable {

		Inventory source, drain;
		boolean doInverse;
		Player player;

		public InvUpdater(Inventory source, Inventory drain, Player player) {
			this(source, drain, player, true);
		}

		public InvUpdater(Inventory source, Inventory drain, Player player, boolean doInverse) {
			this.source = source;
			this.drain = drain;
			this.doInverse = doInverse;
			this.player = player;
		}

		@Override
		public void run() {
			for (int i = 0; i < 8; i++)
				drain.setItem(i, source.getItem(i));
			if (doInverse) {
				restackInv(drain);
				Bukkit.getScheduler().runTask(plugin, new InvUpdater(drain, source, player, false));
			}
		}

		private void restackInv(Inventory inv) {
			boolean changed = false;
			loop1: for (int i = 0; i < 8; i++) {
				ItemStack cur = inv.getItem(i);
				// get rid of empty slots between items
				if (cur == null) {
					for (int j = i + 1; j < 8; j++)
						if (inv.getItem(j) != null) {
							inv.setItem(i, inv.getItem(j));
							inv.setItem(j, null);
							changed = true;
							continue loop1;
						}
					continue loop1;
				}
				// recombine stacks
				if (cur.getAmount() < cur.getMaxStackSize())
					loop2: for (int j = i + 1; j < 8; j++)
						if (inv.getItem(j) != null && inv.getItem(j).isSimilar(cur)) {
							int jSize = inv.getItem(j).getAmount();
							int newSize = cur.getAmount() + jSize;
							if (newSize > cur.getMaxStackSize()) {
								inv.getItem(j).setAmount(jSize - (cur.getMaxStackSize() - cur.getAmount()));
								cur.setAmount(cur.getMaxStackSize());
								changed = true;
								break loop2;
							} else {
								cur.setAmount(cur.getAmount() + jSize);
								inv.setItem(j, null);
							}
							changed = true;
						}

			}
			if (changed)
				restackInv(inv);
		}
	}
}
