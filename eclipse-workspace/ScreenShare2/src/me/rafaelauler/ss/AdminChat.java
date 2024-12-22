package me.rafaelauler.ss;



import java.util.ArrayList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class AdminChat extends Command {
  public static ArrayList<String> sc = new ArrayList<>();
  
  public AdminChat(Plugin plugin) {
    super("ac", null, new String[] { "adminchat" });
  }
  
  public void execute(CommandSender sender, String[] args) {
    ProxiedPlayer p = (ProxiedPlayer)sender;
    if (!sender.hasPermission("tag.gerente") || !sender.hasPermission("tag.admin")) {
      sender.sendMessage((BaseComponent)new TextComponent("§cVocê não é gerente ou superior."));
      return;
    } 
    if (args.length > 0) {
      String message = String.join(" ", (CharSequence[])args);
      Main.getInstance().acMandarMsg(p, message);
    } else if (args.length == 0) {
      sender.sendMessage((BaseComponent)new TextComponent("§cUtilize /ac <Mensagem>"));
      sc.remove(p.getName().toLowerCase());
   
  }
  }
}

