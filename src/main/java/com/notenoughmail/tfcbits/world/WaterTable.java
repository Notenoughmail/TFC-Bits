package com.notenoughmail.tfcbits.world;

import com.notenoughmail.tfcbits.util.mixin.accessor.BiomeManagerAccessor;
import com.notenoughmail.tfcbits.world.noise.SimpleWeightedNoise2D;
import net.dries007.tfc.world.biome.BiomeExtension;
import net.dries007.tfc.world.biome.TFCBiomes;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;

public class WaterTable {

    private static final List<TagKey<Biome>> SEA_LEVEL_WATER_BIOME_TAGS = new ArrayList<>();

    /**
     * Creates a SimpleWightedNoise2D representative of the provided level's water table
     * @param level The level to use
     * @return A Noise2D representing the water table
     */
    public static SimpleWeightedNoise2D createWaterTableNoise(Level level) {
        long seed = ((BiomeManagerAccessor) level.getBiomeManager()).accessor$getBiomeZoomSeed();
        int seaLevel = level.getSeaLevel();
        return new SimpleWeightedNoise2D(new Noise2D[] {
                new OpenSimplex2D(seed).octaves(4).spread(0.1F).scaled(seaLevel - 15, seaLevel + 4), // Default
                new OpenSimplex2D((seed << 12) * 145248894662254L).octaves(3).spread(0.73F).scaled(seaLevel + 2, seaLevel + 25), // Mountainous terrain
                (x, y) -> seaLevel // Sea level
        }, new Integer[] {95, 3, 2});
    }

    public static void init() {
        SEA_LEVEL_WATER_BIOME_TAGS.add(BiomeTags.IS_OCEAN);
        SEA_LEVEL_WATER_BIOME_TAGS.add(BiomeTags.IS_RIVER);
        SEA_LEVEL_WATER_BIOME_TAGS.add(BiomeTags.IS_DEEP_OCEAN);
    }

    /**
     * Used to determine if a certain position is below the provided level's simulated water table, accounting for things like oceans, rivers, lakes, mountains, etc.
     * @param level The current level
     * @param pos The BlockPos to be checked
     * @return True if the BlockPos is below the level's local water table
     */
    public static boolean isBelowWaterTable(Level level, BlockPos pos) {
        SimpleWeightedNoise2D noise = createWaterTableNoise(level);
        Holder<Biome> biomeHolder = level.getBiomeManager().getBiome(pos);
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();

        // Use biome extensions since TFC doesn't tag their biomes
        BiomeExtension biomeExtension = TFCBiomes.getExtension(level, biomeHolder.value());
        if (biomeExtension != null) {
            ChunkAccess chunk = level.getChunk(pos.getX(), pos.getZ(), ChunkStatus.BIOMES, false);
            if (biomeExtension.isRiver() || biomeExtension.isShore() || biomeExtension.getGroup() == BiomeExtension.Group.OCEAN) {
                noise.changeWeight(2, 250);
            } else if (biomeExtension.getGroup() == BiomeExtension.Group.LAKE && chunk != null) {
                return y < (chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 3 + noise.noise(x, z)) / 2;
            } else if (biomeExtension == TFCBiomes.MOUNTAINS || biomeExtension == TFCBiomes.OLD_MOUNTAINS) {
                noise.changeWeight(1, 400);
            }
        }

        // Handle tagged biomes
        if (biomeHolder.is(BiomeTags.IS_MOUNTAIN)) {
            noise.changeWeight(1, 450);
        } else {
            for (TagKey<Biome> tag : SEA_LEVEL_WATER_BIOME_TAGS) {
                if (biomeHolder.is(tag)) {
                    noise.changeWeight(2, 250);
                }
            }
        }

        return y <= noise.noise(x, z);
    }
}