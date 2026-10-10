package justjabka.doppelganger.common.impl;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.common.Registry;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseRegistry<T> implements Registry<T> {
    protected final Map<NamespacedKey, T> storage = new ConcurrentHashMap<>();

    @Override
    public void register(NamespacedKey key, T value) {
        if (storage.containsKey(key)) {
            Doppelganger.LOGGER.warn("Value with {} is already registered", key);
        }

        storage.put(key, value);
    }

    @Override
    public @Nullable T get(NamespacedKey key) {
        return storage.get(key);
    }

    @Override
    public T getOrThrow(NamespacedKey key) {
        T value = get(key);

        if (value == null) {
            throw new NoSuchElementException("No value for %s".formatted(key));
        }

        return value;
    }

    @Override
    public Set<NamespacedKey> keys() {
        return Collections.unmodifiableSet(storage.keySet());
    }

    @Override
    public Collection<T> values() {
        return Collections.unmodifiableCollection(storage.values());
    }
}
