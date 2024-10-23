package net.wavemc.core.bukkit.account;

import lombok.Getter;
import net.wavemc.core.bukkit.WaveBukkit;
import net.wavemc.core.bukkit.account.provider.PlayerPvP;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class WavePlayerManager {

    @Getter private final List<WavePlayer> players;
    @Getter private final WavePlayerController controller;

    public WavePlayerManager(WaveBukkit plugin) {
        this.controller = new WavePlayerController(plugin);
        this.players = controller.load();
    }

    public boolean containsPlayer(String username) {
        return this.players.stream().anyMatch(
                wavePlayer -> wavePlayer.getName().equalsIgnoreCase(username)
        );
    }

    public WavePlayer getPlayer(String name) {
        return this.players.stream().filter(
                wavePlayer -> wavePlayer.getName().equalsIgnoreCase(name)
        ).findFirst().orElseGet(() -> {
            WavePlayer wavePlayer = new WavePlayer(name, Objects.requireNonNull(Bukkit.getPlayer(name)).getUniqueId(), true, new PlayerPvP(0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0 ,0 ,0,0,0 ,0, 0,0,0));
            this.players.add(wavePlayer);
            return wavePlayer;
        });
    }
}