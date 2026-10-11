package justjabka.doppelganger.effect;

import justjabka.doppelganger.effect.generic.BaseEffect;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class EffectRunnable extends BukkitRunnable {

    // TODO: Optimize this shit😭
    @Override
    public void run() {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                Set<BaseEffect> activeEffects = EffectManager.getActiveEffects(entity);

                for (BaseEffect effect : activeEffects) {
                    effect.tick(entity);
                }
            }
        }
    }
}
