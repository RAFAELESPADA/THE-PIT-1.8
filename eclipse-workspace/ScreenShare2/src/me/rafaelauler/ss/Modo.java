package me.rafaelauler.ss;



/*    */ 


import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public final class Modo
  implements Listener, CommandExecutor
{
  public static ArrayList<Player> embuild = new ArrayList();
  LuckPerms api = LuckPermsProvider.get();
  
	  public static ArrayList<Player> isstaff = new ArrayList();
	  public static HashMap<String, String> savegroup = new HashMap<String, String>();

	  public static HashMap<String, Boolean> isop = new HashMap<String, Boolean>();
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("modo")) {
      if (!p.hasPermission("modo.staff")) {
    	  p.sendMessage(ChatColor.RED + "Você não é staff!");
    	  return true;
      }
        if (args.length == 0)
        {
          if (!isstaff.contains(p))
          {
        	isstaff.add(p);  
            embuild.add(p);
            isop.put(p.getName(), p.isOp());
            p.setOp(false);
            
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " clear");

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission settemp modo.staff true 6h");
            p.sendMessage(String.valueOf("§c§lSTAFF") + "§a Você saiu do modo staff");
            savegroup.put(p.getName(), api.getUserManager().getUser(p.getUniqueId()).getPrimaryGroup());
            p.sendMessage(String.valueOf("§c§lSTAFF") + "§a Você perdeu suas permissões");
            Bukkit.broadcast(ChatColor.GREEN + "STAFF " + p.getName() + " saiu do modo staff e perdeu suas permissões temporariamente", "staffchat.use");
          }

          else
          {
            embuild.remove(p);
        	isstaff.remove(p);  
            p.sendMessage(String.valueOf("§c§lBUILD") + "§cVocê voltou pro modo staff");
            api.getUserManager().getUser(p.getUniqueId()).setPrimaryGroup(savegroup.get(p.getName()));
            p.setOp(isop.get(p.getName()));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent set " + savegroup.get(p.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unsettemp modo.staff");
            Bukkit.broadcast(ChatColor.GREEN + "STAFF " + p.getName() + " entrou no modo staff e ganhou suas permissões novamente", "staffchat.use");
            p.sendMessage(String.valueOf("§c§lBUILD") + "§cSuas permissões retornaram");
            api.getUserManager().loadUser(p.getUniqueId());
          }
  
	
  
}
	return false;
  }
	return false;}
}


