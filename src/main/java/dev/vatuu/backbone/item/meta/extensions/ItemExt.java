package dev.vatuu.backbone.item.meta.extensions;

import dev.vatuu.backbone.item.meta.ItemMeta;
import net.minecraft.item.ItemStack;

public interface ItemExt {
    <S extends ItemMeta<S>> S getMeta(ItemStack stack);
}
