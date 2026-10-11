package justjabka.doppelganger.effect.showcase;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.effect.Effect;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BleedingEffect extends Effect {

    @Override
    public NamespacedKey getKey() {
        return Doppelganger.key("bleeding");
    }

    @Override
    public String translationKey() {
        return "effect.test.bleeding";
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public void onStart(LivingEntity entity, int amplifier) {}

    @Override
    public void onTick(LivingEntity entity, int amplifier) {
        if (entity.getServer().getCurrentTick() % 20 != 0) return;
        if (entity.getHealth() <= 1) return;

        final double damage = 2 * (1 + amplifier);

        entity.damage(damage, DamageSource.builder(DamageType.GENERIC)
                .withDirectEntity(entity)
                .withCausingEntity(entity)
                .build()
        );
    }

    @Override
    public void onEnd(LivingEntity entity) {}

    @Override
    public void onUpdated(LivingEntity entity, int amplifier) {}
}
