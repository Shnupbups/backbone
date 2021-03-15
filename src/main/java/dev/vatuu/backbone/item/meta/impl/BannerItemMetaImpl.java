package dev.vatuu.backbone.item.meta.impl;

import com.mojang.datafixers.util.Pair;
import dev.vatuu.backbone.item.meta.BannerItemMeta;
import dev.vatuu.backbone.utils.Codecs;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BannerItemMetaImpl extends ItemMetaImpl<BannerItemMeta> implements BannerItemMeta {

    public BannerItemMetaImpl(ItemStack s) { super(s); }

    @Override
    public List<Pair<BannerPattern, DyeColor>> getPatterns() {
        if(stack.getSubTag("BlockEntityTag") == null || !stack.getSubTag("BlockEntityTag").contains("Patterns"))
            return new ArrayList<>();
        Optional<Pair<List<Pair<BannerPattern, DyeColor>>, Tag>> result = NbtOps.INSTANCE.withDecoder(Codecs.BANNER_PATTERN.listOf()).apply(stack.getSubTag("BlockEntityTag").get("Patterns")).result();
        return result.map(pair -> new ArrayList<>(pair.getFirst())).orElseGet(ArrayList::new);
    }

    @Override
    public BannerItemMeta setPattern(List<Pair<BannerPattern, DyeColor>> patterns) {
        if(patterns == null || patterns.isEmpty())
            stack.getOrCreateSubTag("BlockEntityTag").remove("Patterns");
        else {
            Tag t = NbtOps.INSTANCE.withEncoder(Codecs.BANNER_PATTERN.listOf()).apply(patterns).result().orElse(new CompoundTag());
            stack.getOrCreateSubTag("BlockEntityTag").put("Patterns", t);
        }
        return this;
    }

    @Override
    public BannerItemMeta addPattern(BannerPattern pattern, DyeColor color) {
        List<Pair<BannerPattern, DyeColor>> list = getPatterns();
        list.add(new Pair<>(pattern, color));
        setPattern(list);
        return this;
    }

    @Override
    public BannerItemMeta insertPattern(int index, BannerPattern pattern, DyeColor color) {
        List<Pair<BannerPattern, DyeColor>> list = getPatterns();
        if(index >= list.size())
            list.add(new Pair<>(pattern, color));
        else
            list.add(Math.max(0, index), new Pair<>(pattern, color));
        setPattern(list);
        return this;
    }

    @Override
    public BannerItemMeta removePattern(int index) {
        List<Pair<BannerPattern, DyeColor>> list = getPatterns();
        if(index >= list.size())
            return this;
        list.remove(Math.max(0, index));
        setPattern(list);
        return this;
    }
}
