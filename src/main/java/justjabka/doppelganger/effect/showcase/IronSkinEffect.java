package justjabka.doppelganger.effect.showcase;

import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.effect.Effect;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class IronSkinEffect extends Effect {

    @Override
    public NamespacedKey getKey() {
        return Doppelganger.key("iron_skin");
    }

    @Override
    public String translationKey() {
        return "effect.test.iron_skin";
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    @Override
    protected void onStart(LivingEntity entity, int amplifier) {
        addModifier(entity, amplifier);
    }

    @Override
    protected void onTick(LivingEntity entity, int amplifier) {}

    @Override
    protected void onEnd(LivingEntity entity) {
        removeModifier(entity);
    }

    @Override
    protected void onUpdated(LivingEntity entity, int amplifier) {
        removeModifier(entity);
        addModifier(entity, amplifier);
    }

    private void removeModifier(LivingEntity entity) {
        AttributeInstance armorInstance = entity.getAttribute(Attribute.ARMOR);
        if (armorInstance == null) return;

        armorInstance.removeModifier(getKey());
    }

    private void addModifier(LivingEntity entity, int amplifier) {
        AttributeInstance armorInstance = entity.getAttribute(Attribute.ARMOR);
        if (armorInstance == null) return;

        double amount = 3 * (1 + amplifier);
        armorInstance.addModifier(new AttributeModifier(
                getKey(),
                amount,
                AttributeModifier.Operation.ADD_NUMBER)
        );
    }
}
