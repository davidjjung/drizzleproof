package com.davigj.drizzleproof.core.mixin;

import com.davigj.drizzleproof.core.DrizzleproofConfig;
import com.davigj.drizzleproof.core.other.DrizzleproofBlockTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(Block.class)
public class BlockMixin {
    @ModifyArg(method = "popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V"), index = 1)
    private static Supplier<ItemEntity> mollify(Supplier<ItemEntity> supplier, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos,
                                                @Local(ordinal = 0) double d0, @Local(ordinal = 1) double d1, @Local(ordinal = 2) double d2, @Local(ordinal = 3) double d3) {
        if (level.getBlockState(pos).is(DrizzleproofBlockTags.STATIC_BLOCKS) || DrizzleproofConfig.COMMON.allBlocksStatic.get()) {
            return () -> new ItemEntity(level, d1, d2, d3, stack, 0, DrizzleproofConfig.COMMON.pepStep.get(), 0);
        } else {
            return supplier;
        }
    }

    @WrapOperation(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Z)V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"), remap = false)
    private static void onPlop(List<ItemStack> instance, Consumer<ItemStack> consumer, Operation<Void> original,
                               @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos,
                               @Local(argsOnly = true) Entity entity, @Local(argsOnly = true) ItemStack stack) {
        boolean sick = entity instanceof LivingEntity living && living.hasEffect(MobEffects.CONFUSION);
        if (DrizzleproofConfig.COMMON.allBlocksStatic.get() || (!(sick && DrizzleproofConfig.COMMON.nauseousDisarray.get()) &&
                (state.is(DrizzleproofBlockTags.STATIC_BLOCKS) ||
                        (DrizzleproofConfig.COMMON.silkBlocksStatic.get() && stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0)))) {
            instance.forEach(((ItemStack itemStack) -> {
                float f = EntityType.ITEM.getHeight() / 2.0F;
                double d1 = (double) ((float) pos.getY() + 0.5F) - (double) f;
                ItemEntity itemEntity = new ItemEntity(level, (float) pos.getX() + 0.5F, d1, (float) pos.getZ() + 0.5F,
                        itemStack, 0, DrizzleproofConfig.COMMON.pepStep.get(), 0);
                if (!level.isClientSide && !itemStack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !level.restoringBlockSnapshots) {
                    level.addFreshEntity(itemEntity);
                }
            }));
            return;
        }
        original.call(instance, consumer);
    }
}
