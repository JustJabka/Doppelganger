package justjabka.doppelganger.effect.showcase;

import justjabka.doppelganger.DoppelgangerRegistries;
import justjabka.doppelganger.effect.Effect;

public class EffectShowcaseRegistry {

    public static void register() {
        registerEffect(new BleedingEffect());
        registerEffect(new IronSkinEffect());
    }

    private static void registerEffect(Effect effect) {
        DoppelgangerRegistries.EFFECTS.register(effect.getKey(), effect);
    }
}
