package com.notenoughmail.tfcbits.world;

import com.notenoughmail.tfcbits.util.config.BitsServerConfig;
import com.notenoughmail.tfcbits.util.mixin.accessor.BiomeManagerAccessor;
import net.dries007.tfc.world.biome.BiomeExtension;
import net.dries007.tfc.world.biome.TFCBiomes;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;

import java.util.ArrayList;
import java.util.List;

public class WaterTable {

    private static final List<TagKey<Biome>> SEA_LEVEL_WATER_BIOME_TAGS = new ArrayList<>();

    public static void init() {
        SEA_LEVEL_WATER_BIOME_TAGS.add(BiomeTags.IS_OCEAN);
        SEA_LEVEL_WATER_BIOME_TAGS.add(BiomeTags.IS_RIVER);
        SEA_LEVEL_WATER_BIOME_TAGS.add(BiomeTags.IS_DEEP_OCEAN);
    }

    /**
     * Used to determine if a certain position is below the provided level's simulated water table
     * @param level The current level
     * @param pos The BlockPos to be checked
     * @return True if the BlockPos is below the level's local water table
     */
    public static boolean isBelowWaterTable(Level level, BlockPos pos) {
        BiomeManager biomeManager = level.getBiomeManager();
        Holder<Biome> biomeHolder = biomeManager.getBiome(pos);
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();

        // Handle TFC water biomes
        BiomeExtension biomeExtension = TFCBiomes.getExtension(level, biomeHolder.value());
        if (biomeExtension != null) {
            if (biomeExtension.isRiver() || biomeExtension.isShore() || biomeExtension.getGroup() == BiomeExtension.Group.OCEAN) {
                return y < level.getSeaLevel();
            } else if (biomeExtension.getGroup() == BiomeExtension.Group.LAKE) {
                return true; // Might change
            }
        }

        // Handle vanilla water biomes
        for (TagKey<Biome> tag : SEA_LEVEL_WATER_BIOME_TAGS) {
            if (biomeHolder.is(tag) && y < level.getSeaLevel()) {
                return true;
            }
        }

        Noise2D noise = new OpenSimplex2D(((BiomeManagerAccessor) biomeManager).accessor$getBiomeZoomSeed()).octaves(4).spread(0.1F).scaled(level.getSeaLevel() - 15, level.getSeaLevel() + 4);
        return y <= noise.noise(x, z);
    }
}
