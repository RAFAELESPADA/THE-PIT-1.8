package net.wavemc.core.bukkit.api;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;


import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBar {

    public static void sendActionBar(Player player, String rawMessage) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(rawMessage.replace("&", "§")));
    }

    public static void sendActionBarWithoutCheck(Player player, String rawMessage) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(rawMessage.replace("&", "§")));
    }
}