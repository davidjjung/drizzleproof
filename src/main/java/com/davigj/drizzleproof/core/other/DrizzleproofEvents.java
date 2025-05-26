package com.davigj.drizzleproof.core.other;

import com.davigj.drizzleproof.core.Drizzleproof;
import com.davigj.drizzleproof.core.DrizzleproofConfig;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = Drizzleproof.MOD_ID)
public class DrizzleproofEvents {
    @SubscribeEvent
    public static void isItThatEasy(BlockDropsEvent event) {
        Entity entity = event.getBreaker();
        boolean sick = DrizzleproofConfig.COMMON.nauseousDisarray.get() && entity instanceof LivingEntity living && living.hasEffect(MobEffects.CONFUSION);
        boolean silk = EnchantmentHelper.hasTag(event.getTool(), DrizzleproofEnchantmentTags.DRIZZLEPROOF) && DrizzleproofConfig.COMMON.silkBlocksStatic.get();
        boolean tagged = event.getState().is(DrizzleproofBlockTags.STATIC_BLOCKS);
        boolean ollKorrect = DrizzleproofConfig.COMMON.allBlocksStatic.get();
        if (sick) return;
        if (ollKorrect || (silk || tagged)) {
            float f = EntityType.ITEM.getHeight() / 2.0F;
            double d1 = (double) ((float) event.getPos().getY() + 0.5F) - (double) f;
            event.getDrops().forEach(itemEntity -> {
                itemEntity.setDeltaMovement(0, DrizzleproofConfig.COMMON.pepStep.get(), 0);
                itemEntity.setPos(event.getPos().getX() + 0.5F, d1, event.getPos().getZ() + 0.5F);
            });
        }
    }
}
