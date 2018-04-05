package io.github.syst3ms.blockyhockey.team.enums;

import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.Map;

public enum BlockyHockeyTeam {
	MEDIEVAL_MINERS(
		"Medieval Miners",
		ChatColor.GOLD,
		Color.fromRGB(255, 223, 17),
		Color.YELLOW
	),
	VANTA_VIKINGS(
		"Vanta Vikings",
		ChatColor.GRAY,
		Color.BLACK,
		Color.BLACK
	),
	STRIDENT_STRIKERS(
		"Strident Strikers",
		ChatColor.AQUA,
		Color.fromRGB(69, 126, 239),
		Color.fromRGB(91, 190, 230)
	),
	COURAGEOUS_COUGARS(
		"Courageous Cougars",
		ChatColor.LIGHT_PURPLE,
		Color.fromRGB(255, 159, 216),
		Color.WHITE
	),
	FIERY_FELONS(
		"Fiery Felons",
		ChatColor.GOLD,
		Color.fromRGB(255, 178, 0),
		Color.BLACK
	),
	WITHERING_WIZARDS(
		"Withering Wizards",
		ChatColor.DARK_PURPLE,
		Color.fromRGB(148, 58, 255),
		Color.BLACK
	),
	RAKISH_RAMBLERS(
		"Rakish Ramblers",
		ChatColor.RED,
		Color.fromRGB(255, 35, 35),
		Color.fromRGB(64, 64, 64)
	),
	DEATHLY_DAGGERS(
		"Deathly Daggers",
		ChatColor.YELLOW,
		Color.YELLOW,
		Color.GRAY
	),
	PENALTY_PIRATES(
		"Penalty Pirates",
		ChatColor.GREEN,
		Color.fromRGB(78, 218, 22),
		Color.fromRGB(64, 64, 64)
	),
	TENACIOUS_TRIDENTS(
		"Tenacious Tridents",
		ChatColor.BLUE,
		Color.BLUE,
		Color.WHITE
	),
	NONE(
		"No Team",
		ChatColor.WHITE,
		Color.WHITE,
		Color.WHITE
	);

	private static Map<BlockyHockeyTeam, ItemStack[]> armorCache = new HashMap<>();
	private final ChatColor chatColor;
	private final Color topColor;
    private final String name;
	private final Color bottomColor;
	private final String chatName;

	BlockyHockeyTeam(String name, ChatColor chatColor, Color topColor, Color bottomColor) {
		this.chatColor = chatColor;
		this.name = name;
		this.topColor = topColor;
		this.bottomColor = bottomColor;
		this.chatName = chatColor + "The " + name + ChatColor.RESET;
	}

    public String getName() {
        return name;
    }

	public ItemStack[] getArmor() {
		if (armorCache.containsKey(this))
			return armorCache.get(this);
		ItemStack[] armor = new ItemStack[4];
		ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
		ItemMeta helmetMeta = helmet.getItemMeta();
		helmetMeta.setDisplayName("Hockey Helmet");
		helmet.setItemMeta(helmetMeta);
		armor[3] = helmet;
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
		chestplateMeta.setDisplayName(name + " Chestplate");
		chestplateMeta.setColor(topColor);
		chestplate.setItemMeta(chestplateMeta);
		armor[2] = chestplate;
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta leggingMeta = (LeatherArmorMeta) leggings.getItemMeta();
		leggingMeta.setDisplayName(name + " Leggings");
		leggingMeta.setColor(bottomColor);
		leggings.setItemMeta(leggingMeta);
		armor[1] = leggings;
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta bootMeta = (LeatherArmorMeta) boots.getItemMeta();
		bootMeta.setDisplayName("Hockey Skates");
		bootMeta.setColor(Color.BLACK);
		boots.setItemMeta(bootMeta);
		armor[0] = boots;
		armorCache.put(this, armor);
		return armor;
	}

	public ChatColor getChatColor() {
		return chatColor;
	}

	public String getDisplayName() {
		return ChatColor.stripColor(chatName);
	}

	public Color getTopColor() {
		return topColor;
	}

	public String getChatName() {
		return chatName;
	}

	public Color getBottomColor() {
		return bottomColor;
	}
}

