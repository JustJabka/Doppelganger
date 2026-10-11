package justjabka.doppelganger.effect;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.DoppelgangerRegistries;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@NullMarked
public final class EffectManager {

    private EffectManager() {}

    // Keys
    public static final NamespacedKey EFFECTS_KEY = Doppelganger.key("effects");
    public static final NamespacedKey DURATION_KEY = Doppelganger.key("duration");
    public static final NamespacedKey AMPLIFIER_KEY = Doppelganger.key("amplifier");

    public static final NamespacedKey PARTICLES_KEY = Doppelganger.key("particles");

    // Constants
    public static final int DEFAULT_DURATION = 90 * 20;
    public static final int DEFAULT_AMPLIFIER = 0;

    public static final boolean DEFAULT_VISIBILITY = true;
    public static final int INFINITE_DURATION = -1;

    public static final int MAX_DURATION = Integer.MAX_VALUE;
    public static final int MAX_AMPLIFIER = 255;

    // Methods
    private static PersistentDataContainer getEffectsContainer(LivingEntity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.getOrDefault(
                EFFECTS_KEY,
                PersistentDataType.TAG_CONTAINER,
                pdc.getAdapterContext().newPersistentDataContainer()
        );
    }

    public static Set<EffectInstance> getActiveEffects(LivingEntity entity) {
        PersistentDataContainer container = getEffectsContainer(entity);

        final Set<EffectInstance> activeEffects = new HashSet<>();

        for (NamespacedKey key : container.getKeys()) {
            Effect effect = DoppelgangerRegistries.EFFECTS.get(key);
            if (effect == null) continue;

            PersistentDataContainer data = container.get(key, PersistentDataType.TAG_CONTAINER);
            if (data == null) continue;

            int duration = data.getOrDefault(DURATION_KEY, PersistentDataType.INTEGER, DEFAULT_DURATION);
            int amplifier = data.getOrDefault(AMPLIFIER_KEY, PersistentDataType.INTEGER, DEFAULT_AMPLIFIER);
            boolean visible = data.getOrDefault(PARTICLES_KEY, PersistentDataType.BOOLEAN, DEFAULT_VISIBILITY);

            EffectInstance instance = new EffectInstance(effect, duration, amplifier, visible);
            activeEffects.add(instance);
        }

        return activeEffects;
    }

    public static @Nullable EffectInstance getActiveEffect(LivingEntity entity, Effect effect) {
        Set<EffectInstance> activeEffects = getActiveEffects(entity);

        for (EffectInstance instance : activeEffects) {
            if (!instance.getEffect().equals(effect)) continue;

            return instance;
        }

        return null;
    }

    public static boolean applyEffect(LivingEntity entity, EffectInstance newEffect) {
        EffectInstance currentEffect = getActiveEffect(entity, newEffect.getEffect());

        if (currentEffect == null) {
            newEffect.start(entity);
            return true;
        }

        return currentEffect.update(entity, newEffect);
    }

    public static boolean clearEffect(LivingEntity entity, EffectInstance effect) {
        effect.end(entity);
        return true;
    }

    public static boolean clearEffect(LivingEntity entity, Effect effect) {
        EffectInstance instance = EffectManager.getActiveEffect(entity, effect);
        if (instance == null) return false;

        return EffectManager.clearEffect(entity, instance);
    }

    public static boolean clearAllEffects(LivingEntity entity) {
        Set<EffectInstance> activeEffects = getActiveEffects(entity);
        if (activeEffects.isEmpty()) return false;

        for (EffectInstance effect : activeEffects) {
            clearEffect(entity, effect);
        }

        return true;
    }
}
