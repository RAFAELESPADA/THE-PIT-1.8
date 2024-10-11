package me.rafaelauler.ss;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
  Main plugin;
  
  public PlayerListener(Main plugin) {
    this.plugin = plugin;
  }
  @EventHandler(priority = 32)
  public void onTab(TabCompleteEvent e) {
    String[] args = e.getCursor().toLowerCase().split(" ");
    if (args.length >= 1)
      if (args[0].startsWith("/"))
        if (args[0].startsWith("/btp") && e
          .getCursor().contains(" ")) {
          e.getSuggestions().clear();
          ProxiedPlayer p = (ProxiedPlayer)e.getSender();
          if (args.length == 1) {
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
              e.getSuggestions().add(all.getName()); 
            return;
          } 
          if (args.length == 2 && getSpace(e.getCursor()) == 1) {
            addSuggestions(e, args);
            return;
          } 
          if (args.length == 2) {
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
              e.getSuggestions().add(all.getName()); 
            return;
          } 
          if (args.length == 3 && getSpace(e.getCursor()) == 2)
            addSuggestions(e, args); 
        }   
  }
  
  private void addSuggestions(TabCompleteEvent e, String[] args) {
    String check = args[args.length - 1];
    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
      if (all.getName().toLowerCase().startsWith(check))
        e.getSuggestions().add(all.getName()); 
    } 
  }
  
  public static int getSpace(String s) {
    int space = 0;

    for (int i = 0; i < s.length(); i++) {
    if (Character.isWhitespace(s.charAt(i)))
        space++; 
    } 
    return space;
  }

    
  @EventHandler
  public void onServerKickEvent(ServerKickEvent ev) {
    ServerInfo kickedFrom = null;
    if (ev.getPlayer().getServer() != null) {
      kickedFrom = ev.getPlayer().getServer().getInfo();
    } else if (this.plugin.getProxy().getReconnectHandler() != null) {
      kickedFrom = this.plugin.getProxy().getReconnectHandler().getServer(ev.getPlayer());
    } else {
      kickedFrom = AbstractReconnectHandler.getForcedHost(ev.getPlayer().getPendingConnection());
      if (kickedFrom == null)
        kickedFrom = ProxyServer.getInstance().getServerInfo(ev.getPlayer().getPendingConnection().getListener().getDefaultServer()); 
    } 
    ServerInfo kickTo = this.plugin.getProxy().getServerInfo("lobby");
    ServerInfo kickTo2 = this.plugin.getProxy().getServerInfo("skywars-lobby");
    ServerInfo kickTo3 = this.plugin.getProxy().getServerInfo("bedwars-lobby");
    if (kickedFrom != null && kickedFrom.equals(kickTo))
      return; 
    else if (kickedFrom.getName().contains("skywars-solo") || kickedFrom.getName().contains("skywars-dupla")) {
    	ev.setCancelled(true);
        ev.setCancelServer(kickTo2);
            ev.getPlayer().sendMessage(ChatColor.GREEN + "Retornado ao lobby de skywars"); 
            return;
    }
    else if (kickedFrom.getName().contains("bedwars-solo") || kickedFrom.getName().contains("bedwars-dupla")) {
    	ev.setCancelled(true);
        ev.setCancelServer(kickTo3);
            ev.getPlayer().sendMessage(ChatColor.GREEN + "Retornado ao lobby de bedwars"); 
            return;
    }
    else {



          ev.setCancelled(true);
          ev.setCancelServer(kickTo);
         
         
      } 
    
  }
  public void onMessageReceived(PluginMessageEvent e) {
      BungeeCord.getInstance().getLogger().severe("Received message from " + e.getTag());
 
      if(e.getTag().equals("BungeeCord")) {
          DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
          try {
              ServerInfo server = BungeeCord.getInstance().getPlayer(e.getReceiver().toString()).getServer().getInfo();
              String channel = in.readUTF();
         
              if(channel.equals("BungeeTeleport")) {
             
                  
                     
                     
                  
               }
         
          } catch(IOException er) {
              er.printStackTrace();
          }
      }
  }

}
