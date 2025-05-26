package com.davigj.drizzleproof.core.other;

import com.davigj.drizzleproof.core.Drizzleproof;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class DrizzleproofEnchantmentTags {
    public static final TagKey<Enchantment> DRIZZLEPROOF = enchantmentTag("drizzleproof");

    public static TagKey<Enchantment> enchantmentTag(String name) {
        return TagKey.create(Registries.ENCHANTMENT, Drizzleproof.location(name));
    }
}
