package dev.vatuu.backbone.item.meta.extensions.mixin;

import dev.vatuu.backbone.item.meta.ItemMeta;
import dev.vatuu.backbone.item.meta.extensions.ItemExt;
import dev.vatuu.backbone.item.meta.impl.MapItemMetaImpl;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NetworkSyncedItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FilledMapItem.class)
public abstract class MapItemMixin extends NetworkSyncedItem implements ItemExt {

    public MapItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public <S extends ItemMeta<S>> S getMeta(ItemStack stack) {
        return (S)new MapItemMetaImpl(stack);
    }
}
