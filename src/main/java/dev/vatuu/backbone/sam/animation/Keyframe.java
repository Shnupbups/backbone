package dev.vatuu.backbone.sam.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vatuu.backbone.sam.model.BoneTransformation;

import java.util.Map;

public final class Keyframe {

    private final Map<String, BoneTransformation> bones;

    public Keyframe(Map<String, BoneTransformation> bones) {
        this.bones = bones;
    }

    public BoneTransformation getBone(String id) {
        return bones.get(id);
    }

    public boolean hasBoneData(String id) {
        return bones.containsKey(id);
    }

    public final static Codec<Keyframe> CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.unboundedMap(Codec.STRING, BoneTransformation.CODEC).fieldOf("data").forGetter((Keyframe o) -> o.bones)
    ).apply(i, Keyframe::new));
}
