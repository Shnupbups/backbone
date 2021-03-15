package dev.vatuu.backbone.utils;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vatuu.backbone.item.meta.BannerItemMeta;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public final class Codecs {

    public static final Codec<Identifier> IDENTIFIER = Codec.STRING.xmap(Identifier::new, Identifier::toString);

    public static final Codec<DyeColor> COLOR = Codec.INT.xmap(DyeColor::byId, DyeColor::getId);

    public static final Codec<Pair<BannerPattern, DyeColor>> BANNER_PATTERN = RecordCodecBuilder.create(i -> i.group(
       Codec.STRING.xmap(BannerItemMeta::patternById, BannerPattern::getId).fieldOf("Pattern").forGetter(Pair::getFirst),
       Codecs.COLOR.fieldOf("Color").forGetter(Pair::getSecond)
    ).apply(i, Pair::new));
}
