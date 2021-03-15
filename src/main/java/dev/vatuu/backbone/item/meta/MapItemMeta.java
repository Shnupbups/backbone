package dev.vatuu.backbone.item.meta;

import dev.vatuu.backbone.maps.MapView;

public interface MapItemMeta extends ItemMeta<MapItemMeta> {

    MapView getMapView();
    MapItemMeta setMapView(MapView view);
    MapItemMeta setMapView(int id);
    MapItemMeta createMapView();
}
