package justjabka.doppelganger;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import justjabka.doppelganger.command.DAttribute;
import justjabka.doppelganger.command.DEffect;

@SuppressWarnings("UnstableApiUsage")
public class DoppelgangerBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(final BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(DEffect.build());
            event.registrar().register(DAttribute.build());
        });
    }
}
