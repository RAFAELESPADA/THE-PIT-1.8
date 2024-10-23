package net.wavemc.core.bukkit.account.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor @Data
public class PlayerPvP {

    private int kills, deaths, killstreak, coins , killsfps , deathsfps , winssumo , deathssumo , winstreaksumo , winsx1, deathsx1 , winstreakx1 , xp, passouchallenge, thepitkills, thepitdeaths, thepitstreak, gold, thepitxp;
private String uuid;

    public PlayerPvP(int kills, int deaths, int killstreak, int coins, int killsfps, int deathsfps, int winssumo, int deathssumo, int winstreaksumo, int winsx1, int deathsx1, int winstreakx1, int xp, int passouchallenge, int thepitkills, int thepitdeaths, int thepitstreak, int gold, int thepitxp) {
    }

    public void addKills(int kills) {
        setKills(getKills() + kills);
    }

    public void addKillsFPS(int killsfps) {
        setKillsfps(getKillsfps() + killsfps);
    }

    public void addDeaths(int deaths)  {
        setDeaths(getDeaths() + deaths);
    }
    
    public void addDeathsFPS(int deaths)  {
        setDeathsfps(getDeathsfps() + deaths);
    }
    
    public void addWinsSumo(int deaths)  {
        setWinssumo(getWinssumo() + deaths);
    }
    
    public void addXP(int xp)  {
        setXp(getXp() + xp);
    }
    public void addChallege(int xp) {
        setPassouchallenge(getPassouchallenge() + xp);
    }
    public void addDeathsSumo(int deaths)  {
        setDeathssumo(getDeathssumo() + deaths);
    }
    public void addWinsX1(int deaths)  {
        setWinsx1(getWinsx1() + deaths);
    }
    public void addWinstreakSumo(int deaths)  {
        setWinstreaksumo(getWinstreaksumo() + deaths);
    }
    public void addWinstreakX1(int deaths)  {
        setWinstreakx1(getWinstreakx1() + deaths);
    }
    public void addwinsX1(int deaths)  {
        setWinsx1(getWinsx1() + deaths);
    }
    public void adddeathsX1(int deaths)  {
        setDeathsx1(getDeathsx1() + deaths);
    }
    public void addthepitkills(int kills) {
        setThepitkills(getThepitkills() + kills);
    }
    public void addthepitdeaths(int kills) {
        setThepitdeaths(getThepitdeaths() + kills);
    }
    public void addthepitstreak(int kills) {
        setThepitstreak(getThepitstreak() + kills);
    }
    public void addthepitxp(int kills) {
        setThepitxp(getThepitxp() + kills);
    }
    public void addgold(int kills) {
        setGold(getGold() + kills);
    }
    public void addKillstreak(int killstreak) {
        setKillstreak(getKillstreak() + killstreak);
    }

    public void addCoins(int coins) {
        setCoins(getCoins() + coins);
    }

    public void removeCoins(int coins) {
        setCoins(getCoins() - coins);
    }
}
