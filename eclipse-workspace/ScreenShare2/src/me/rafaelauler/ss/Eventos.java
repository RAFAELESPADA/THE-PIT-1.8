package me.rafaelauler.ss;



	import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.ystoreplugins.product.yminas.MinaAPIHolder;
import br.com.ystoreplugins.product.yrankup.RankupAPIHolder;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;


	public class Eventos implements Listener {
	    private final BukkitMain plugin;
	    private final LuckPerms luckPerms;

	    public Eventos(BukkitMain plugin, LuckPerms luckPerms) {
	        this.plugin = plugin;
	        this.luckPerms = luckPerms;
	    }

	    public void register() {
	        EventBus eventBus = this.luckPerms.getEventBus();
	        eventBus.subscribe(this.plugin, NodeAddEvent.class, this::onNodeAdd);
	    }

	    @EventHandler
		public void onJoin(PlayerLoginEvent e) {
			Player player = e.getPlayer();
			if (!player.hasPermission("manutencao.bypass") && !Manutencao.istoggled) {
			e.disallow(Result.KICK_OTHER, ChatColor.RED + "Esse servidor está em manutenção.");
		}
		}
	    @EventHandler
		public void otnShot(EntityDamageByEntityEvent e) {
			
				
				if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
					
					Player damagedPlayer = (Player) e.getEntity();
					Arrow arrow = (Arrow) e.getDamager();
					
					if (arrow.getShooter() != null && arrow.getShooter() instanceof Player) {
						
						Player shooter = (Player) arrow.getShooter();
						
						// ARROW HEALTH MESSAGE
						
						if (damagedPlayer.getName() != shooter.getName()) {
							
							new BukkitRunnable() {
								
								@Override
								public void run() {
									
									double health = Math.round(damagedPlayer.getHealth() * 10.0) / 10.0;
									
										if (health != 20.0) {	
											
											shooter.sendMessage(damagedPlayer.getName() + ChatColor.YELLOW + " está com " + health + " corações de vida!");									
										}						
																	}							
								}
								
							.runTaskLater(BukkitMain.plugin, 2L);
							
						}
					}
				}
			}
	    
	    @EventHandler
	    public void event(org.bukkit.event.player.PlayerInteractEvent e) {
	        Player p = e.getPlayer();
	        if (!Bukkit.getServer().getName().equals("Prison")) {
	        	return;
	        }
	        if(!p.isSneaking()) return;
	        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	        ItemStack item = p.getInventory().getItemInMainHand();
	        if(item == null) return;
	        Material t = item.getType(); // type of item
	        if(t == Material.DIAMOND_PICKAXE || t == Material.IRON_PICKAXE) {
	        p.performCommand("dm open upgrades");

	        }
	    }
	    public static MinaAPIHolder getMinaAPI() {
	        try {
	        	
	           RegisteredServiceProvider<MinaAPIHolder> rsp = Bukkit.getServer().getServicesManager().getRegistration(MinaAPIHolder.class);
	           return rsp == null ? null : (MinaAPIHolder)rsp.getProvider();
	        } catch (Throwable var1) {
	           return null;
	        }
	     }
	    public static RankupAPIHolder getRankupAPI() {
	        try {
	           RegisteredServiceProvider<RankupAPIHolder> rsp = Bukkit.getServer().getServicesManager().getRegistration(RankupAPIHolder.class);
	           return rsp == null ? null : (RankupAPIHolder)rsp.getProvider();
	        } catch (Throwable var1) {
	           return null;
	        }
	     }


	    

    public void onNodeAdd(NodeAddEvent e) {
        // Check if the event was acting on a User
        if (!e.isUser()) {
            return;
        }

        // Check if the node was a permission node
        Node node = e.getNode();
        if (node.getType() != NodeType.PERMISSION) {
            return;
        }

        net.luckperms.api.model.user.User user = (net.luckperms.api.model.user.User) e.getTarget();
        for (Player player : Bukkit.getOnlinePlayers())
        	if (player.hasPermission("utils.staffchat.use")) {

        	      String msg = "§b§lAVISO §f" + user.getUsername() + " recebeu a permissão §e" + node.getKey();
          player.sendMessage(msg); 
        	}
          if (node.getKey() == "*" && !user.getPrimaryGroup().equalsIgnoreCase("dono"))  {
        	  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + user.getUsername() + " Conseguiu OP (*) sem autorização (Automático)");
          }
    }
    }
