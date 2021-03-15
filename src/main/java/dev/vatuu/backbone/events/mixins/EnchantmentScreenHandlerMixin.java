package dev.vatuu.backbone.events.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import dev.vatuu.backbone.events.EnchantmentEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {
	@Shadow @Final public int[] enchantmentPower;

	@Shadow @Final private Property seed;

	protected EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}

	@Inject(method = "Lnet/minecraft/screen/EnchantmentScreenHandler;method_17410(Lnet/minecraft/item/ItemStack;ILnet/minecraft/entity/player/PlayerEntity;ILnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;generateEnchantments(Lnet/minecraft/item/ItemStack;II)Ljava/util/List;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void enchantItem(ItemStack item, int id, PlayerEntity enchanter, int lapisCost, ItemStack lapis, World world, BlockPos pos, CallbackInfo ci, ItemStack output, List<EnchantmentLevelEntry> enchantmentsToAdd) {
		EnchantmentEvents.EnchantItemInfo info = new EnchantmentEvents.EnchantItemInfo(world, pos, world.getBlockState(pos), enchanter, item, lapis, output, enchantmentsToAdd, this.enchantmentPower[id], lapisCost, id, this.seed.get());
		EnchantmentEvents.ENCHANT_ITEM.invoker().enchantItem(info);
		if(info.isCancelled()) ci.cancel();
	}
}
