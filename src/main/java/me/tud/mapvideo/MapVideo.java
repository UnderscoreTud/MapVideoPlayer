package me.tud.mapvideo;

import me.tud.mapvideo.commands.VideoCommand;
import me.tud.mapvideo.util.Commands;
import me.tud.mapvideo.util.Video;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapVideo extends JavaPlugin {

    private static MapVideo instance;

    @Override
    public void onEnable() {
        instance = this;
        Commands.registerCommands(new VideoCommand("video"));
        Video.initFolder();
    }

    public static MapVideo getInstance() {
        return instance;
    }
}
