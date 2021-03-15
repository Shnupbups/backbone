package dev.vatuu.backbone.item.meta.impl;

import com.google.common.collect.Lists;
import dev.vatuu.backbone.item.meta.ItemMeta;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ItemMetaImpl<S extends ItemMeta<S>> implements ItemMeta<S> {

    protected final ItemStack stack;

    public ItemMetaImpl(ItemStack s) {
        this.stack = s;
    }

    @Override
    public int getCount() { return stack.getCount(); }
    @Override
    public S setCount(int count) {
        stack.setCount(count);
        return (S)this;
    }

    @Override
    public int getDamage() { return stack.getDamage(); }
    @Override
    public S setDamage(int damage) {
        if(damage > stack.getMaxDamage())
            stack.setDamage(stack.getMaxDamage());
        else stack.setDamage(Math.max(damage, 0));
        return (S)this;
    }

    @Override
    public Optional<Integer> getCustomModelValue() {
        CompoundTag tag = stack.getTag();
        if(tag == null || tag.contains("CustomModelData"))
            return Optional.empty();
        return Optional.of(tag.getInt("CustomModelData"));
    }
    @Override
    public S setCustomModelValue(int value) {
        stack.getOrCreateTag().putInt("CustomModelData", value);
        return (S)this;
    }

    @Override
    public boolean isUnbreakable() { return stack.isDamageable(); }
    @Override
    public S setUnbreakable(boolean b) {
        if(b)
            stack.getOrCreateTag().putBoolean("Unbreakable", true);
        else if(stack.hasTag() && stack.getTag().contains("Unbreakable"))
            stack.getTag().remove("Unbreakable");
        return (S)this;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() { return EnchantmentHelper.get(stack); }
    @Override
    public S addEnchantment(Enchantment e, int level) {
        if(getEnchantments().containsKey(e))
            removeEnchantment(e);
        stack.addEnchantment(e, level);
        return (S)this;
    }
    @Override
    public S removeEnchantment(Enchantment e) {
        Map<Enchantment, Integer> enchs = getEnchantments();
        enchs.remove(e);
        EnchantmentHelper.set(enchs, stack);
        return (S)this;
    }

    @Override
    public int getRepairCost() { return stack.getRepairCost(); }
    @Override
    public S setRepairCost(int cost) {
        stack.setRepairCost(cost);
        return (S)this;
    }

    @Override
    public int getCooldown() { return stack.getCooldown(); }
    @Override
    public S setCooldown(int cooldown) {
        stack.setCooldown(cooldown);
        return (S)this;
    }

    @Override
    public Text getName() { return stack.getName(); }
    @Override
    public S setName(Text t) {
        if(t == null)
            stack.removeCustomName();
        else
            stack.setCustomName(t);
        return (S)this;
    }

    @Override
    public List<Text> getLore() {
        if(stack.getSubTag("display") == null || !stack.getSubTag("display").contains("Lore"))
            return new ArrayList<>();
        else
            return ((ListTag)stack.getSubTag("display").get("Lore")).stream()
                    .map(t -> Text.Serializer.fromJson((t).toString()))
                    .collect(Collectors.toList());
    }
    @Override
    public S setLore(List<Text> lore) {
        ListTag tag = new ListTag();
        lore.forEach(t -> tag.add(StringTag.of(Text.Serializer.toJson(t))));
        stack.getOrCreateSubTag("display").put("Lore", tag);
        return (S)this;
    }
    @Override
    public S addLoreLine(Text... text) {
        List<Text> lore = getLore();
        lore.addAll(Lists.newArrayList(text));
        setLore(lore);
        return (S)this;
    }
    @Override
    public S insertLoreLine(int index, Text... text) {
        List<Text> lore = getLore();
        if(index >= lore.size())
            lore.addAll(Lists.newArrayList(text));
        else
            lore.addAll(index, Lists.newArrayList(text));
        setLore(lore);
        return (S)this;
    }
    @Override
    public S removeLoreLine(int index) {
        List<Text> lore = getLore();
        if(index >= lore.size())
            return (S)this;
        lore.remove(index);
        setLore(lore);
        return (S)this;
    }

    public ItemStack apply() {
        return stack;
    }
}
