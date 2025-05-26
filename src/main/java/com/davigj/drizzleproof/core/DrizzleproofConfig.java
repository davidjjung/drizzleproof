package com.davigj.drizzleproof.core;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class DrizzleproofConfig {
    public static class Common {
        public final ModConfigSpec.ConfigValue<Boolean> allBlocksStatic;
        public final ModConfigSpec.ConfigValue<Boolean> silkBlocksStatic;
        public final ModConfigSpec.ConfigValue<Boolean> nauseousDisarray;
        public final ModConfigSpec.ConfigValue<Double> pepStep;

        Common (ModConfigSpec.Builder builder) {
            builder.push("changes");
            allBlocksStatic = builder.comment("Should all blocks drop items without scattering").define("Make all blocks static", false);
            silkBlocksStatic = builder.comment("Should all blocks mined with silk touch drop items without scattering").define("Silk touch drizzleproof", true);
            nauseousDisarray = builder.comment("If a player has Nausea, all of their block drops scatter").define("Nauseous drizzle", false);
            pepStep = builder.comment("Initial vertical velocity for dropped drizzleproof item entities").defineInRange("Item entity drop bounce multiplier", 0.085, 0, 0.2);
            builder.pop();
        }
    }

    static final ModConfigSpec COMMON_SPEC;
    public static final DrizzleproofConfig.Common COMMON;

    static {
        Pair<Common, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = (ModConfigSpec) commonSpecPair.getRight();
        COMMON = (Common) commonSpecPair.getLeft();
    }
}
