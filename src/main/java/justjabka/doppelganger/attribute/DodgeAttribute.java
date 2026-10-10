package justjabka.doppelganger.attribute;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.attribute.generic.BaseAttribute;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.jspecify.annotations.NonNull;

public class DodgeAttribute extends BaseAttribute {

    @Override
    public @NonNull NamespacedKey getKey() {
        return Doppelganger.key("dodge");
    }

    @Override
    public @NonNull String translationKey() {
        return "attribute.test.dodge";
    }

    @Override
    public Attribute.@NonNull Sentiment getSentiment() {
        return Attribute.Sentiment.POSITIVE;
    }

    @Override
    public double getDefaultValue() {
        return 0;
    }
}
