package me.rafaelauler.ss;



import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import dev.aurelium.auraskills.api.AuraSkillsBukkit;
import dev.aurelium.auraskills.api.event.skill.XpGainEvent;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;








public class BukkitMain extends JavaPlugin implements PluginMessageListener, Listener {

    public static BukkitMain plugin;
    private static String channel2 = "BungeeTeleport";
    private LuckPerms luckPerms;
    
    public static AuraSkillsBukkit getSkillsAPI() {
    	AuraSkillsBukkit auraSkillsBukkit = AuraSkillsBukkit.get();
    	return auraSkillsBukkit;
    }
    @Override
    public void onEnable() {
    	  plugin = this;
registerEvents();
if (MCVersion.get().isInferior(MCVersion.v1_13)) {
    channel2 = "bungee:teleport"; 
}
if (!Validated.validate()) {
	Bukkit.shutdown();
}
this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);
  Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel2);
  new Ritual();
  Bukkit.getMessenger().registerIncomingPluginChannel(this, channel2, this);
  Bukkit.getServer().getPluginManager().registerEvents(this, this);
  Bukkit.getConsoleSender().sendMessage("[TELEPORT] CANAL DO BUNGEE " + channel2 + " REGISTRADO");
  new Eventos(this, this.luckPerms).register();
  getCommand("tag").setExecutor(new TagCommand());
  getCommand("report").setExecutor(new ReportCMD());
  getCommand("adquirirvip").setExecutor(new AdquirirVip());

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
     	
    	pm.registerEvents(new Eventos(this, this.luckPerms), this);
    }

public static void darEfeito(Player p, PotionEffectType tipo, int duracao, int level)
/*     */   {
/* 349 */     p.addPotionEffect(new PotionEffect(tipo, 20 * duracao, level));
/*     */   }
@EventHandler    
public void onNodggg(BlockBreakEvent e) {
	if (!(e.getBlock().getType().toString().contains("_ORE") || e.getBlock().getType().toString().contains("STONE") || e.getBlock().getType().equals(Material.PRISMARINE))) {	
	
	return;
	}
	if (!(e.getPlayer().getWorld().equals(Bukkit.getWorld("minas")))) {	
		
		return;
		}

/*  46 */         Random rand = new Random();
/*  47 */         int percent = rand.nextInt(100);

/*  47 */         float percent2 = rand.nextFloat(100);
/*  48 */         if (percent <= 20) {
		Eventos.getRankupAPI().addFragmentos(e.getPlayer(), 4);	  
		
}
/*  48 */         if (percent <= 1) {
	Eventos.getRankupAPI().addFragmentos(e.getPlayer(), 15);	  
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
@EventHandler
public void event(XpGainEvent e) {
    if (!(e.getPlayer().getWorld().equals(Bukkit.getWorld("world")))) {
		return;
    }
    if (e.getPlayer().getLocation().getX() > 8000) {
	e.setCancelled(true);
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

