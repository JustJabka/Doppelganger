package justjabka.doppelganger.common;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface PersistentHolder extends Keyed {
    NamespacedKey getContainerKey();

    @NonNull
    default PersistentDataContainer getContainer(LivingEntity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.getOrDefault(
                getContainerKey(),
                PersistentDataType.TAG_CONTAINER,
                pdc.getAdapterContext().newPersistentDataContainer()
        );
    }

    default void saveContainer(LivingEntity entity, PersistentDataContainer container) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        pdc.set(
                getContainerKey(),
                PersistentDataType.TAG_CONTAINER,
                container
        );
    }

    @NonNull
    default PersistentDataContainer getEntry(LivingEntity entity) {
        PersistentDataContainer container = getContainer(entity);

        return container.getOrDefault(
                getKey(),
                PersistentDataType.TAG_CONTAINER,
                container.getAdapterContext().newPersistentDataContainer()
        );
    }

    default void saveEntry(LivingEntity entity, PersistentDataContainer entry) {
        PersistentDataContainer container = getContainer(entity);

        container.set(
                getKey(),
                PersistentDataType.TAG_CONTAINER,
                entry
        );

        saveContainer(entity, container);
    }

    /**
     * Checks if entity has container data in his container.
     * @param entity Entity that container would be checked
     * @param key Field key
     * @return {@code true} if entity has data in PDC
     */
    default boolean hasContainerData(LivingEntity entity, NamespacedKey key) {
        return getContainer(entity).has(key);
    }

    default boolean hasEntryData(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).has(key);
    }

    default <T, Z> void setContainerData(LivingEntity entity, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        PersistentDataContainer container = getContainer(entity);
        container.set(key, dataType, value);
        saveContainer(entity, container);
    }

    default <T, Z> void setEntryData(LivingEntity entity, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        PersistentDataContainer entry = getEntry(entity);
        entry.set(key, dataType, value);
        saveEntry(entity, entry);
    }

    /**
     * Removes container data from entity's PDC
     * @param entity Entity whose container data would be removed
     * @param key Container field key
     */
    default void removeContainerData(LivingEntity entity, NamespacedKey key) {
        PersistentDataContainer container = getContainer(entity);
        container.remove(key);
        saveContainer(entity, container);
    }

    default void removeEntryData(LivingEntity entity, NamespacedKey key) {
        PersistentDataContainer entry = getEntry(entity);
        entry.remove(key);
        saveEntry(entity, entry);
    }

    // region Primitives Getters
    default byte getEntryByte(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).getOrDefault(key, PersistentDataType.BYTE, (byte) 0);
    }

    default short getEntryShort(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).getOrDefault(key, PersistentDataType.SHORT, (short) 0);
    }

    default int getEntryInt(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    default long getEntryLong(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).getOrDefault(key, PersistentDataType.LONG, 0L);
    }

    default float getEntryFloat(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).getOrDefault(key, PersistentDataType.FLOAT, 0.0f);
    }

    default double getEntryDouble(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).getOrDefault(key, PersistentDataType.DOUBLE, 0.0);
    }

    default boolean getEntryBoolean(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    // endregion

    // region Object Getters

    default String getEntryString(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).get(key, PersistentDataType.STRING);
    }

    default UUID getEntryUUID(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).get(key, DataType.UUID);
    }
    default ItemStack[] getEntryInventory(LivingEntity entity, NamespacedKey key) {
        return getEntry(entity).getOrDefault(key, DataType.ITEM_STACK_ARRAY, new ItemStack[]{});
    }

    default PersistentDataContainer getEntryTagContainer(LivingEntity entity, NamespacedKey key) {
        PersistentDataContainer entry = getEntry(entity);
        return entry.getOrDefault(key, PersistentDataType.TAG_CONTAINER, entry.getAdapterContext().newPersistentDataContainer());
    }

    // endregion

    // region Primitives Setters
    default void setEntryByte(LivingEntity entity, NamespacedKey key, byte value) {
        setEntryData(entity, key, PersistentDataType.BYTE, value);
    }

    default void setEntryShort(LivingEntity entity, NamespacedKey key, short value) {
        setEntryData(entity, key, PersistentDataType.SHORT, value);
    }

    default void setEntryInt(LivingEntity entity, NamespacedKey key, int value) {
        setEntryData(entity, key, PersistentDataType.INTEGER, value);
    }

    default void setEntryLong(LivingEntity entity, NamespacedKey key, long value) {
        setEntryData(entity, key, PersistentDataType.LONG, value);
    }

    default void setEntryFloat(LivingEntity entity, NamespacedKey key, float value) {
        setEntryData(entity, key, PersistentDataType.FLOAT, value);
    }

    default void setEntryDouble(LivingEntity entity, NamespacedKey key, double value) {
        setEntryData(entity, key, PersistentDataType.DOUBLE, value);
    }

    default void setEntryBoolean(LivingEntity entity, NamespacedKey key, boolean state) {
        setEntryData(entity, key, PersistentDataType.BOOLEAN, state);
    }

    // endregion

    // region Object Setters
    default void setEntryString(LivingEntity entity, NamespacedKey key, String string) {
        setEntryData(entity, key, PersistentDataType.STRING, string);
    }

    default void setEntryUUID(LivingEntity entity, NamespacedKey key, UUID uuid) {
        setEntryData(entity, key, DataType.UUID, uuid);
    }

    default void setEntryInventory(LivingEntity entity, NamespacedKey key, ItemStack[] items) {
        setEntryData(entity, key, DataType.ITEM_STACK_ARRAY, items);
    }

    default void setEntryTagContainer(LivingEntity entity, NamespacedKey key, PersistentDataContainer tagContainer) {
        setEntryData(entity, key, PersistentDataType.TAG_CONTAINER, tagContainer);
    }
    // endregion
}
