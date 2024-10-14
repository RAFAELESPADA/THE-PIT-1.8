package me.rafaelauler.ss;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ystoreplugins.ypoints.api.yPointsAPI;

import net.md_5.bungee.api.ChatColor;


public class AdquirirVip implements CommandExecutor {
    
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
        if (!(sender instanceof Player)) {
            return true;
	}
        if (args.length < 1) {
            sender.sendMessage("§b/adquirirvip VIP/VIP+");
            sender.sendMessage("§eVIP ( 1 SEMANA ) CUSTA 4000 CASH");
            sender.sendMessage("§eVIP+ ( 1 SEMANA ) CUSTA 8000 CASH");
            return true;
        }
        String targetName = args[0];
 if (targetName.equalsIgnoreCase("vip")) {
	 if (yPointsAPI.getBalance(sender.getName()) > 4000.0) {
		 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + sender.getName() + " parent addtemp vip 7d");
		 sender.sendMessage(ChatColor.GREEN + " Você adquiriu VIP por 1 semana por 4000 de cash");
		 yPointsAPI.withdraw(sender.getName(), 4000, true);
	 } else {
		 sender.sendMessage(ChatColor.RED + " Você não tem cash o suficiente");
				 
	 }
	 
	 return true;
 } else if (targetName.equalsIgnoreCase("vip+")) {
	 if (yPointsAPI.getBalance(sender.getName()) > 8000.0) {
		 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + sender.getName() + " parent addtemp vip+ 7d");
		 yPointsAPI.withdraw(sender.getName(), 8000, true);
		 sender.sendMessage(ChatColor.GREEN + " Você adquiriu VIP por 1 semana por 8000 de cash");
	 } else {
		 sender.sendMessage(ChatColor.RED + " Você não tem cash o suficiente");
				 
	 }
	 
	 return true;
 }
return false;
	}



}