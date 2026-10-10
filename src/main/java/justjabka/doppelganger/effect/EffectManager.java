package justjabka.doppelganger.effect;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.DoppelgangerRegistries;
import justjabka.doppelganger.effect.generic.BaseEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NullMarked
public final class EffectManager {

    public static final NamespacedKey EFFECTS_KEY = Doppelganger.key("effects");

    public static final NamespacedKey DURATION_KEY = Doppelganger.key("duration");
    public static final NamespacedKey AMPLIFIER_KEY = Doppelganger.key("amplifier");

    private EffectManager() {}

    public static Set<BaseEffect> getActiveEffects(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
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

    public static void clearAllEffects(Player player) {
        Set<BaseEffect> activeEffects = getActiveEffects(player);

        for (BaseEffect effect : activeEffects) {
            effect.end(player);
        }
    }
}
