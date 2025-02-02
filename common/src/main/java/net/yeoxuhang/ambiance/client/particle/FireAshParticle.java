package net.yeoxuhang.ambiance.client.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class FireAshParticle extends TextureSheetParticle {
    private final float rotSpeed;
    private final SpriteSet sprites;

    protected FireAshParticle(ClientLevel clientLevel, SpriteSet spriteSet, double d, double e, double f, double g, double h, double i) {
        super(clientLevel, d, e, f, g, h, i);
        this.pickSprite(spriteSet);
        this.rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
        this.roll = (float)Math.random() * 6.2831855F;
        this.sprites = spriteSet;
    }

    @Override
    public float getQuadSize(float f) {
        return 0.07F * Mth.clamp(((float)this.age + f) / (float)this.lifetime * 64.0F, 0.0F, 1.0F);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            if (this.age > this.lifetime / 2) {
                this.setAlpha(1.0F - ((float)this.age - (float)(this.lifetime / 2)) / (float)this.lifetime);
            }
            this.setSpriteFromAge(this.sprites);
            this.oRoll = this.roll;
            this.roll += 3.1415927F * this.rotSpeed * 2.0F;
            if (this.onGround) {
                this.oRoll = this.roll = 0.0F;
            }

            this.move(this.xd, this.yd, this.zd);
            this.yd += 0.003000000026077032;
            this.yd = Math.max(this.yd, +0.1000000059604645);
        }
    }

    @Override
    public int getLightColor(float f) {
        int i = 200;
        float g = (float)this.age / (float)this.lifetime;
        g *= g;
        g *= g;
        int j = i & 255;
        int k = 0;
        k += (int)(g * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<ColorParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public FireAshParticle createParticle(ColorParticleOption colorParticleOption, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            RandomSource randomSource = clientLevel.random;
            double j = randomSource.nextGaussian() * 0.999999974752427E-10;
            double k = randomSource.nextGaussian() * 0.999999747378752E-3;
            double l = randomSource.nextGaussian() * 0.999999974752427E-10;
            FireAshParticle fireAshParticle = new FireAshParticle(clientLevel, this.sprites, d, e, f, j, k, l);
            fireAshParticle.setColor(colorParticleOption.getRed(), colorParticleOption.getGreen(), colorParticleOption.getBlue());
            fireAshParticle.setLifetime(randomSource.nextInt(10) + 20);
            fireAshParticle.setAlpha(1.0F);
            fireAshParticle.setSize(0.5F, 0.5F);
            return fireAshParticle;
        }
    }
}