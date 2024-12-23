package me.rafaelauler.ss;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.aurelium.auraskills.api.event.skill.XpGainEvent;

public class AuraListener implements Listener {



@EventHandler
public void event(XpGainEvent e) {
	if (Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
	if (!Bukkit.getServer().getName().equals("Rankup")) {
    	return;
    }
    if (!(e.getPlayer().getWorld().equals(Bukkit.getWorld("world")))) {
		return;
    }
    if (e.getPlayer().getLocation().getX() > 8000) {
	e.setCancelled(true);
}
	}
}
}
