package com.davigj.drizzleproof.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class DrizzleproofConfig {
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Boolean> allBlocksStatic;
        public final ForgeConfigSpec.ConfigValue<Boolean> silkBlocksStatic;

        Common (ForgeConfigSpec.Builder builder) {
            builder.push("changes");
            allBlocksStatic = builder.comment("Should all blocks drop items without scattering").define("Make all blocks static", false);
            silkBlocksStatic = builder.comment("Should all blocks mined with silk touch drop items without scattering").define("Silk touch drizzleproof", true);
            builder.pop();
        }
    }

    static final ForgeConfigSpec COMMON_SPEC;
    public static final DrizzleproofConfig.Common COMMON;


    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(DrizzleproofConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
