package io.github.syst3ms.blockyhockey.team.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum TeamRole {
	OFFENSE("Offense", Material.DIAMOND_HOE),
	DEFENSE("Defense", Material.DIAMOND_HOE),
	GOALIE("Goalie", Material.GOLD_HOE),
	NONE(null, null);

	private final ItemStack stickItem;
	private final String name;

	TeamRole(String name, Material material) {
		this.name = name;
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name + " Stick");
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
		this.stickItem = item;
	}

	public String getName() {
		return name;
	}

	public ItemStack getStickItem() {
		return stickItem;
	}
}
