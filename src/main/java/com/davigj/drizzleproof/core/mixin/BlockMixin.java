package com.davigj.drizzleproof.core.mixin;

import com.davigj.drizzleproof.core.DrizzleproofConfig;
import com.davigj.drizzleproof.core.other.DrizzleproofBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void popMoreLikeLock(Level level, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (level.getBlockState(pos).is(DrizzleproofBlockTags.STATIC_BLOCKS) || DrizzleproofConfig.COMMON.allBlocksStatic.get()) {
            float f = EntityType.ITEM.getHeight() / 2.0F;
            double d0 = (double)((float)pos.getX() + 0.5F);
            double d1 = (double)((float)pos.getY() + 0.5F) - (double)f;
            double d2 = (double)((float)pos.getZ() + 0.5F);
            ItemEntity itemEntity = new ItemEntity(level, d0, d1, d2, stack);
            itemEntity.setDeltaMovement(0, -0.05, 0);
            level.addFreshEntity(itemEntity);
            ci.cancel();
        }
    }
}
