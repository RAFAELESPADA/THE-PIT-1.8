package me.rafaelauler.ss;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Sudo
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("sudo"))
    {
      if (!sender.hasPermission("tag.gerente"))
      {
        try
        {
          sender.sendMessage(ChatColor.DARK_RED + "No Permission!");
        }
        catch (Exception e)
        {
          sender.sendMessage(ChatColor.DARK_RED + "No Permission!");
        }
        return true;
      }
      if (args.length < 2)
      {
        sender.sendMessage(ChatColor.DARK_RED + "Argumentos incorretos! Use /sudo <Player> <comando> [-c]");
        sender.sendMessage(ChatColor.RED + "Se o -c for incluido o jogador mandara uma mensagem no lugar de um comando");
        return true;
      }
      if (Bukkit.getPlayer(args[0]) == null)
      {
        try
        {
          sender.sendMessage(ChatColor.DARK_RED + "Jogador offline!");
        }
        catch (Exception e)
        {
          sender.sendMessage(ChatColor.DARK_RED + "Jogador offline!");
        }
        return true;
      }
if (Bukkit.getPlayer(args[0]).hasPermission("tag.admin")) {
	 sender.sendMessage(ChatColor.DARK_RED + "Esse jogador não pode ser obrigado a falar nada!");
return true;
}
      Boolean sendMessage = Boolean.valueOf(false);
      String msg = "";
      for (int x = 1; args.length > x; x++)
      {
        if (args[x].equalsIgnoreCase("-c"))
        {
          sendMessage = Boolean.valueOf(true);
          break;
        }
        msg = msg + args[x] + " ";
      }
      if (!sendMessage.booleanValue()) {
        Bukkit.dispatchCommand(Bukkit.getPlayer(args[0]), msg);
        sender.sendMessage(ChatColor.DARK_RED + "Voce obrigou " +  Bukkit.getPlayer(args[0]).getName() + " a digitar /" + msg);
      } else {
        Bukkit.getPlayer(args[0]).chat(msg);
        sender.sendMessage(ChatColor.DARK_RED + "Voce obrigou " +  Bukkit.getPlayer(args[0]).getName() + " a digitar " + msg);
      }
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("consolesudo"))
    {
      if (!sender.getName().equals("Ebenezer7"))
      {
        try
        {
          sender.sendMessage(ChatColor.DARK_RED + "No Permission!");
        }
        catch (Exception e)
        {
          sender.sendMessage(ChatColor.DARK_RED + "No Permission!");
        }
        return true;
      }
      if (args.length < 1)
      {
        sender.sendMessage(ChatColor.RED + "Argumento incorreto Use! /consolesudo <command>");
        return true;
      }
      String msg = "";
      for (int x = 0; args.length > x; x++) {
        msg = msg + args[x] + " ";
      }
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg);
      sender.sendMessage(ChatColor.DARK_RED + "Voce obrigou o Console a digitar: /" + msg);
      return true;
    }
    return false;
  }
}

