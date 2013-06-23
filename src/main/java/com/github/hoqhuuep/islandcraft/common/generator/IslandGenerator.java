package com.github.hoqhuuep.islandcraft.common.generator;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld2;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public final class IslandGenerator implements ICGenerator {
    private final ICWorld2 world;
    private final int islandSize;
    private final int islandSeparation;
    private final int oceanBiome;
    private final ICDatabase database;

    public IslandGenerator(final int islandSize, final int islandGap, final ICWorld2 world, final int oceanBiome, final ICDatabase database) {
        this.islandSize = islandSize;
        this.islandSeparation = islandSize + islandGap;
        this.world = world;
        this.oceanBiome = oceanBiome;
        this.database = database;
    }

    @Override
    public int biomeAt(final int x, final int z) {
        final int xx = x + this.islandSize / 2;
        final int zz = z + this.islandSize / 2;
        final int row = IslandMath.div(zz, this.islandSeparation);
        final int xxx;
        if (row % 2 == 0) {
            xxx = xx;
        } else {
            xxx = xx + this.islandSeparation / 2;
        }
        final int col = IslandMath.div(xxx, this.islandSeparation);
        final int rx = IslandMath.mod(xxx, this.islandSeparation);
        final int rz = IslandMath.mod(zz, this.islandSeparation);
        final int cz = row * this.islandSeparation;
        final int cx;
        if (row % 2 == 0) {
            cx = col * this.islandSeparation;
        } else {
            cx = col * this.islandSeparation - this.islandSeparation / 2;
        }
        if (rx >= this.islandSize || rz >= this.islandSize) {
            return this.oceanBiome;
        }
        return islandBiome(islandSeed(cx, cz), rx, rz);
    }

    @Override
    public int[] biomeChunk(final int x, final int z, final int[] result) {
        final int xx = x + this.islandSize / 2;
        final int zz = z + this.islandSize / 2;
        final int row = IslandMath.div(zz, this.islandSeparation);
        final int xxx;
        if (row % 2 == 0) {
            xxx = xx;
        } else {
            xxx = xx + this.islandSeparation / 2;
        }
        final int col = IslandMath.div(xxx, this.islandSeparation);
        final int rx = IslandMath.mod(xxx, this.islandSeparation);
        final int rz = IslandMath.mod(zz, this.islandSeparation);
        final int cz = row * this.islandSeparation;
        final int cx;
        if (row % 2 == 0) {
            cx = col * this.islandSeparation;
        } else {
            cx = col * this.islandSeparation - this.islandSeparation / 2;
        }
        if (rx >= this.islandSize || rz >= this.islandSize) {
            Arrays.fill(result, this.oceanBiome);
            return result;
        }
        return islandChunk(islandSeed(cx, cz), rx, rz, result);
    }

    /**
     * @param rx
     *            x position relative to island in range [0, island-size)
     * @param rz
     *            z position relative to island in range [0, island-size)
     */
    private int islandBiome(final long islandSeed, final int rx, final int rz) {
        return IslandCache.getBiome(rx, rz, this.islandSize, this.islandSize, islandSeed, this.world);
    }

    private int[] islandChunk(final long islandSeed, final int rx, final int rz, final int[] result) {
        return IslandCache.getChunk(rx, rz, this.islandSize, this.islandSize, islandSeed, this.world, result);
    }

    private long islandSeed(final int cx, final int cz) {
        ICLocation location = new ICLocation(this.world.getName(), cx, cz);
        Long oldSeed = this.database.loadIslandSeed(location);
        if (oldSeed == null) {
            Long newSeed = new Long(this.world.getSeed() ^ (cx + (((long) cz) << 32)));
            this.database.saveIslandSeed(location, newSeed);
            return newSeed.longValue();
        }
        return oldSeed.longValue();
    }
}
