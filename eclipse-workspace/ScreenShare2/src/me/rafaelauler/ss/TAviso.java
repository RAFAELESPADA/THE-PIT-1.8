package me.rafaelauler.ss;



import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TAviso extends Command {
  public TAviso() {
    super("taviso", null, new String[] { "talerta" });
  }
  
  public void execute(CommandSender sender, String[] args) {
    ProxiedPlayer p = (ProxiedPlayer)sender;
    if (!(sender instanceof ProxiedPlayer))
      p.sendMessage("Apenas jogadores podem fazer isto."); 
    if (p.hasPermission("kombo.cmd.evento")) {
      if (args.length == 0) {
        sender.sendMessage("§cUso incorreto. Use /talerta <mensagem>.");
        return;
      } 
      Title t2 = ProxyServer.getInstance().createTitle();
      String msg = "" + String.join(" ", (CharSequence[])args);
      for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
    	  player.sendTitle(t2.title(TextComponent.fromLegacyText("§b§lAVISO")).subTitle(TextComponent.fromLegacyText("§f" + msg)).stay(120));
       
    } else {
      p.sendMessage("§cVocê não tem autorização para isso.");
    } 
  }
}
