package me.rafaelauler.ss;


	import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;


	public class DemoteAllStaff extends Command {
		public DemoteAllStaff() {
		    super("demoteallstaff", null, new String[] { "demotartodos" , "resetarequipe" });
		  }
		  
	  LuckPerms api = LuckPermsProvider.get();
	  public static boolean isPlayerInGroup(Player player, String group) {
	        return player.hasPermission("group." + group);
	    }
	  public void execute(CommandSender sender, String[] args) {
	    if (!sender.hasPermission("tag.dono")) {
	      sender.sendMessage(ChatColor.RED + "Você precisa ser Owner ou superior para executar esse comando.");
	      return;
	    } 
	    if (args.length < 1) {
	      sender.sendMessage(ChatColor.DARK_AQUA + "Uso correto: /demotartodos confirmar.");
	      return;
	    } 
	    if (args[0].equalsIgnoreCase("confirmar")) {
	    	Set<Group> g = api.getGroupManager().getLoadedGroups();
	    	for (Group g2 : g) {
	    		Set<String> groups = g2.getNodes().stream()
	                    .filter(NodeType.INHERITANCE::matches)
	                    .map(NodeType.INHERITANCE::cast)
	                    .map(InheritanceNode::getGroupName)
	                    .collect(Collectors.toSet());
	            for(String a : groups){
	                if(a.equalsIgnoreCase("staff") || g2.equals(api.getGroupManager().getGroup("diretor"))) {
	                for (User u : api.getUserManager().getLoadedUsers()) {
	                    if (u.getPrimaryGroup().equals(a)) {
	                    	u.setPrimaryGroup("default"); 
	                    	u.getInheritedGroups(QueryOptions.nonContextual()).remove(g2);
	                    	sender.sendMessage(ChatColor.BLUE + "Você demotou todos os staffs!");
	                    }
	                }	
	                } else {
	                	 sender.sendMessage(ChatColor.DARK_AQUA + "Uso correto: /demotartodos confirmar.");	
	                }
	            }	
	    		}
	    	}
	    
	  }
	}
	
