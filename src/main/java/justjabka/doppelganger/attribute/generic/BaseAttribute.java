package justjabka.doppelganger.attribute.generic;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public abstract class BaseAttribute implements Keyed, Translatable {

    /**
     * {@return the sentiment of this attribute}
     */
    @NotNull
    public abstract Attribute.Sentiment getSentiment();

    /**
     * {@return the default value of this attribute}
     * <p>
     * Default attribute values may differ between entity types, use {@link EntityType#getDefaultAttributes()} to get default attribute values for a specific entity type.
     */
    public abstract double getDefaultValue();
}
