package justjabka.doppelganger.common;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public interface Registry<T> {

    void register(NamespacedKey key, T value);

    @Nullable T get(NamespacedKey key);
    T getOrThrow(NamespacedKey key);

    Collection<T> values();
    Set<NamespacedKey> keys();
}
