package com.davigj.drizzleproof.core.other;

import com.davigj.drizzleproof.core.Drizzleproof;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DrizzleproofBlockTags {
    public static final TagKey<Block> STATIC_BLOCKS = blockTag("static_blocks");

    public static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(Drizzleproof.MOD_ID, name));
    }
}
