package dev.vatuu.backbone.entity.meta.mixin;

import dev.vatuu.backbone.entity.meta.base.DecorationEntityMeta;
import dev.vatuu.backbone.entity.meta.base.EntityMeta;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractDecorationEntity.class)
public interface DecorationEntityMixin extends EntityMeta<DecorationEntityMixin>, DecorationEntityMeta<DecorationEntityMixin> {
    @Accessor("facing") Direction getFacing();
    @Invoker("setFacing") void setDir(Direction dir);
}
