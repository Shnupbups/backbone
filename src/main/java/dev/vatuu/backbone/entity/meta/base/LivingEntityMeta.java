package dev.vatuu.backbone.entity.meta.base;

import dev.vatuu.backbone.entity.meta.mixin.EntityMixin;
import dev.vatuu.backbone.entity.meta.mixin.LivingEntityMixin;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings("unchecked")
public interface LivingEntityMeta<S extends LivingEntityMeta<S>> extends EntityMeta<S> {

    default float getAbsorptionAmount() { return ((LivingEntity) this).getAbsorptionAmount(); }
    default S setAbsorptionAmount(float amount) {
        ((LivingEntity) this).setAbsorptionAmount(amount);
        return (S) this;
    }

    default Collection<StatusEffectInstance> getStatusEffects() { return ((LivingEntity) this).getStatusEffects(); }
    //TODO Add/remove/modify status effects

    default AttributeContainer getAttributes() { return ((LivingEntity) this).getAttributes(); }
    //TODO Add/remove/modify Attributes

    default float getHealth() { return ((LivingEntity) this).getHealth(); }
    default S setHealth(float health) {
        ((LivingEntity) this).setHealth(health);
        return (S) this;
    }

    default ItemStack getEquippedItem(EquipmentSlot slot) { return ((LivingEntity)this).getEquippedStack(slot); }
    default boolean hasSlotEquipped(EquipmentSlot slot) { return ((LivingEntity)this).hasStackEquipped(slot); }
    default S setArmorItems(EquipmentSlot slot, ItemStack stack) {
        ((LivingEntity)this).equipStack(slot, stack);
        return (S)this;
    }
    default S removeEquippedSlot(EquipmentSlot slot) {
        ((LivingEntity)this).equipStack(slot, ItemStack.EMPTY);
        return (S)this;
    }

    default int getHurtTime() { return ((LivingEntity) this).hurtTime; }
    default S setHurtTime(int hurtTime) {
        ((LivingEntity) this).hurtTime = hurtTime;
        return (S) this;
    }

    default int getHurtTimestamp() { return ((LivingEntity) this).getLastAttackedTime(); }
    default S setHurtTimestamp(int timestamp) {
        ((LivingEntityMixin)this).setLastAttackedTime(timestamp);
        return (S)this;
    }

    default int getDeathTime() { return ((LivingEntity)this).deathTime; }
    default S setDeathTime(int deathTime) {
        ((LivingEntity)this).deathTime = deathTime;
        return (S)this;
    }

    default boolean isGliding() { return ((LivingEntity)this).isFallFlying(); }
    default S setGliding(boolean b) {
        ((EntityMixin)(LivingEntity)this).setFlags(7, b);
        return (S)this;
    }

    default Optional<BlockPos> getSleepingPos() { return ((LivingEntity)this).getSleepingPosition(); }
    default boolean isSleeping() { return ((LivingEntity)this).isSleeping(); }
    default S setSleepingPos(BlockPos pos) {
        ((LivingEntity)this).setSleepingPosition(pos);
        return (S)this;
    }
}
