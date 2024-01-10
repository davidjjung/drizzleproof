package com.davigj.drizzleproof.core.other;

import com.davigj.drizzleproof.core.Drizzleproof;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DrizzleproofBlockTags {
    public static final TagKey<Block> STATIC_BLOCKS = blockTag("static_blocks");

    private static TagKey<Block> blockTag(String name) {
        return TagUtil.blockTag(Drizzleproof.MOD_ID, name);
    }
}
