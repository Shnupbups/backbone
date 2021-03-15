package dev.vatuu.backbone.item.meta.extensions.mixin;

import dev.vatuu.backbone.item.meta.ItemMeta;
import dev.vatuu.backbone.item.meta.extensions.ItemExt;
import dev.vatuu.backbone.item.meta.impl.ItemMetaImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemConvertible, ItemExt {

    public <S extends ItemMeta<S>> S getMeta(ItemStack stack) {
        return (S)new ItemMetaImpl<S>(stack);
    }
}
