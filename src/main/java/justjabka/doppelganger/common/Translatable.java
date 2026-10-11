package justjabka.doppelganger.common;

import net.kyori.adventure.text.Component;

public interface Translatable extends net.kyori.adventure.translation.Translatable {

    default Component getDisplayName() {
        return Component.translatable(translationKey());
    }
}
