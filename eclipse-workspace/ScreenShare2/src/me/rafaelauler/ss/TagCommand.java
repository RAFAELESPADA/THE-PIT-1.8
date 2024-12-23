package me.rafaelauler.ss;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neznamy.tab.api.TabAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TagCommand implements CommandExecutor {

	Map<String, Long> cooldowns = new HashMap<String, Long>();

	  public static HashMap<Player , String> tagatual = new HashMap();
    @SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        
       
        LuckPerms api = LuckPermsProvider.get();
       
        Player player = (Player) sender;
        if (args.length == 0) {

           
            TextComponent finalComponent = new TextComponent(ChatColor.GREEN + "As suas tags disponíveis são: ");
            List<PlayerGroup> rolesAvaliable = (List<PlayerGroup>)PlayerGroup.getRoles().stream().filter(role -> (player.hasPermission(role.getPermission()) || role.getPermission().isEmpty())).collect(Collectors.toList());
            for (int i = 0; i < rolesAvaliable.size(); i++) {
              PlayerGroup role = rolesAvaliable.get(i);
              TextComponent component = new TextComponent();
              component.setText(StringUtils.formatColors(role.getColoredName().replace("plus", "+")));
              component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(" [" + role.getColoredName().replace("plus", "+") + "] " + player.getName())));
              component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + StringUtils.stripColors(role.getName().replace("plus", "+"))));
              if (i != rolesAvaliable.size() - 1)
                component.addExtra((BaseComponent)new TextComponent("§f, ")); 
              finalComponent.addExtra((BaseComponent)component);
            } 
            finalComponent.addExtra((BaseComponent)new TextComponent("§f."));
            player.spigot().sendMessage((BaseComponent)finalComponent);
            return true;
          } 
        	


        	
        
       
Player p = (Player)sender;


            
        


        if (!player.hasPermission("tag." + args[0])) {
            player.sendMessage("§cVocê não tem essa tag ou ela não existe.");
            return true;
        }
        if (cooldowns.containsKey(player.getName() + "EPT")) {
            if (cooldowns.get(player.getName() + "EPT") > System.currentTimeMillis()) {
                long timeleft = (cooldowns.get(player.getName() + "EPT") - System.currentTimeMillis()) / 1000;
                player.sendMessage(ChatColor.RED + "Espere " + timeleft +" segundos para mudar sua tag novamente!");
                return true;
            }
        }

try {
	
        String prefix = api.getGroupManager().getGroup(args[0]).getCachedData().getMetaData().getPrefix();

        // Get an OfflinePlayer object for the player

       


        // Load, modify & save the user in LuckPerms.
        api.getUserManager().modifyUser(player.getUniqueId(), (User user) -> {

            // Remove all other prefixes the user had before.
            user.data().clear(NodeType.PREFIX::matches);

            // Find the highest priority of their other prefixes
            // We need to do this because they might inherit a prefix from a parent group,
            // and we want the prefix we set to override that!
            Map<Integer, String> inheritedPrefixes = user.getCachedData().getMetaData(QueryOptions.nonContextual()).getPrefixes();
            int priority = inheritedPrefixes.keySet().stream().mapToInt(i -> i + 10).max().orElse(10);

            // Create a node to add to the player.
            Node node = PrefixNode.builder(prefix, priority).build();

            // Add the node to the user.
            user.data().add(node);
            
            // Tell the sender.
            cooldowns.put(player.getName() + "EPT", System.currentTimeMillis() + 7 * 1000);
            sender.sendMessage("§a§lTAG: §fSua tag foi alterada para " + (args[0].equalsIgnoreCase("Membro") || (args[0].equalsIgnoreCase("default")) ? "§7[Membro]" : ChatColor.RESET + prefix.replace("&", "§")));
            if (Bukkit.getPluginManager().isPluginEnabled("TAB")) {
            	   TabAPI apitab = TabAPI.getInstance();
            apitab.getPlayer(player.getName()).setTemporaryGroup(args[0]);
            tagatual.put(player, args[0]);
            }
            Bukkit.getConsoleSender().sendMessage(player.getName() + " alterou a tag para " + args[0]);
        });
} catch (NullPointerException e) {
	sender.sendMessage(ChatColor.RED + "§cVocê não tem essa tag ou ela não existe.");
}

     
		return false;

        }
    

}