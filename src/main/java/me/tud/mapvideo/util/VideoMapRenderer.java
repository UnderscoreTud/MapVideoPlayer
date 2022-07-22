package me.tud.mapvideo.util;

import me.tud.mapvideo.MapVideo;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VideoMapRenderer extends MapRenderer {

    private final File videoFile;
    private final boolean loop;

    protected VideoMapRenderer(File videoFile) {
        this(videoFile, false);
    }

    protected VideoMapRenderer(File videoFile, boolean loop) {
        this.videoFile = videoFile;
        this.loop = loop;
    }

    private boolean started = false;

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        if (!started) {
            started = true;
            try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile)) {
                List<BufferedImage> images = new LinkedList<>();
                grabber.start();
                for (int i = 0; i < grabber.getLengthInFrames(); i++) {
                    Frame frame = grabber.grabImage();
                    try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                        images.add(converter.convert(frame));
                    }
                }
                grabber.stop();
                new BukkitRunnable() {
                    private int i = 0;
                    @Override
                    public void run() {
                        if (i >= images.size() - 1) {
                            if (loop) i = 0;
                            else {
                                cancel();
                                return;
                            }
                        }
                        BufferedImage bufferedImage = images.get(i++);
                        canvas.drawImage(0, 0, MapPalette.resizeImage(bufferedImage));
                    }
                }.runTaskTimerAsynchronously(MapVideo.getInstance(), 0L, 1L);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
