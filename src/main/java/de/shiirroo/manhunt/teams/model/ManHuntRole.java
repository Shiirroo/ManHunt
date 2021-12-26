package de.shiirroo.manhunt.teams.model;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;

public enum ManHuntRole {
    Assassin(NamedTextColor.BLUE, ChatColor.BLUE),
    Speedrunner(NamedTextColor.DARK_PURPLE, ChatColor.DARK_PURPLE),
    Hunter(NamedTextColor.RED, ChatColor.RED),
    Unassigned(NamedTextColor.YELLOW, ChatColor.YELLOW);

    private final NamedTextColor textColor;
    private final ChatColor chatColor;

    ManHuntRole(NamedTextColor textColor, ChatColor chatColor) {
        this.textColor = textColor;
        this.chatColor = chatColor;
    }

    public NamedTextColor getTextColor() {
        return textColor;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
}
