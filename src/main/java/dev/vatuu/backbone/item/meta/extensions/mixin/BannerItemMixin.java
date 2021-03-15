package dev.vatuu.backbone.item.meta.extensions.mixin;

import dev.vatuu.backbone.item.meta.ItemMeta;
import dev.vatuu.backbone.item.meta.extensions.ItemExt;
import dev.vatuu.backbone.item.meta.impl.BannerItemMetaImpl;
import net.minecraft.block.Block;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallStandingBlockItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BannerItem.class)
public abstract class BannerItemMixin extends WallStandingBlockItem implements ItemExt {

    public BannerItemMixin(Block standingBlock, Block wallBlock, Settings settings) {
        super(standingBlock, wallBlock, settings);
    }

    @Override
    public <S extends ItemMeta<S>> S getMeta(ItemStack stack) {
        return (S)new BannerItemMetaImpl(stack);
    }
}
