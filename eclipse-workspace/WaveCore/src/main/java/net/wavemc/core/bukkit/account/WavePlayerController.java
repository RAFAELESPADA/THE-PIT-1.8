package net.wavemc.core.bukkit.account;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.wavemc.core.bukkit.WaveBukkit;
import net.wavemc.core.bukkit.account.provider.PlayerPvP;
import net.wavemc.core.bukkit.data.HelixDataStorageController;
import net.wavemc.core.storage.StorageConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class WavePlayerController extends HelixDataStorageController<WavePlayer> {
    public WavePlayerController(WaveBukkit plugin) {
        super(plugin);
    }

    public void save(final WavePlayer wavePlayer) {
        try {
            Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)WaveBukkit.getInstance(), new Runnable() {
                public void run() {
                    try (StorageConnection storageConnection = WaveBukkit.getStorage().newConnection()) {
                        ResultSet resultSet = storageConnection.query("select * from wave_pvp where ID = '" + wavePlayer.getUuid() + "'");
                        String update = "update wave_pvp set `kills` = '" + String.valueOf(wavePlayer.getPvp().getKills()) + "', `passouchallenge` = '" + String.valueOf(wavePlayer.getPvp().getPassouchallenge()) + "', `deaths` = '" + String.valueOf(wavePlayer.getPvp().getDeaths()) + "', `killstreak` = '" + String.valueOf(wavePlayer.getPvp().getKillstreak()) + "' , `killsfps` = '" + String.valueOf(wavePlayer.getPvp().getKillsfps()) + "', `deathsfps` = '" + String.valueOf(wavePlayer.getPvp().getDeathsfps()) + "', `name` = '" + wavePlayer.getName() + "', `winssumo` = '" + String.valueOf(wavePlayer.getPvp().getWinssumo()) + "', `losessumo` = '" + String.valueOf(wavePlayer.getPvp().getDeathssumo()) + "', `kssumo` = '" + String.valueOf(wavePlayer.getPvp().getWinstreaksumo()) + "', `wins1v1` = '" + String.valueOf(wavePlayer.getPvp().getWinstreakx1()) + "', `deaths1v1` = '" + String.valueOf(wavePlayer.getPvp().getDeathsx1()) + "', `ks1v1` = '" + String.valueOf(wavePlayer.getPvp().getWinsx1()) + "', `coins` = '" + String.valueOf(wavePlayer.getPvp().getCoins()) + "', `xp` = '" + String.valueOf(wavePlayer.getPvp().getXp()) + "' where `ID` = '" + String.valueOf(wavePlayer.getUuid() + "'");
                        String insert = "insert into wave_pvp (name, kills, passouchallenge, deaths, killstreak, killsfps , deathsfps , winssumo , losessumo , kssumo , wins1v1 , deaths1v1 , ks1v1, coins, xp, ID) values ('" + wavePlayer.getName() + "', '" + wavePlayer.getPvp().getKills() + "', '" + wavePlayer.getPvp().getDeaths() + "', '" + wavePlayer.getPvp().getPassouchallenge() + "', '" + wavePlayer.getPvp().getKillstreak() + "', '" + wavePlayer.getPvp().getKillsfps() + "', '" + wavePlayer.getPvp().getDeathsfps() + "', '" + wavePlayer.getPvp().getWinssumo() + "', '" + wavePlayer.getPvp().getDeathssumo() + "', '" + wavePlayer.getPvp().getWinstreaksumo() + "', '" + wavePlayer.getPvp().getWinsx1() + "', '" + wavePlayer.getPvp().getDeathsx1() + "', '" + wavePlayer.getPvp().getWinstreakx1() + "', '" + wavePlayer.getPvp().getCoins() + "', '" + wavePlayer.getPvp().getXp() + "', '" + wavePlayer.getUuid() + "')";
                        if (resultSet.next()) {
                            storageConnection.execute(update);
                            Bukkit.getServer().getConsoleSender().sendMessage("INSERT STATEMENT: " + update);
                        } else {
                            storageConnection.execute(insert);
                            Bukkit.getServer().getConsoleSender().sendMessage("INSERT STATEMENT: " + insert);
                        }
                        resultSet.close();
                        if (!WaveBukkit.getInstance().getConfig().getBoolean("mysql.enable"))
                            storageConnection.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            },  40L);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public WavePlayer load(final WavePlayer wavePlayer, StorageConnection storageConnection) {
        try {
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)WaveBukkit.getInstance(), new Runnable() {
                public void run() {
                    try {
                        try (StorageConnection storageConnection = WaveBukkit.getStorage().newConnection()) {
                            ResultSet resultSet = storageConnection.query("select * from wave_pvp where ID = '" + wavePlayer.getUuid() + "'");
                            if (resultSet.next()) {
                                wavePlayer.getPvp().setKills(resultSet.getInt("kills"));
                                wavePlayer.getPvp().setXp(resultSet.getInt("xp"));
                                wavePlayer.getPvp().setKillsfps(resultSet.getInt("killsfps"));
                                wavePlayer.getPvp().setWinssumo(resultSet.getInt("winssumo"));
                                wavePlayer.getPvp().setWinstreaksumo(resultSet.getInt("kssumo"));
                                wavePlayer.getPvp().setDeathssumo(resultSet.getInt("losessumo"));
                                wavePlayer.getPvp().setDeaths(resultSet.getInt("deaths"));
                                wavePlayer.getPvp().setWinsx1(resultSet.getInt("wins1v1"));
                                wavePlayer.getPvp().setDeathsx1(resultSet.getInt("deaths1v1"));
                                wavePlayer.getPvp().setPassouchallenge(resultSet.getInt("passouchallenge"));
                                wavePlayer.getPvp().setWinstreakx1(resultSet.getInt("ks1v1"));
                                wavePlayer.getPvp().setDeathsfps(resultSet.getInt("deathsfps"));
                                wavePlayer.getPvp().setKillstreak(resultSet.getInt("killstreak"));
                                wavePlayer.getPvp().setCoins(resultSet.getInt("coins"));
                                wavePlayer.getPvp().setUuid(resultSet.getString("ID"));
                            }
                            resultSet.close();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return wavePlayer;
    }

    public List<WavePlayer> load() {
        List<WavePlayer> players = new ArrayList<>();
        WaveBukkit.getExecutorService().execute(() -> {
            try {
                getPlugin();
                try (StorageConnection storageConnection = WaveBukkit.getStorage().newConnection()) {
                    if (storageConnection.query("select * from wave_pvp") == null) {
                        return;
                    }
                    ResultSet resultSet = storageConnection.query("select * from wave_pvp");
                    while (resultSet != null && resultSet.next()) {
                        WavePlayer helixPlayer = new WavePlayer(resultSet.getString("name"), UUID.fromString(resultSet.getString("ID")), true, new PlayerPvP(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        players.add(load(helixPlayer, storageConnection));
                    }
                    ((ResultSet)Objects.<ResultSet>requireNonNull(resultSet)).close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        return players;
    }
}
