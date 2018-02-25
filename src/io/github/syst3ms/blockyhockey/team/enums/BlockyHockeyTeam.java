package io.github.syst3ms.blockyhockey.team.enums;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Colorable;

public enum BlockyHockeyTeam {
	PIGS("Pigs", "The Pigs", ChatColor.LIGHT_PURPLE, DyeColor.PINK),
	BOX("Box", "Team Box", ChatColor.GREEN, DyeColor.LIME),
	SPARTANS("Spartans", "The Spartans", ChatColor.GOLD, DyeColor.YELLOW),
	TORNADOES("Tornadoes", "The Tornadoes", ChatColor.GRAY, DyeColor.SILVER),
	NONE(null, null, null, null);

	private final ChatColor chatColor;
	private final DyeColor dyeColor;
	private final String name;
	private final String displayName;
	private final String chatName;

	BlockyHockeyTeam(String name, String displayName, ChatColor chatColor, DyeColor dyeColor) {
		this.displayName = displayName;
		this.chatColor = chatColor;
		this.dyeColor = dyeColor;
		this.name = name;
		this.chatName = chatColor + displayName + ChatColor.RESET;
	}

	public ChatColor getChatColor() {
		return chatColor;
	}

	public String getDisplayName() {
		return displayName;
	}

	public DyeColor getDyeColor() {
		return dyeColor;
	}

	public String getChatName() {
		return chatName;
	}

	public ItemStack[] getArmor() {
		ItemStack[] armor = new ItemStack[4];
		ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
		ItemMeta helmetMeta = helmet.getItemMeta();
		helmetMeta.setDisplayName("Hockey Helmet");
		helmet.setItemMeta(helmetMeta);
		armor[0] = helmet;
		for (int i = 1; i < 4; i++) {
			ItemStack item = null;
			String partName = null;
			switch (i) {
				case 1:
					item = new ItemStack(Material.LEATHER_CHESTPLATE);
					partName = name + " Chestplate";
					break;
				case 2:
					item = new ItemStack(Material.LEATHER_LEGGINGS);
					partName = name + " Leggings";
					break;
				case 3:
					item = new ItemStack(Material.LEATHER_BOOTS);
					partName = name + " Boots";
					break;
			}
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(partName);
			meta.setUnbreakable(true);
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			((Colorable) meta).setColor(dyeColor);
			item.setItemMeta(meta);
			armor[i] = item;
		}
		return armor;
	}
}

