package justjabka.doppelganger.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.DoppelgangerRegistries;
import justjabka.doppelganger.common.Registry;
import justjabka.doppelganger.effect.Effect;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EffectArgument extends CustomRegistryArgument<Effect> {

    @Override
    public Registry<Effect> getRegistry() {
        return DoppelgangerRegistries.EFFECTS;
    }

    @Override
    public Effect convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), Doppelganger.getInstance());

        if (key == null) {
            throw new IllegalArgumentException("Unknown effect");
        }

        return getRegistry().getOrThrow(key);
    }
}
