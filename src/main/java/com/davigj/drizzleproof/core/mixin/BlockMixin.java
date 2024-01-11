package com.davigj.drizzleproof.core.mixin;

import com.davigj.drizzleproof.core.DrizzleproofConfig;
import com.davigj.drizzleproof.core.other.DrizzleproofBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.world.level.block.Block.getDrops;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void popMoreLikeLock(Level level, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (level.getBlockState(pos).is(DrizzleproofBlockTags.STATIC_BLOCKS) || DrizzleproofConfig.COMMON.allBlocksStatic.get()) {
            float f = EntityType.ITEM.getHeight() / 2.0F;
            double d0 = (float) pos.getX() + 0.5F;
            double d1 = (double) ((float) pos.getY() + 0.5F) - (double) f;
            double d2 = (float) pos.getZ() + 0.5F;
            ItemEntity itemEntity = new ItemEntity(level, d0, d1, d2, stack);
            if (!level.isClientSide && !stack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !level.restoringBlockSnapshots) {
                itemEntity.setDeltaMovement(0, 0.05, 0);
                level.addFreshEntity(itemEntity);
            }
            ci.cancel();
        }
    }

    @Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "HEAD"), cancellable = true)
    private static void onDropResources(BlockState state, Level level, BlockPos pos, @Nullable BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
        AtomicBoolean nauseous = new AtomicBoolean(false);
        if (entity instanceof LivingEntity living) {
            living.getActiveEffects().forEach(effectInstance -> {
                if (effectInstance.getEffect() == MobEffects.CONFUSION) {
                    nauseous.set(true);
                }
            });
        }
        if (DrizzleproofConfig.COMMON.allBlocksStatic.get() || (!(nauseous.get() && DrizzleproofConfig.COMMON.nauseousDisarray.get()) &&
                (state.is(DrizzleproofBlockTags.STATIC_BLOCKS)
                || (DrizzleproofConfig.COMMON.silkBlocksStatic.get() && EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)))) {
            getDrops(state, (ServerLevel) level, pos, blockEntity, entity, stack).forEach((p_49925_) -> {
                float f = EntityType.ITEM.getHeight() / 2.0F;
                double d0 = (float) pos.getX() + 0.5F;
                double d1 = (double) ((float) pos.getY() + 0.5F) - (double) f;
                double d2 = (float) pos.getZ() + 0.5F;
                ItemEntity itemEntity = new ItemEntity(level, d0, d1, d2, p_49925_);
                if (!level.isClientSide && !p_49925_.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !level.restoringBlockSnapshots) {
                    itemEntity.setDeltaMovement(0, 0.05, 0);
                    level.addFreshEntity(itemEntity);
                }
            });
            state.spawnAfterBreak((ServerLevel) level, pos, stack, true);
            ci.cancel();
        }
    }
}
