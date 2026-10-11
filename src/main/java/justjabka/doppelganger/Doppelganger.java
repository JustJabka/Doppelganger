package justjabka.doppelganger;

import justjabka.doppelganger.attribute.AttributeTestRegistry;
import justjabka.doppelganger.common.impl.AttributeRegistry;
import justjabka.doppelganger.common.impl.EffectRegistry;
import justjabka.doppelganger.effect.EffectListener;
import justjabka.doppelganger.effect.EffectRunnable;
import justjabka.doppelganger.effect.showcase.EffectShowcaseRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Doppelganger extends JavaPlugin {

    private static Doppelganger INSTANCE;

    public static final String NAMESPACE = "doppelganger";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    @NonNull
    public static Doppelganger getInstance() {
        return INSTANCE;
    }

    @NonNull
    public static NamespacedKey key(@NonNull String value) {
        return new NamespacedKey(NAMESPACE, value);
    }

    @Override
    public void onEnable() {
        // TODO: move everything test/showcase to separate project
        EffectShowcaseRegistry.register();
        AttributeTestRegistry.register();

        // Listeners
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new EffectListener(), this);

        // Runnables
        new EffectRunnable().runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void onLoad() {
        INSTANCE = this;

        DoppelgangerRegistries.ATTRIBUTES = new AttributeRegistry();
        DoppelgangerRegistries.EFFECTS = new EffectRegistry();
    }
}
