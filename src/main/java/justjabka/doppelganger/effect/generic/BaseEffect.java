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

    public static final int DEFAULT_DURATION = 90 * 20;
    public static final int DEFAULT_AMPLIFIER = 0;

    @Override
    public NamespacedKey getContainerKey() {
        return EffectManager.EFFECTS_KEY;
    }

    public int getDuration(Player player) {
        return getEntryInt(player, EffectManager.DURATION_KEY);
    }

    public void setDuration(Player player, @Range(from = 0, to = Integer.MAX_VALUE) int duration) {
        setEntryInt(player, EffectManager.DURATION_KEY, duration);
    }

    public int getAmplifier(Player player) {
        return getEntryInt(player, EffectManager.DURATION_KEY);
    }

    public void setAmplifier(Player player, @Range(from = 0, to = 255) int duration) {
        setEntryInt(player, EffectManager.AMPLIFIER_KEY, duration);
    }

    // TODO: make particle fully configurable
    @NonNull
    public abstract Color getColor();

    public boolean isEnded(Player player) {
        return getDuration(player) == 0;
    }

    public abstract void onStart(Player player);
    public abstract void onTick(Player player);

    public abstract void onEnd(Player player);

    public void start(Player player) {
        start(player, DEFAULT_DURATION, DEFAULT_AMPLIFIER);
    }

    public void start(Player player, int duration) {
        start(player, duration, DEFAULT_AMPLIFIER);
    }

    public void start(Player player, int duration, int amplifier) {
        setDuration(player, duration);
        setAmplifier(player, amplifier);

        onStart(player);
    }

    public void tick(Player player) {
        if (isEnded(player)) {
            end(player);
            return;
        }

        decreaseDuration(player);

        onTick(player);

        showParticles(player);
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
