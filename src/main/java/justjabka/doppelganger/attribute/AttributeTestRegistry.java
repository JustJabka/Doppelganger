package justjabka.doppelganger.attribute;

import justjabka.doppelganger.DoppelgangerRegistries;
import justjabka.doppelganger.attribute.generic.BaseAttribute;

public class AttributeTestRegistry {
    public static void register() {
        registerAttribute(new DodgeAttribute());
    }

    private static void registerAttribute(BaseAttribute attribute) {
        DoppelgangerRegistries.ATTRIBUTES.register(attribute.getKey(), attribute);
    }
}
