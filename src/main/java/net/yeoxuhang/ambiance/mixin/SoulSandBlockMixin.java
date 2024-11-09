package net.yeoxuhang.ambiance.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.yeoxuhang.ambiance.Ambiance;
import net.yeoxuhang.ambiance.util.ParticlesUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(SoulSandBlock.class)
public class SoulSandBlockMixin extends Block {
    public SoulSandBlockMixin(Properties properties) {
        super(properties);
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if(blockState.getBlock() instanceof SoulSandBlock && randomSource.nextInt(1000) == 1 && Ambiance.config.blocks.soulSand.enableParticle && ParticlesUtil.isBiome(level, blockPos, Biomes.SOUL_SAND_VALLEY)) {
            double x = blockPos.getX() + randomSource.nextDouble();
            double y = blockPos.getY() + 1.2;
            double z = blockPos.getZ() + randomSource.nextDouble();
            level.addAlwaysVisibleParticle(ParticleTypes.SOUL, x, y, z, 0, 0, 0);
        }
        if (randomSource.nextInt(16) == 0 && isFree(level.getBlockState(blockPos.below())) && Ambiance.config.blocks.soulSand.enableParticle) {
            ParticlesUtil.spawnParticleBelow(level, blockPos, randomSource, new BlockParticleOption(ParticleTypes.FALLING_DUST, blockState));
        }
    }

    @Unique
    private static boolean isFree(BlockState p_185759_0_) {
        Material material = p_185759_0_.getMaterial();
        return p_185759_0_.isAir() || p_185759_0_.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }
}
