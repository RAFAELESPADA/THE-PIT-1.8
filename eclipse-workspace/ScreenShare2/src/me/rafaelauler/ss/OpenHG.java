package me.rafaelauler.ss;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class OpenHG extends Command {
  public OpenHG() {
    super("openevento", null, new String[] { "abrirevento" });
  }
	public static boolean istoggled = false;
	
  public void execute(CommandSender sender, String[] args) {
    ProxiedPlayer p = (ProxiedPlayer)sender;
    if (!(sender instanceof ProxiedPlayer))
      p.sendMessage("Apenas jogadores podem fazer isto."); 
    if (p.hasPermission("kombo.cmd.evento")) {
      if (!istoggled) {
        istoggled = true;
        p.sendMessage("SALA ABERTA");
      } 
      else {
    	  istoggled = false;
          p.sendMessage("SALA FECHADA");  
      }
    } 
  }
}