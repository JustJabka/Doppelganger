package justjabka.doppelganger.effect;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EffectListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerRespawnEvent event) {
        EffectManager.clearAllEffects(event.getPlayer());
    }
}
