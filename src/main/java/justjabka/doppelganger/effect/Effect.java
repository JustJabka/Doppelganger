package justjabka.doppelganger.effect;

import justjabka.doppelganger.common.Translatable;
import org.bukkit.Color;
import org.bukkit.Keyed;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Effect implements Keyed, Translatable {

    public abstract Color getColor();

    protected abstract void onStart(LivingEntity entity, int amplifier);
    protected abstract void onTick(LivingEntity entity, int amplifier);
    protected abstract void onEnd(LivingEntity entity);

    protected abstract void onUpdated(LivingEntity entity, int amplifier);
}
