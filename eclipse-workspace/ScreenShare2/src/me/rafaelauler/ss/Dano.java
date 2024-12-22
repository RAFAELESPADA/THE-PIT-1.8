package me.rafaelauler.ss;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Dano implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("Console não é permitido.");
      return true;
    } 
    Player p = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("dano")) {
      if (args.length == 0) {
        p.sendMessage(ChatColor.RED + "Utilize /dano on ou /dano off");
        return true;
      } 
      if (p.hasPermission("kombo.cmd.report") && args[0].equalsIgnoreCase("on")) {
        Bukkit.broadcastMessage(ChatColor.GREEN + "O Dano foi ativado!");
        p.getWorld().setPVP(true);
      }
      if (p.hasPermission("kombo.cmd.report") && args[0].equalsIgnoreCase("off")) {
    	  Bukkit.broadcastMessage(ChatColor.RED + "O Dano foi desativado!");
      p.getWorld().setPVP(false);
      } 
      return true;
    } 
    return false;
  }
}
