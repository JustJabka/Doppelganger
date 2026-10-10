package justjabka.doppelganger.effect;

import justjabka.doppelganger.effect.generic.BaseEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class EffectRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Set<BaseEffect> activeEffects = EffectManager.getActiveEffects(player);

            for (BaseEffect effect : activeEffects) {
                effect.tick(player);
            }
        }
    }
}
