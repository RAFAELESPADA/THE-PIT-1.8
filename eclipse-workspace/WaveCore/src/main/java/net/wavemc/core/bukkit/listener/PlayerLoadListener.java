package net.wavemc.core.bukkit.listener;

import lombok.AllArgsConstructor;
import net.wavemc.core.Wave;
import net.wavemc.core.bukkit.account.WavePlayer;
import net.wavemc.core.bukkit.WaveBukkit;

import net.wavemc.core.storage.StorageConnection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import java.sql.SQLException;

@AllArgsConstructor
public class PlayerLoadListener implements Listener {

    private final WaveBukkit plugin;

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {

        WavePlayer wavePlayer = plugin.getPlayerManager().getPlayer(event.getPlayer().getName());
        if (wavePlayer.getPvp().getUuid() != event.getPlayer().getUniqueId().toString()) {
            wavePlayer.getPvp().setUuid(event.getPlayer().getUniqueId().toString());
        }
        
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        WavePlayer wavePlayer = plugin.getPlayerManager().getPlayer(player.getName());
        if (wavePlayer.getPvp().getUuid() != event.getPlayer().getUniqueId().toString()) {
            wavePlayer.getPvp().setUuid(event.getPlayer().getUniqueId().toString());
        }
Wave.getInstance().getExecutorService().submit(() -> {
            try (StorageConnection storageConnection = plugin.getStorage().newConnection()) {
                plugin.getPlayerManager().getController().load(wavePlayer, storageConnection);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }
}
