package me.tud.mapvideo.util;

import me.tud.mapvideo.MapVideo;

public class Util {

    public static void log(String message) {
        MapVideo.getInstance().getLogger().info(message);
    }

}
