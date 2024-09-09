package com.berttowne.candortrial;

import com.berttowne.candortrial.command.TeleportCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CandorTrial extends JavaPlugin {

    private TeleportCommand teleportCommand;

    @Override
    public void onEnable() {
        this.teleportCommand = new TeleportCommand(this);
    }

    public TeleportCommand getTeleportCommand() {
        return teleportCommand;
    }

}