package justjabka.doppelganger.effect;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.persistence.PersistentDataContainerView;
import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.DoppelgangerItems;
import justjabka.doppelganger.DoppelgangerRegistries;
import justjabka.doppelganger.common.EffectHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class EffectListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerRespawnEvent event) {
        EffectManager.clearAllEffects(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerClearAllStatusEffectsEvent(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        Consumable consumable = item.getData(DataComponentTypes.CONSUMABLE);
        if (consumable == null) return;

        List<ConsumeEffect> consumeEffects = consumable.consumeEffects();
        if (consumeEffects.isEmpty()) return;

        if (!consumeEffects.contains(ConsumeEffect.clearAllStatusEffects())) return;

        Player player = event.getPlayer();
        EffectManager.clearAllEffects(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDrinkCustomPotion(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        PersistentDataContainerView pdc = item.getItemMeta().getPersistentDataContainer();

        EffectInstance instance = getEffectInstance(EffectHolder.POTION, pdc);
        if (instance == null) return;

        EffectManager.applyEffect(event.getPlayer(), instance);
    }

    @EventHandler
    public void onCustomTippedArrowHitEvent(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;
        if (!(event.getHitEntity() instanceof LivingEntity livingEntity)) return;

        PersistentDataContainerView pdc = arrow.getItemStack().getPersistentDataContainer();

        EffectInstance instance = getEffectInstance(EffectHolder.TIPPED_ARROW, pdc);
        if (instance == null) return;

        EffectManager.applyEffect(livingEntity, instance);
    }

    private static @Nullable EffectInstance getEffectInstance(EffectHolder requiredHolder, PersistentDataContainerView pdc) {
        PersistentDataContainer container = pdc.get(DoppelgangerItems.EFFECT_HOLDER_KEY, PersistentDataType.TAG_CONTAINER);
        if (container == null || container.isEmpty()) return null;

        final EffectHolder effectHolder = getEffectHolder(container);
        if (!requiredHolder.equals(effectHolder)) return null;

        final Effect effect = getEffect(container);
        if (effect == null) return null;

        final int duration = container.getOrDefault(EffectManager.DURATION_KEY, PersistentDataType.INTEGER, EffectManager.DEFAULT_DURATION);
        final int amplifier = container.getOrDefault(EffectManager.AMPLIFIER_KEY, PersistentDataType.INTEGER, EffectManager.DEFAULT_AMPLIFIER);
        final boolean visibility = container.getOrDefault(EffectManager.PARTICLES_KEY, PersistentDataType.BOOLEAN, EffectManager.DEFAULT_VISIBILITY);

        return new EffectInstance(effect, duration, amplifier, visibility);
    }

    private static @Nullable EffectHolder getEffectHolder(PersistentDataContainer container) {
        String holderStr = container.get(DoppelgangerItems.TYPE_KEY, PersistentDataType.STRING);
        if (holderStr == null) return null;

        NamespacedKey holderKey = NamespacedKey.fromString(holderStr);
        if (holderKey == null) return null;

        try {
            return EffectHolder.valueOf(holderKey.value().toUpperCase());
        } catch (IllegalArgumentException _) {
            return null;
        }
    }

    private static @Nullable Effect getEffect(PersistentDataContainer container) {
        String effectStr = container.get(DoppelgangerItems.ID_KEY, PersistentDataType.STRING);
        if (effectStr == null) return null;

        NamespacedKey effectKey = NamespacedKey.fromString(effectStr);
        if (effectKey == null) return null;

        return DoppelgangerRegistries.EFFECTS.get(effectKey);
    }
}
