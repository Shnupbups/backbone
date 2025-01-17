package dev.vatuu.backbone.events;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import dev.vatuu.backbone.events.base.Cancellable;
import dev.vatuu.backbone.events.base.Event;
import dev.vatuu.backbone.events.base.EventFactory;

import java.util.List;

public class EnchantmentEvents {
	public static class EnchantItemInfo implements Cancellable {
		protected World world;
		protected BlockPos pos;
		protected BlockState enchantmentTableState;
		protected PlayerEntity enchanter;
		protected ItemStack input,lapis,output;
		protected List<EnchantmentLevelEntry> enchantmentsToAdd;
		protected int experienceLevelRequired,cost,buttonId,seed;
		protected boolean cancelled = false;

		public EnchantItemInfo(World world, BlockPos pos, BlockState enchantmentTableState, PlayerEntity enchanter, ItemStack input, ItemStack lapis, ItemStack output, List<EnchantmentLevelEntry> enchantmentsToAdd, int experienceLevelRequired, int cost, int buttonId, int seed) {
			this.world = world;
			this.pos = pos;
			this.enchantmentTableState = enchantmentTableState;
			this.enchanter = enchanter;
			this.input = input;
			this.lapis = lapis;
			this.output = output;
			this.enchantmentsToAdd = enchantmentsToAdd;
			this.experienceLevelRequired = experienceLevelRequired;
			this.cost = cost;
			this.buttonId = buttonId;
			this.seed = seed;
		}

		public World getWorld() {
			return world;
		}

		/**
		 * Corresponds to the position of the Enchantment Table
		 * @return position of the enchantment table
		 */
		public BlockPos getPos() {
			return pos;
		}

		public BlockState getEnchantmentTableState() {
			return enchantmentTableState;
		}

		public PlayerEntity getEnchanter() {
			return enchanter;
		}

		public ItemStack getInput() {
			return input;
		}

		public ItemStack getLapis() {
			return lapis;
		}

		public ItemStack getOutput() {
			return output;
		}

		public List<EnchantmentLevelEntry> getEnchantmentsToAdd() {
			return enchantmentsToAdd;
		}

		public void setEnchantmentsToAdd(List<EnchantmentLevelEntry> enchantmentsToAdd) {
			this.enchantmentsToAdd = enchantmentsToAdd;
		}

		public void addEnchantment(EnchantmentLevelEntry enchantment) {
			this.enchantmentsToAdd.add(enchantment);
		}

		public void addEnchantment(Enchantment enchantment, int level) {
			this.enchantmentsToAdd.add(new EnchantmentLevelEntry(enchantment, level));
		}

		public void addEnchantment(Enchantment enchantment) {
			this.enchantmentsToAdd.add(new EnchantmentLevelEntry(enchantment, 1));
		}

		public void removeEnchantment(EnchantmentLevelEntry enchantment) {
			this.enchantmentsToAdd.remove(enchantment);
		}

		public void removeEnchantment(Enchantment enchantment) {
			this.enchantmentsToAdd.removeIf((enchantmentLevelEntry) -> enchantmentLevelEntry.enchantment.equals(enchantment));
		}

		public void removeEnchantment(Enchantment enchantment, int level) {
			this.enchantmentsToAdd.removeIf((enchantmentLevelEntry) -> enchantmentLevelEntry.enchantment.equals(enchantment) && enchantmentLevelEntry.level == level);
		}

		public boolean isEnchantmentBeingAdded(EnchantmentLevelEntry enchantment) {
			return this.enchantmentsToAdd.contains(enchantment);
		}

		public boolean isEnchantmentBeingAdded(Enchantment enchantment) {
			return this.enchantmentsToAdd.stream().anyMatch((enchantmentLevelEntry) -> enchantmentLevelEntry.enchantment.equals(enchantment));
		}

		public boolean isEnchantmentBeingAdded(Enchantment enchantment, int level) {
			return this.enchantmentsToAdd.stream().anyMatch((enchantmentLevelEntry) -> enchantmentLevelEntry.enchantment.equals(enchantment) && enchantmentLevelEntry.level == level);
		}

		/**
		 * The amount of levels required to perform the enchantment.
		 * Note that this is *not* the amount of levels *used* to perform the enchantment.
		 * @return experience levels required
		 */
		public int getExperienceLevelRequired() {
			return experienceLevelRequired;
		}

		/**
		 * The amount of levels required to perform the enchantment.
		 * Note that this is *not* the amount of levels *used* to perform the enchantment.
		 */
		public void setExperienceLevelRequired(int experienceLevelRequired) {
			this.experienceLevelRequired = experienceLevelRequired;
		}

		/**
		 * The amount of levels and lapis used to perform the enchantment.
		 * Note that this is *not* the amount of levels *required* to perform the enchantment.
		 * @return cost
		 */
		public int getCost() {
			return this.cost;
		}

		/**
		 * The amount of levels and lapis used to perform the enchantment.
		 * Note that this is *not* the amount of levels *required* to perform the enchantment.
		 */
		public void setCost(int cost) {
			this.cost = cost;
		}

		/**
		 * Corresponds to which choice of the three buttons was chosen, zero-indexed.
		 * @return the ID of the button clicked
		 */
		public int getButtonId() {
			return this.buttonId;
		}

		/**
		 * This refers to the seed with which enchantments are chosen, not the world seed.
		 * @return
		 */
		public int getSeed() {
			return this.seed;
		}

		@Override
		public boolean isCancelled() {
			return cancelled;
		}

		@Override
		public void setCancelled(boolean cancelled) {
			this.cancelled = cancelled;
		}
	}

	public static final Event<EnchantItem> ENCHANT_ITEM = EventFactory.createArrayBacked(EnchantItem.class, callbacks -> (info) -> {
		for (EnchantItem callback : callbacks)
			callback.enchantItem(info);
	});

	@FunctionalInterface
	public interface EnchantItem {
		void enchantItem(EnchantItemInfo info);
	}
}
