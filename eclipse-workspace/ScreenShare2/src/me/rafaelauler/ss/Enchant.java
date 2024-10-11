package me.rafaelauler.ss;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import net.luckperms.api.LuckPerms;

public class Enchant implements CommandExecutor {
    private final BukkitMain plugin;
    private final LuckPerms luckPerms;

    public Enchant(BukkitMain plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    	Player p = (Player)sender;
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Especifique um encantamento!");
            return true;
        }
        if (!Bukkit.getServer().getName().equals("Prison")) {

            sender.sendMessage(ChatColor.RED + "SERVER INVALIDO!");
        	return true;
        }
        if (!(p.getInventory().getItemInMainHand().getType() == Material.IRON_PICKAXE || p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE)) {

            sender.sendMessage(ChatColor.RED + "O item deve ser uma picareta de ferro ou diamante!");
        	return true;
        }
else if (args[0].equals("eff")) {
	p.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.DIG_SPEED, p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DIG_SPEED) + 1);
}
else if (args[0].equals("fortune")) {
	
	p.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1);
}
else if (args[0].equals("inq")) {
	
	p.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.DURABILITY, p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY) + 1);
}
return false;
}
}