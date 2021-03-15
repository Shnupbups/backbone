package dev.vatuu.backbone.maps.mapimage;

import dev.vatuu.backbone.entity.meta.ItemFrameEntityMeta;
import dev.vatuu.backbone.entity.meta.base.EntityMeta;
import dev.vatuu.backbone.item.meta.ItemMeta;
import dev.vatuu.backbone.item.meta.MapItemMeta;
import dev.vatuu.backbone.maps.MapView;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapImage {

    private final Identifier key;

    private BufferedImage imageBase;
    private BufferedImage imageRendered;

    private final int mapX, mapY;
    private final MapView[][] mapViews;

    private final List<MapImageOverlay> overlays = new ArrayList<>();

    public MapImage(String path, Identifier key) {
        try (InputStream s = this.getClass().getClassLoader().getResourceAsStream(path)){
            imageBase = ImageIO.read(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.key = key;
        this.mapX = imageBase.getWidth() / 128;
        this.mapY = imageBase.getHeight() / 128;
        this.mapViews = new MapView[mapX][mapY];
    }

    public void addOverlay(MapImageOverlay entry) {
        this.overlays.add(entry);
    }

    public void create(ServerWorld w) {
        generateMapViews();
    }

    public void destroy() {
        this.imageRendered = null; this.imageBase = null;
    }

    public BufferedImage getImagePart(int x, int y) {
        return imageRendered.getSubimage(x * 128, y * 128, 128, 128);
    }

    public void createDisplay(BlockPos corner, Direction facing, ServerWorld w) {
        for (int i = 0; i < mapY; i++) {
            for (int ii = 0; ii < mapX; ii++) {
                BlockPos loc = corner.mutableCopy();
                loc.add(-ii, -i, 0);
                ItemFrameEntity frame = EntityType.ITEM_FRAME.create(w);
                EntityMeta.<ItemFrameEntityMeta>getMeta(frame)
                        .setItem(getMap(mapViews[ii][i]))
                        .fixate(true)
                        .setDirection(Direction.NORTH)
                        .setAttachmentPos(loc);
            }
        }
    }

    private ItemStack getMap(MapView v) {
        return ItemMeta.<MapItemMeta>of(Items.FILLED_MAP).apply();
    }

    private void renderImage() {
        imageRendered = new BufferedImage(imageBase.getWidth(), imageBase.getHeight(), imageBase.getType());
        Graphics2D data = imageRendered.createGraphics();
        data.drawImage(imageBase, 0, 0, imageBase.getWidth(), imageBase.getHeight(), null);
        data.dispose();
        overlays.forEach(t -> t.render(imageRendered));
    }

    private void generateMapViews() {
        renderImage();
        for (int i = 0; i < mapX; i++) {
            for (int j = 0; j < mapY; j++) {
                MapView view = new MapView();
                view.getRenderers().clear();
                view.getRenderers().add(new MapImageRenderer(i, j, this));
                mapViews[i][j] = view;
            }
        }
    }
}
