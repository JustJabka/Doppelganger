package justjabka.doppelganger.effect.generic;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.Color;
import org.bukkit.Keyed;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseEffect implements Keyed, Translatable {

    public abstract Color getColor();

    public abstract void onStart(LivingEntity entity, int amplifier);
    public abstract void onTick(LivingEntity entity, int amplifier);
    public abstract void onEnd(LivingEntity entity);

    public abstract void onUpdated(LivingEntity entity, int amplifier);
}
