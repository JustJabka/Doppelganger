package justjabka.doppelganger;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import justjabka.doppelganger.common.EffectHolder;
import justjabka.doppelganger.effect.Effect;
import justjabka.doppelganger.effect.EffectInstance;
import justjabka.doppelganger.effect.EffectManager;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class DoppelgangerItems {

    private DoppelgangerItems() {}

    public static final NamespacedKey EFFECT_HOLDER_KEY = Doppelganger.key("effect_holder");
    public static final NamespacedKey ID_KEY = Doppelganger.key("id");
    public static final NamespacedKey TYPE_KEY = Doppelganger.key("type");

    public static ItemStack getEffectHolder(EffectInstance effect, EffectHolder holder) {
        ItemStack item = new ItemStack(holder.getMaterial());

        Effect effectType = effect.getEffect();

        // Add potion contents
        item.setData(DataComponentTypes.POTION_CONTENTS, PotionContents.potionContents()
                .customColor(effectType.getColor())
                .customName(effectType.getPotionName())
                .build()
        );

        // Add marker
        ItemMeta meta = item.getItemMeta();

        NamespacedKey markerKey = Doppelganger.key(holder.toString());

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        PersistentDataContainer container = pdc.getOrDefault(EFFECT_HOLDER_KEY, PersistentDataType.TAG_CONTAINER, pdc.getAdapterContext().newPersistentDataContainer());

        container.set(TYPE_KEY, PersistentDataType.STRING, markerKey.asString());

        container.set(ID_KEY, PersistentDataType.STRING, effect.getKey().asString());
        container.set(EffectManager.DURATION_KEY, PersistentDataType.INTEGER, effect.getDuration());
        container.set(EffectManager.AMPLIFIER_KEY, PersistentDataType.INTEGER, effect.getAmplifier());
        container.set(EffectManager.PARTICLES_KEY, PersistentDataType.BOOLEAN, effect.isVisible());

        pdc.set(EFFECT_HOLDER_KEY, PersistentDataType.TAG_CONTAINER, container);

        item.setItemMeta(meta);

        return item;
    }
}
