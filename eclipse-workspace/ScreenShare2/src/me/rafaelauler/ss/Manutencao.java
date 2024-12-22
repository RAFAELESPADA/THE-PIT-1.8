package me.rafaelauler.ss;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.luckperms.api.LuckPerms;

public class Manutencao implements CommandExecutor {
    private final BukkitMain plugin;
    private final LuckPerms luckPerms;
    public static boolean istoggled = true;
	
    public Manutencao(BukkitMain plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
if (!sender.hasPermission("cmd.manutencao")) {
	sender.sendMessage(ChatColor.RED + "SEM AUTORIZA��O!");
	return true;
}
if (!istoggled) {
    istoggled = true;
    sender.sendMessage("MANUTEN��O DESLIGADA");
  } 
  else {
	  istoggled = false;
      sender.sendMessage("SERVIDOR EST� AGORA EM MANUTEN��O");  
      for (Player p2 : Bukkit.getOnlinePlayers()) {
    	  if (!p2.hasPermission("manutencao.bypass")) {
    		  p2.kickPlayer(ChatColor.RED + "Esse servidor est� em manuten��o.");
    	  }
      }
  }
return false;
    }
}
