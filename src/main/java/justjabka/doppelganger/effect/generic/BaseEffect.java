package justjabka.doppelganger.effect.generic;

import justjabka.doppelganger.common.PersistentHolder;
import justjabka.doppelganger.effect.EffectManager;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

import java.util.Random;

public abstract class BaseEffect implements Keyed, Translatable, PersistentHolder {

    private static final Random RANDOM = new Random();

    @Override
    public NamespacedKey getContainerKey() {
        return EffectManager.EFFECTS_KEY;
    }

    public int getDuration(LivingEntity entity) {
        return getEntryInt(entity, EffectManager.DURATION_KEY);
    }

    public void setDuration(LivingEntity entity, @Range(from = 0, to = EffectManager.MAX_DURATION) int duration) {
        setEntryInt(entity, EffectManager.DURATION_KEY, duration);
    }

    public int getAmplifier(LivingEntity entity) {
        return getEntryInt(entity, EffectManager.AMPLIFIER_KEY);
    }

    public void setAmplifier(LivingEntity entity, @Range(from = 0, to = EffectManager.MAX_AMPLIFIER) int duration) {
        setEntryInt(entity, EffectManager.AMPLIFIER_KEY, duration);
    }

    // TODO: make particle fully configurable
    @NonNull
    public abstract Color getColor();

    public boolean getParticles(LivingEntity entity) {
        return getEntryBoolean(entity, EffectManager.PARTICLES_KEY);
    }

    public void setParticlesVisibility(LivingEntity entity, boolean particles) {
        setEntryBoolean(entity, EffectManager.PARTICLES_KEY, particles);
    }

    public boolean isEnded(LivingEntity entity) {
        return getDuration(entity) <= 0 && isFinite(entity);
    }

    public boolean isInfinite(LivingEntity entity) {
        return getDuration(entity) == EffectManager.INFINITE_DURATION;
    }

    public boolean isFinite(LivingEntity entity) {
        return !isInfinite(entity);
    }

    protected abstract void onStart(LivingEntity entity);
    protected abstract void onTick(LivingEntity entity);
    protected abstract void onEnd(LivingEntity entity);

    public void start(LivingEntity entity) {
        start(entity, EffectManager.DEFAULT_DURATION);
    }

    public void start(LivingEntity entity, int duration) {
        start(entity, duration, EffectManager.DEFAULT_AMPLIFIER);
    }

    public void start(LivingEntity entity, int duration, int amplifier) {
        start(entity, duration, amplifier, EffectManager.DEFAULT_PARTICLES);
    }

    public void start(LivingEntity entity, int duration, int amplifier, boolean particles) {
        setDuration(entity, duration);
        setAmplifier(entity, amplifier);
        setParticlesVisibility(entity, particles);

        onStart(entity);
    }

    public void tick(LivingEntity entity) {
        if (isEnded(entity) || !entity.isValid()) {
            end(entity);
            return;
        }

        if (isFinite(entity)) {
            decreaseDuration(entity);
        }

        onTick(entity);

        if (getParticles(entity)) {
            showParticles(entity);
        }
    }

    public void end(LivingEntity entity) {
        onEnd(entity);

        removeContainerData(entity, getKey());
    }

    private void showParticles(LivingEntity entity) {
        int bound = 4;

        if (RANDOM.nextInt(bound) != 0) return;

        entity.getWorld().spawnParticle(
                Particle.ENTITY_EFFECT,
                getParticleLocation(entity),
                1,
                0,
                0,
                0,
                getColor()
        );
    }

    private Location getParticleLocation(LivingEntity entity) {
        Location location = entity.getLocation();

        double bbWidth = entity.getBoundingBox().getWidthX();
        double bbHeight = entity.getBoundingBox().getHeight();

        double x = location.getX() + getParticleSpread(bbWidth);
        double y = location.getY() + (RANDOM.nextDouble() * bbHeight);
        double z = location.getZ() + getParticleSpread(bbWidth);

        return new Location(entity.getWorld(), x, y, z);
    }

    private static double getParticleSpread(double bbWidth) {
        double spread = 0.5;
        double progress = (2.0 * RANDOM.nextDouble() - 1.0) * spread;

        return bbWidth * progress;
    }

    private void decreaseDuration(LivingEntity player) {
        setDuration(player, getDuration(player) - 1);
    }
}
