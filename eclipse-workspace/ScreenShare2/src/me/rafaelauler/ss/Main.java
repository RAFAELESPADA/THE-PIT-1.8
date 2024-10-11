package me.rafaelauler.ss;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;

import com.google.common.io.ByteStreams;

import lombok.SneakyThrows;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {
	public static Main instance;
	private Configuration config;

	  public static String channel = "bungee:teleport";
	  @SneakyThrows
	    @Override
	    public void onEnable() {
		  ToggleFake.istoggled = true;
		  OpenHG.istoggled = false;

	       new NodeEvento(this, LuckPermsProvider.get()).register();


	        ProxyServer.getInstance().registerChannel(channel);

	        loadCommands(getProxy().getPluginManager());
instance = this;
	            }

	  public static Main getInstance() {
		    return instance;
		  }
	  public void onDisable() {
		    this.config = null;
		  }
		  
		  public void loadConfig() {
		    try {
		      this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource("config.yml"));
		    } catch (IOException e) {
		      getLogger().log(Level.SEVERE, "Exception while reading config", e);
		    } 
		  }
		  public void scMandarMsg(ProxiedPlayer p, String message) {
			    for (ProxiedPlayer player : getProxy().getPlayers()) {
			      if (!player.hasPermission("utils.staffchat.use") || 
			        player == null)
			        continue; 
			      player.sendMessage((BaseComponent)new TextComponent("§6[StaffChat] §7" + p.getName() + "§d: §f" +  message));
			      } 
			  }

		    
		    
		  @EventHandler
		  public void onChat(ChatEvent e) {
		    ProxiedPlayer p = (ProxiedPlayer)e.getSender();
		    if (StaffChat.sc.contains(p.getName().toLowerCase()) && p.hasPermission("utils.staffchat.use") && 
		      !e.getMessage().startsWith("/")) {
		      for (ProxiedPlayer player : getProxy().getPlayers()) {
		        if (!player.hasPermission("utils.staffchat.use") || 
		          player == null)
		          continue; 
		        player.sendMessage((BaseComponent)new TextComponent("§6[StaffChat] §7" + p.getName() + "§d: §f" +  e.getMessage()));
		      } 
		      e.setCancelled(true);
		    } 
		  }

		  public File loadResource(String resource) {
		    File folder = getDataFolder();
		    if (!folder.exists())
		      folder.mkdir(); 
		    File resourceFile = new File(folder, resource);
		    try {
		      if (!resourceFile.exists()) {
		        resourceFile.createNewFile();
		        try(InputStream in = getResourceAsStream(resource); 
		            OutputStream out = new FileOutputStream(resourceFile)) {
		          ByteStreams.copy(in, out);
		        } 
		      } 
		    } catch (Exception e) {
		      getLogger().log(Level.SEVERE, "Exception while writing default config", e);
		    } 
		    return resourceFile;
		  }
		  
		  public Configuration getConfig() {
		    return this.config;
		  }
		  
		    
		  
	  @EventHandler
	  public void onPing2(ProxyPingEvent event)
	  {
		  if (!ToggleFake.istoggled) {
		  Random r = new Random();
	      ServerPing ping = event.getResponse();
	      ServerPing.Players current = ping.getPlayers();
	      Players players = ping.getPlayers();
	      players.setOnline(current.getOnline() + 4 + r.nextInt(25));
	      ping.setPlayers(players);
	      event.setResponse( ping );
	  }
	  }
		

	    private void loadCommands(PluginManager pluginManager) {
	    	 pluginManager.registerCommand(this, new GO());
	    	 
	    	 pluginManager.registerCommand(this, new Aviso());
	    	 getProxy().getPluginManager().registerListener(this, this);
	    	   getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
	    	    getProxy().getPluginManager().registerCommand(this, new ReloadCommand(this));
	    	    getProxy().getPluginManager().registerCommand(this, new TempoGrupo());
	    	 pluginManager.registerCommand(this, new BSudo());
	    	 pluginManager.registerCommand(this, new BTP());
	    	 pluginManager.registerCommand(this, new PingCommand());
	    	 pluginManager.registerCommand(this, new ListarUsuarios());
	    	 pluginManager.registerCommand(this, new Report());
	    	 pluginManager.registerCommand(this, new Grupo());
	    	 pluginManager.registerCommand(this, new ToggleFake());

	    	 pluginManager.registerCommand(this, new StaffChat(this));
	    	 pluginManager.registerCommand(this, new HG());
	    	 pluginManager.registerCommand(this, new OpenHG());
	    	 getLogger().info("IP da Host: " + getIpLocalHost());
	    }
	    protected String getIpLocalHost() {
	        try {
	            return (new BufferedReader(new InputStreamReader((new URL("http://checkip.amazonaws.com")).openStream())))
	                    .readLine();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

}

