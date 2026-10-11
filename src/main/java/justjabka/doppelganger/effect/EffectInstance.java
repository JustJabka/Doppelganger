package justjabka.doppelganger.effect;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.common.PersistentHolder;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

import java.util.Random;

@NullMarked
public class EffectInstance implements PersistentHolder {

    private static final Random RANDOM = new Random();

    private final Effect effect;
    private int duration;
    private int amplifier;
    private boolean visible;

    public EffectInstance(Effect effect) {
        this.effect = effect;
        this.duration = EffectManager.DEFAULT_DURATION;
        this.amplifier = EffectManager.DEFAULT_AMPLIFIER;
        this.visible = EffectManager.DEFAULT_VISIBILITY;
    }

    public EffectInstance(Effect effect, int duration) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = EffectManager.DEFAULT_AMPLIFIER;
        this.visible = EffectManager.DEFAULT_VISIBILITY;
    }

    public EffectInstance(Effect effect, int duration, int amplifier) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.visible = EffectManager.DEFAULT_VISIBILITY;
    }

    public EffectInstance(Effect effect, int duration, int amplifier, boolean visible) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.visible = visible;
    }

    @Override
    public NamespacedKey getKey() {
        return this.effect.getKey();
    }

    @Override
    public NamespacedKey getContainerKey() {
        return EffectManager.EFFECTS_KEY;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void start(LivingEntity entity) {
        updateEntityData(entity);
        this.effect.onStart(entity, this.amplifier);
    }

    public void tick(LivingEntity entity) {
        if (isEnded()) {
            end(entity);
            return;
        }

        if (isFinite()) {
            decreaseDuration();
        }

        this.effect.onTick(entity, this.amplifier);

        if (this.isVisible()) {
            showParticles(entity);
        }

        updateEntityData(entity);
    }

    public void end(LivingEntity entity) {
        this.effect.onEnd(entity);

        removeContainerData(entity, getKey());
    }

    public void updateEntityData(LivingEntity entity) {
        setEntryInt(entity, EffectManager.DURATION_KEY, this.duration);
        setEntryInt(entity, EffectManager.AMPLIFIER_KEY, this.amplifier);
        setEntryBoolean(entity, EffectManager.PARTICLES_KEY, this.visible);
    }

    private void decreaseDuration() {
        this.duration -= 1;
    }

    public boolean update(LivingEntity entity, EffectInstance takeOver) {
        boolean changed = false;

        if (!takeOver.effect.equals(this.effect)) {
            Doppelganger.LOGGER.warn("This method should only be called for matching effects!");
            return changed;
        }

        if (takeOver.amplifier > this.amplifier) {
            this.amplifier = takeOver.amplifier;
            this.duration = takeOver.duration;
            changed = true;
        } else if (this.isShorterDurationThan(takeOver)) {
            if (takeOver.amplifier == this.amplifier) {
                this.duration = takeOver.duration;
                changed = true;
            }
        }

        if (takeOver.visible != this.visible) {
            this.visible = takeOver.visible;
            changed = true;
        }

        if (changed) {
            updateEntityData(entity);
            this.effect.onUpdated(entity, this.amplifier);
        }

        return changed;
    }

    private boolean isShorterDurationThan(final EffectInstance other) {
        return this.isFinite() && (this.duration < other.duration || other.isInfinite());
    }

    public boolean isEnded() {
        return this.duration <= 0 && isFinite();
    }

    public boolean isInfinite() {
        return this.duration == EffectManager.INFINITE_DURATION;
    }

    public boolean isFinite() {
        return !isInfinite();
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
                this.effect.getColor()
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
}
