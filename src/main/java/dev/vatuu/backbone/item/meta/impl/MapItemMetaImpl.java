package dev.vatuu.backbone.item.meta.impl;

import dev.vatuu.backbone.item.meta.MapItemMeta;
import dev.vatuu.backbone.maps.MapView;
import net.minecraft.item.ItemStack;

public class MapItemMetaImpl extends ItemMetaImpl<MapItemMeta> implements MapItemMeta {

    public MapItemMetaImpl(ItemStack stack) { super(stack); }

    @Override
    public MapView getMapView() {
        //TODO THIS
        return null;
    }

    @Override
    public MapItemMeta setMapView(MapView view) {
        return setMapView(view.getId());
    }

    @Override
    public MapItemMeta setMapView(int id) {
        stack.getOrCreateTag().putInt("map", id);
        return this;
    }

    @Override
    public MapItemMeta createMapView() {
        MapView view = new MapView();
        setMapView(view.getId());
        return this;
    }
}
