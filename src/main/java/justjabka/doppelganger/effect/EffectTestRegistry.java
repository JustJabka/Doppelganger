package justjabka.doppelganger.effect;

import justjabka.doppelganger.DoppelgangerRegistries;
import justjabka.doppelganger.effect.generic.BaseEffect;

public class EffectTestRegistry {

    public static void register() {
        registerEffect(new BleedingEffect());
    }

    private static void registerEffect(BaseEffect effect) {
        DoppelgangerRegistries.EFFECTS.register(effect.getKey(), effect);
    }
}
