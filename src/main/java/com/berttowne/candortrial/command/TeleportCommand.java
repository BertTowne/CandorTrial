package com.berttowne.candortrial.command;

import com.berttowne.candortrial.CandorTrial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TeleportCommand implements CommandExecutor {

    private final CandorTrial plugin;

    public TeleportCommand(@NotNull CandorTrial plugin) {
        this.plugin = plugin;

        plugin.getCommand("teleport").setExecutor(this);
        plugin.getCommand("tp").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String name, @NotNull String[] args) {
        // check if sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You must be a player to use this command!").color(NamedTextColor.RED));
            return true;
        }

        // check if player has permission
        if (!sender.hasPermission("candortrial.teleport")) {
            sender.sendMessage(Component.text("You do not have permission to use this command!").color(NamedTextColor.RED));
            return true;
        }

        // check if player has teleported within the last 5 seconds
        if (player.hasMetadata("lastTeleport")) {
            long lastTeleport = player.getMetadata("lastTeleport").get(0).asLong();

            if (System.currentTimeMillis() - lastTeleport < TimeUnit.SECONDS.toMillis(5)) {
                sender.sendMessage(Component.text("Please wait a moment before teleporting again!").color(NamedTextColor.RED));
                return true;
            }
        }

        // check if player provided coordinates
        if (args.length < 3) {
            sender.sendMessage(Component.text("Usage: /teleport <x> <y> <z>").color(NamedTextColor.RED));
            return true;
        }

        // parse coordinates
        double x, y, z;
        try {
            x = Double.parseDouble(args[0]);
            y = Double.parseDouble(args[1]);
            z = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid coordinates! Please provide a valid number!").color(NamedTextColor.RED));
            return true;
        }

        // teleport player and set last teleport time for cooldown
        player.teleport(new Location(player.getWorld(), x, y, z).add(0.5, 1, 0.5));
        player.setMetadata("lastTeleport", new FixedMetadataValue(plugin, System.currentTimeMillis()));
        player.sendMessage(Component.text("Teleported to " + x + ", " + y + ", " + z).color(NamedTextColor.GREEN));

        return true;
    }

}