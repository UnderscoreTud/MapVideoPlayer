package me.tud.mapvideo.util;

import me.tud.mapvideo.MapVideo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.io.File;

public class Video {

    private final static File videoFolder = new File(MapVideo.getInstance().getDataFolder(), "Videos");
    private final ItemStack itemStack;
    private final File videoFile;

    public Video(String fileName) {
        videoFile = new File(getFolder(), fileName);
        itemStack = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
        MapView mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));
        mapMeta.setMapView(mapView);
        itemStack.setItemMeta(mapMeta);
    }

    public void play(boolean loop) {
        MapView mapView = ((MapMeta) itemStack.getItemMeta()).getMapView();
        if (mapView == null) return;
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.addRenderer(new VideoMapRenderer(videoFile, loop));
    }

    public ItemStack getMap() {
        return itemStack;
    }

    public static void initFolder() {
        if (videoFolder.exists() && videoFolder.isDirectory())
            videoFolder.delete();
        videoFolder.mkdirs();
    }

    public static File getFolder() {
        if (!videoFolder.exists())
            initFolder();
        return videoFolder;
    }
}
