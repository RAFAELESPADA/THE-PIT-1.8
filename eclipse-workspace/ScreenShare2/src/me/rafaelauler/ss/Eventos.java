package me.rafaelauler.ss;



	import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

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
          if (node.getKey() == "*" && !user.getPrimaryGroup().equalsIgnoreCase("diretor"))  {
        	  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + user.getUsername() + " Conseguiu OP (*) sem autorização (Automático)");
          }
    }
    }
