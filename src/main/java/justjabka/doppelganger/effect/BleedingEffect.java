package justjabka.doppelganger.effect;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.effect.generic.BaseEffect;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
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
    protected void onStart(LivingEntity entity) {}

    @Override
    protected void onTick(LivingEntity entity) {
        if (entity.getServer().getCurrentTick() % 20 != 0) return;
        if (entity.getHealth() <= 1) return;

        final double damage = 2 * (1 + getAmplifier(entity));

        entity.damage(damage, DamageSource.builder(DamageType.GENERIC)
                .withDirectEntity(entity)
                .withCausingEntity(entity)
                .build()
        );
    }

    @Override
    protected void onEnd(LivingEntity entity) {}
}
