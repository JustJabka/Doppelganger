package justjabka.doppelganger.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.DoppelgangerRegistries;
import justjabka.doppelganger.common.Registry;
import justjabka.doppelganger.effect.generic.BaseEffect;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EffectArgument extends CustomRegistryArgument<BaseEffect> {

    @Override
    public Registry<BaseEffect> getRegistry() {
        return DoppelgangerRegistries.EFFECTS;
    }

    @Override
    public BaseEffect convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), Doppelganger.getInstance());

        if (key == null) {
            throw new IllegalArgumentException("Unknown effect");
        }

        return getRegistry().getOrThrow(key);
    }
}
