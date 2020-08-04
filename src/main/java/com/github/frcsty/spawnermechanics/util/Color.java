package com.github.frcsty.spawnermechanics.util;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public final class Color {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorize(final List<String> message) {
        return message.stream().map(Color::colorize).collect(Collectors.toList());
    }

}
