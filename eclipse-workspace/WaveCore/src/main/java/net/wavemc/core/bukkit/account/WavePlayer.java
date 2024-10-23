package net.wavemc.core.bukkit.account;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.wavemc.core.Wave;
import net.wavemc.core.bukkit.account.provider.PlayerPvP;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

@AllArgsConstructor @Data
public class WavePlayer {

    private final String name;
    private final UUID uuid;
    private boolean tell;

    private PlayerPvP pvp;
    @Getter private final static Wave instance = new Wave();
    @Getter
    private final ExecutorService executorService = Executors.newFixedThreadPool(50);
    private final LuckPerms luckPerms = LuckPermsProvider.get();






    public PlayerPvP getPvp() {
        return pvp != null ? pvp : (pvp = new PlayerPvP(0, 0, 0, 0, 0, 0 , 0  ,0 ,0 ,0 ,0 ,0 ,0 ,0, 0 ,0 ,0, 0, 0));
    }













    public static void unregisterCommands(String... commands) {
        try {
            Field firstField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            firstField.setAccessible(true);

            CommandMap commandMap = (CommandMap) firstField.get(Bukkit.getServer());
            Field secondField = commandMap.getClass().getDeclaredField("knownCommands");
            secondField.setAccessible(true);

            HashMap<String, Command> knownCommands = (HashMap<String, Command>) secondField.get(commandMap);

            for (String command : commands) {
                if (knownCommands.containsKey(command)) {
                    knownCommands.remove(command);
                    List<String> aliases = new ArrayList<>();
                    for (String key : knownCommands.keySet()) {
                        if (!key.contains(":"))
                            continue;

                        String substr = key.substring(key.indexOf(":") + 1);
                        if (substr.equalsIgnoreCase(command)) {
                            aliases.add(key);
                        }
                    }
                    for (String alias : aliases) {
                        knownCommands.remove(alias);
                    }
                }
            }
        } catch (Exception e) {}
    }


}