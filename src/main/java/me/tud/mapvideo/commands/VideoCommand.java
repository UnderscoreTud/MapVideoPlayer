package me.tud.mapvideo.commands;

import me.tud.mapvideo.util.Video;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoCommand extends BukkitCommand {

    public VideoCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 1) return true;
        if (sender instanceof Player player) {
            Video video = new Video(args[0]);
            player.getInventory().setItemInMainHand(video.getMap());
            boolean loop = args.length > 1 && args[1].equalsIgnoreCase("loop");
            video.play(loop);
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1 -> {
                return sortCompletions(List.of(Video.getFolder().list()), args[0]);
            }
            case 2 -> {
                return sortCompletions(Collections.singletonList("loop"), args[1]);
            }
        }
        return Collections.emptyList();
    }

    private List<String> sortCompletions(List<String> completions, String input) {
        List<String> newCompletions = new ArrayList<>();
        StringUtil.copyPartialMatches(input, completions, newCompletions);
        Collections.sort(newCompletions);
        return newCompletions;
    }
}
