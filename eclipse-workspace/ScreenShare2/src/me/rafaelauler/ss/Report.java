package me.rafaelauler.ss;


import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import net.helix.core.util.HelixCooldown;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Report extends Command {
    public Report() {
        super("report", null, new String[] { "reportar", "denunciar" });
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer))
            return;
        if (args.length < 2) {
            sender.sendMessage((BaseComponent)new TextComponent("§b/report <Player> <Motivo>"));
            return;
        }
        String targetName = args[0];
        ProxiedPlayer targetPlayer;
        if ((targetPlayer = ProxyServer.getInstance().getPlayer(targetName)) == null) {
            sender.sendMessage((BaseComponent)new TextComponent("§cEsse jogador está offline."));
            return;
        }
        if (impossibleToBan(targetName)) {
        	   sender.sendMessage("§cUtilize o /report apenas para denunciar jogadores.");
               return;
           
        }
        if (targetName.equalsIgnoreCase(sender.getName())) {
            sender.sendMessage("§cVocê não pode se reportar");
            return;
        }
        if (HelixCooldown.inCooldown(sender.getName(), "report")) {
            sender.sendMessage("§cEspere " + HelixCooldown.getTime(sender.getName(), "report") + " segundos para reportar novamente.");
            return;
        }
        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++)
            reasonBuilder.append(args[i]).append(" ");
        String reason = reasonBuilder.toString().trim();
        HelixCooldown.create(sender.getName(), "report", TimeUnit.SECONDS, 60L);
        TextComponent teleport = new TextComponent("§cClique §f§lAQUI §cpara se teleportar até o §cservidor do §cjogador.");
        teleport.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + targetName));
        sender.sendMessage("§cVocê reportou " + targetPlayer.getName() + " por " + reason);

        Title t = ProxyServer.getInstance().createTitle();
        ProxyServer.getInstance().getPlayers().stream().filter(online -> online.hasPermission("staffchat.use"))

                .forEach(online -> {
                    online.sendMessage((BaseComponent)new TextComponent(""));
                    online.sendMessage((BaseComponent)new TextComponent("§3Novo Report"));
                    online.sendMessage((BaseComponent)new TextComponent("§bVitíma: §6" + sender.getName()));
                    online.sendMessage((BaseComponent)new TextComponent("§bAcusado: §c"+ targetName + " §7(" + targetPlayer.getServer().getInfo().getName() + ")"));
                    online.sendMessage((BaseComponent)new TextComponent("§bMotivo: §8" + reason));
                    online.sendTitle(t.title(TextComponent.fromLegacyText("§b§lREPORT")).subTitle(TextComponent.fromLegacyText("§e§lTem um novo report para ser analisado.")).stay(200));
                    
                            online.sendMessage((BaseComponent)new TextComponent(""));
                            online.sendMessage(teleport);
                            online.sendMessage(TextComponent.fromLegacyText(" "));

                });
    }


private static boolean impossibleToBan(String nickName) {
    return Stream.of("Rafael_Auler", "Ritual123", "Ebenezer7").anyMatch(s -> s.equalsIgnoreCase(nickName));
}
}
