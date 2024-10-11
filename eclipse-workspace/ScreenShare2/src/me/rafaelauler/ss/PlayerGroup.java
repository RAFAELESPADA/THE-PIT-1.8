package me.rafaelauler.ss;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum PlayerGroup {

    OWNER("Dono", 0, "Dono", "tag.dono", ChatColor.DARK_RED, 1),
    CHEFIA("Diretor", 1, "Diretor", "tag.diretor", ChatColor.RED, 2),
    ADMIN("Admin", 2, "Admin", "tag.admin", ChatColor.AQUA, 3),
    GERENTE("Gerente", 3, "Gerente", "tag.gerente", ChatColor.DARK_AQUA, 4),

    SENIORMOD("SeniorMod", 4, "SeniorMod", "tag.seniormod", ChatColor.DARK_BLUE, 5),
    MOD("Mod", 5, "Mod", "tag.mod", ChatColor.DARK_GREEN, 6),
    AJUDANTE("Ajudante", 6, "Ajudante", "tag.ajudante", ChatColor.YELLOW, 7),
    BUILDER("Construtor", 7, "Construtor", "tag.construtor", ChatColor.BLUE, 8),
    CREATOR("Creator", 8, "Creator", "tag.creator", ChatColor.RED, 9),
    INVEST("Invest", 9, "Invest", "tag.invest", ChatColor.GREEN , 10),
    VIPPLUS("Vip+", 10, "Vip+", "tag.vip+", ChatColor.AQUA , 11),
    VIP("Vip", 11, "Vip", "tag.vip", ChatColor.GREEN , 12),
    BUGHUNTER("BugHunter", 12, "BugHunter", "tag.bughunter", ChatColor.DARK_GRAY , 13),
    APOIADOR("Apoiador", 13, "Apoiador", "tag.apoiador", ChatColor.DARK_PURPLE , 14),
    NITRO("Nitro", 14, "Nitro", "tag.nitro", ChatColor.LIGHT_PURPLE , 15),
    MEMBRO("Membro", 23, "Membro", "tag.membro", ChatColor.GRAY, 16);

    private final String name;
    private final String permission;
    private final ChatColor color;
    private final int priority;

    PlayerGroup(final String s, final int n, final String name, final String permission, final ChatColor color, final int priority) {
        this.name = name;
        this.permission = permission;
        this.color = color;
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public String getColoredName() {
        return this.getColor() + this.getName();
    }

    public int getPriority() {
        return this.priority;
    }

    public String getBoldColoredName() {
        return this.getColor() + "§l" + this.getName();
    }

    public static PlayerGroup getByName(final String name) {
        for (PlayerGroup group : PlayerGroup.values()) {
            if (group.name().equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
    }

    public static PlayerGroup getGroup(final Player player) {
        for (PlayerGroup group : PlayerGroup.values()) {
            if (player.hasPermission(group.getPermission())) {
                return group;
            }
        }
        return PlayerGroup.MEMBRO;
    }

    public static String getPlayerNameWithGroup(Player player) {
        PlayerGroup group = getGroup(player);
        String prefix = group.getBoldColoredName().toUpperCase();
        return prefix + group.getColor() + " " + player.getName();
    }

}
