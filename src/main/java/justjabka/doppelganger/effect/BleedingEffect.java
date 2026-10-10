package justjabka.doppelganger.effect;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.effect.generic.BaseEffect;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class BleedingEffect extends BaseEffect {

    @Override
    public @NonNull NamespacedKey getKey() {
        return Doppelganger.key("bleeding");
    }

    @Override
    public @NonNull String translationKey() {
        return "effect.test.bleeding";
    }

    @Override
    public @NonNull Color getColor() {
        return Color.RED;
    }

    @Override
    public void onStart(Player player) {}

    @Override
    public void onTick(Player player) {
        if (getDuration(player) % 20 != 0) return;
        if (player.getHealth() <= 1) return;

        player.damage(2, DamageSource.builder(DamageType.GENERIC)
                .withDirectEntity(player)
                .withCausingEntity(player)
                .build()
        );
    }

    @Override
    public void onEnd(Player player) {}
}
