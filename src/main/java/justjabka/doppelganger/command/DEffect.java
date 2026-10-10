package justjabka.doppelganger.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.command.argument.EffectArgument;
import justjabka.doppelganger.effect.generic.BaseEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public class DEffect {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("deffect")
                .requires(ctx -> ctx.getSender().hasPermission(
                        "%s.command.deffect".formatted(Doppelganger.NAMESPACE))
                )
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("effect", new EffectArgument())
                                .then(Commands.argument("duration", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                        .then(Commands.argument("amplifier", IntegerArgumentType.integer(0, 255))
                                                .executes(DEffect::applyEffect)
                                        )
                                )
                        )
                )
                .build();
    }

    private static int applyEffect(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final PlayerSelectorArgumentResolver resolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
        final Player target = resolver.resolve(ctx.getSource()).getFirst();

        final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

        final int duration = IntegerArgumentType.getInteger(ctx, "duration");
        final int amplifier = IntegerArgumentType.getInteger(ctx, "amplifier");

        effect.start(target, duration, amplifier);

        ctx.getSource().getSender().sendRichMessage("<target> got <effect>",
                Placeholder.component("target", target.displayName()),
                Placeholder.component("effect", Component.text(effect.getKey().asString()))
        );

        return Command.SINGLE_SUCCESS;
    }
}
