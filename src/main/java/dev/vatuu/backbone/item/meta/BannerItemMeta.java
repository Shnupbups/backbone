package dev.vatuu.backbone.item.meta;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.util.DyeColor;

import java.util.List;

public interface BannerItemMeta extends ItemMeta<BannerItemMeta> {

    List<Pair<BannerPattern, DyeColor>> getPatterns();

    BannerItemMeta setPattern(List<Pair<BannerPattern, DyeColor>> patterns);

    BannerItemMeta addPattern(BannerPattern pattern, DyeColor color);

    BannerItemMeta insertPattern(int index, BannerPattern pattern, DyeColor color);

    BannerItemMeta removePattern(int index);

    static BannerPattern patternById(String id) {
        for(BannerPattern b : BannerPattern.values())
            if(b.getId().equals(id))
                return b;

        return null;
    }
}


