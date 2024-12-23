package me.rafaelauler.ss;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import br.com.ystoreplugins.product.yrankup.RankupAPIHolder;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;








public class BukkitMain extends JavaPlugin implements PluginMessageListener, Listener {

    public static BukkitMain plugin;
    private static String channel2 = "BungeeTeleport";
    private LuckPerms luckPerms;
    
    

    public String ifNullEmpty(String check) {
      if (check == null)
        return ""; 
      return check;
    }
    @Override
    public void onEnable() {
    	  plugin = this;
registerEvents();
if (MCVersion.get().isInferior(MCVersion.v1_13)) {
    channel2 = "bungee:teleport"; 
}
final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
manager.addPacketListener((PacketListener)new PacketAdapter((Plugin)this, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
      public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
        	if (event.getPlayer().hasPermission("kombo.cmd.evento")) {
        		return;
        	}
          event.setCancelled(true);
          String full = ((String)event.getPacket().getStrings().read(0)).replace("  ", " ");
          String[] slices = full.split(" ");
          String complete = (full.charAt(full.length() - 1) == ' ') ? "" : slices[slices.length - 1];
          String start = ifNullEmpty(complete);
          PacketContainer fakeComplete = manager.createPacket(PacketType.Play.Server.TAB_COMPLETE);
          List<String> online = (List<String>)Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).filter(s -> s.toLowerCase().startsWith(start.toLowerCase())).collect(Collectors.toList());
          String[] players = new String[online.size()];
          for (int i = 0; i < online.size(); i++)
            players[i] = online.get(i); 
          fakeComplete.getStringArrays().write(0, players);
          manager.sendServerPacket(event.getPlayer(), fakeComplete); 
        } 
      }
    });
this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);
  Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel2);
  Bukkit.getMessenger().registerIncomingPluginChannel(this, channel2, this);
  Bukkit.getServer().getPluginManager().registerEvents(this, this);
  Bukkit.getConsoleSender().sendMessage("[TELEPORT] CANAL DO BUNGEE " + channel2 + " REGISTRADO");
  new Eventos(this, this.luckPerms).register();
  getCommand("tag").setExecutor(new TagCommand());
  getCommand("report").setExecutor(new ReportCMD());
  getCommand("adquirirvip").setExecutor(new AdquirirVip());
  getCommand("modo").setExecutor(new Modo());
  getCommand("sudo").setExecutor(new Sudo());
  getCommand("dano").setExecutor(new Dano());
  getCommand("manutencao").setExecutor(new Manutencao(this, this.luckPerms));
  getCommand("ctag").setExecutor(new cTag(this, this.luckPerms));
  getCommand("consolesudo").setExecutor(new Sudo());
  getCommand("pickencantar").setExecutor(new Enchant(this, this.luckPerms));
  getCommand("set-prefix").setExecutor(new SetPrefix(this, this.luckPerms));
    }
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("[TELEPORT] PLUGIN DESLIGADO COM SUCESSO");
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    public void registerEvents(){
    	PluginManager pm = Bukkit.getPluginManager();
    	Bukkit.getConsoleSender().sendMessage("[REPORT] EVENTOS INICIANDO");
    	pm.registerEvents(new PlayerJoin(this), this);
    	if (Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
    	pm.registerEvents(new AuraListener(), this);
    	}
     	
    	pm.registerEvents(new Eventos(this, this.luckPerms), this);
    }

public static void darEfeito(Player p, PotionEffectType tipo, int duracao, int level)
/*     */   {
/* 349 */     p.addPotionEffect(new PotionEffect(tipo, 20 * duracao, level));
/*     */   }
@EventHandler    
public void onNodggg(BlockBreakEvent e) {
	if (!Bukkit.getServer().getName().equals("Rankup")) {
    	return;
    }
	if (!(e.getBlock().getType().toString().contains("_ORE") || e.getBlock().getType().toString().contains("STONE") || e.getBlock().getType().equals(Material.PRISMARINE))) {	
	
	return;
	}
	if (!(e.getPlayer().getWorld().equals(Bukkit.getWorld("minas")))) {	
		
		return;
		}

/*  46 */         Random rand = new Random();
/*  47 */         int percent = rand.nextInt(100);
RankupAPIHolder rsp = (RankupAPIHolder) Bukkit.getServer().getServicesManager().getRegistration(RankupAPIHolder.class);
rsp = (RankupAPIHolder)((RegisteredServiceProvider<RankupAPIHolder>) rsp).getProvider();

/*  47 */         float percent2 = rand.nextFloat(100);
/*  48 */         if (percent <= 20) {
	
		rsp.addFragmentos(e.getPlayer(), 4);	  
}
/*  48 */         if (percent <= 1) {
	rsp.addFragmentos(e.getPlayer(), 15);	  
	e.getPlayer().sendMessage(ChatColor.YELLOW + "Você recebeu muitos fragmentos minerando.");
}

/*  48 */         if (percent2 <= 0.01) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bomb give bomb-small " + e.getPlayer().getName() + " 1");
		e.getPlayer().sendMessage(ChatColor.YELLOW + "Você recebeu uma bomba");
}
/*  48 */         if (percent2 <= 0.001) {
	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pontos add " + e.getPlayer().getName() + " 10");
	e.getPlayer().sendMessage(ChatColor.BLUE + "Você recebeu 10 de cash na mina");
}
/*  48 */         if (percent2 <= 0.0001) {
	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate key give " + e.getPlayer().getName() + " chave_da_caixa_de_ferro 1");
	e.getPlayer().sendMessage(ChatColor.BLUE + "Você recebeu uma chave");
}
/*  48 */         if (percent2 <= 0.001) {
	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate key give " + e.getPlayer().getName() + " chave_da_caixa_pedra 1");
	e.getPlayer().sendMessage(ChatColor.BLUE + "Você recebeu uma chave");
}
/*  48 */         if (percent2 <= 0.0001) {
	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bomb give bomb-medium " + e.getPlayer().getName() + " 1");
	e.getPlayer().sendMessage(ChatColor.YELLOW + "Você recebeu uma bomba média");
}
/*  48 */         if (percent2 <= 0.00000001) {
	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bomb give bomb-large " + e.getPlayer().getName() + " 1");
	e.getPlayer().sendMessage(ChatColor.YELLOW + "Você recebeu uma bomba larga");
	}
	}

	
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("bungee:teleportp4")) {
          return; 
        }
        String action = null;
        ArrayList<String> received = new ArrayList<>();
        BukkitMain.plugin.getLogger().log(Level.INFO, "CANAL SENDO CHAMADO!");
        
        try {
        	  ByteArrayDataInput in = ByteStreams.newDataInput(message);
            while (true) {
                action = in.readUTF();
            received.add(in.readUTF()); 
            }} catch (Exception e) {
          e.printStackTrace();
        } 
        if (action == null) {
        	 BukkitMain.plugin.getLogger().log(Level.SEVERE, "ACTION IS NULL");
          return; 
        }
        BukkitMain.plugin.getLogger().log(Level.INFO, "ACTION IS " + action);
        if (action.equalsIgnoreCase("teleport")) {
          Player from = Bukkit.getServer().getPlayer(received.get(0));
          Player to = Bukkit.getServer().getPlayer(received.get(1));
          from.teleport(to);
        } 
    }
}

