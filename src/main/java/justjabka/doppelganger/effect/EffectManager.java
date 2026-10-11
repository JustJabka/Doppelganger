package justjabka.doppelganger.effect;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.DoppelgangerRegistries;
import justjabka.doppelganger.effect.generic.BaseEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

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

    public static final boolean DEFAULT_PARTICLES = true;
    public static final int INFINITE_DURATION = -1;

    public static final int MAX_DURATION = Integer.MAX_VALUE;
    public static final int MAX_AMPLIFIER = 255;

    // Methods
    public static Set<BaseEffect> getActiveEffects(LivingEntity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        PersistentDataContainer container = pdc.getOrDefault(
                EFFECTS_KEY,
                PersistentDataType.TAG_CONTAINER,
                pdc.getAdapterContext().newPersistentDataContainer()
        );

        final Set<BaseEffect> activeEffects = new HashSet<>();

        for (NamespacedKey key : container.getKeys()) {
            BaseEffect effect = DoppelgangerRegistries.EFFECTS.get(key);
            if (effect == null) continue;

            activeEffects.add(effect);
        }

        return activeEffects;
    }

    public static void clearAllEffects(LivingEntity entity) {
        Set<BaseEffect> activeEffects = getActiveEffects(entity);

        for (BaseEffect effect : activeEffects) {
            effect.end(entity);
        }
    }
}
