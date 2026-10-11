package justjabka.doppelganger.effect.generic;

import justjabka.doppelganger.common.PersistentHolder;
import justjabka.doppelganger.effect.EffectManager;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

import java.util.Random;

public abstract class BaseEffect implements Keyed, Translatable, PersistentHolder {

    private static final Random RANDOM = new Random();

    @Override
    public NamespacedKey getContainerKey() {
        return EffectManager.EFFECTS_KEY;
    }

    public int getDuration(Player player) {
        return getEntryInt(player, EffectManager.DURATION_KEY);
    }

    public void setDuration(Player player, @Range(from = 0, to = EffectManager.MAX_DURATION) int duration) {
        setEntryInt(player, EffectManager.DURATION_KEY, duration);
    }

    public int getAmplifier(Player player) {
        return getEntryInt(player, EffectManager.AMPLIFIER_KEY);
    }

    public void setAmplifier(Player player, @Range(from = 0, to = EffectManager.MAX_AMPLIFIER) int duration) {
        setEntryInt(player, EffectManager.AMPLIFIER_KEY, duration);
    }

    // TODO: make particle fully configurable
    @NonNull
    public abstract Color getColor();

    public boolean getParticles(Player player) {
        return getEntryBoolean(player, EffectManager.PARTICLES_KEY);
    }

    public void setParticlesVisibility(Player player, boolean particles) {
        setEntryBoolean(player, EffectManager.PARTICLES_KEY, particles);
    }

    public boolean isEnded(Player player) {
        return getDuration(player) <= 0 && isFinite(player);
    }

    public boolean isInfinite(Player player) {
        return getDuration(player) == EffectManager.INFINITE_DURATION;
    }

    public boolean isFinite(Player player) {
        return !isInfinite(player);
    }

    protected abstract void onStart(Player player);
    protected abstract void onTick(Player player);
    protected abstract void onEnd(Player player);

    public void start(Player player) {
        start(player, EffectManager.DEFAULT_DURATION);
    }

    public void start(Player player, int duration) {
        start(player, duration, EffectManager.DEFAULT_AMPLIFIER);
    }

    public void start(Player player, int duration, int amplifier) {
        start(player, duration, amplifier, EffectManager.DEFAULT_PARTICLES);
    }

    public void start(Player player, int duration, int amplifier, boolean particles) {
        setDuration(player, duration);
        setAmplifier(player, amplifier);
        setParticlesVisibility(player, particles);

        onStart(player);
    }

    public void tick(Player player) {
        if (isEnded(player) || !player.isValid()) {
            end(player);
            return;
        }

        if (isFinite(player)) {
            decreaseDuration(player);
        }

        onTick(player);

        if (getParticles(player)) {
            showParticles(player);
        }
    }

    public void end(Player player) {
        onEnd(player);

        removeContainerData(player, getKey());
    }

    private void showParticles(Player player) {
        int bound = 4;

        if (RANDOM.nextInt(bound) != 0) return;

        player.getWorld().spawnParticle(
                Particle.ENTITY_EFFECT,
                getParticleLocation(player),
                1,
                0,
                0,
                0,
                getColor()
        );
    }

    private Location getParticleLocation(Player player) {
        Location location = player.getLocation();

        double bbWidth = player.getBoundingBox().getWidthX();
        double bbHeight = player.getBoundingBox().getHeight();

        double x = location.getX() + getParticleSpread(bbWidth);
        double y = location.getY() + (RANDOM.nextDouble() * bbHeight);
        double z = location.getZ() + getParticleSpread(bbWidth);

        return new Location(player.getWorld(), x, y, z);
    }

    private static double getParticleSpread(double bbWidth) {
        double spread = 0.5;
        double progress = (2.0 * RANDOM.nextDouble() - 1.0) * spread;

        return bbWidth * progress;
    }

    private void decreaseDuration(Player player) {
        setDuration(player, getDuration(player) - 1);
    }
}
