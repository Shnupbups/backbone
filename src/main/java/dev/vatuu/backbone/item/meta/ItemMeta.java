package dev.vatuu.backbone.item.meta;

import dev.vatuu.backbone.item.meta.extensions.ItemExt;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemMeta<S extends ItemMeta<S>> {

    //TODO: HideFlags, Attributes

    int getCount();
    S setCount(int count);

    int getDamage();
    S setDamage(int damage);

    Optional<Integer> getCustomModelValue();
    S setCustomModelValue(int value);

    boolean isUnbreakable();
    S setUnbreakable(boolean b);

    Map<Enchantment, Integer> getEnchantments();
    S addEnchantment(Enchantment e, int level);
    S removeEnchantment(Enchantment e);

    int getRepairCost();
    S setRepairCost(int cost);

    int getCooldown();
    S setCooldown(int cooldown);

    Text getName();
    S setName(Text t);

    List<Text> getLore();
    S setLore(List<Text> lore);
    S addLoreLine(Text... text);
    S insertLoreLine(int index, Text... text);
    S removeLoreLine(int index);

    ItemStack apply();

    static <S extends ItemMeta<S>> S of(ItemStack stack) {
        return ((ItemExt)stack.getItem()).getMeta(stack);
    }

    static <S extends ItemMeta<S>> S of(Item i) {
        return of(new ItemStack(i));
    }
}
