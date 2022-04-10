package de.shiirroo.manhunt.teams.model;

import org.bukkit.ChatColor;

public enum ManHuntRole {
    Assassin(ChatColor.BLUE),
    Speedrunner(ChatColor.DARK_PURPLE),
    Hunter(ChatColor.RED),
    Unassigned(ChatColor.YELLOW);

    private final ChatColor chatColor;

    ManHuntRole(ChatColor chatColor) {
        this.chatColor = chatColor;
    }


    public ChatColor getChatColor() {
        return chatColor;
    }
}
