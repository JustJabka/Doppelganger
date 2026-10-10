package justjabka.doppelganger.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
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
import justjabka.doppelganger.effect.EffectManager;
import justjabka.doppelganger.effect.generic.BaseEffect;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class DEffect {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("deffect")
                .requires(ctx -> ctx.getSender().hasPermission(
                        "%s.command.deffect".formatted(Doppelganger.NAMESPACE))
                )
                .then(Commands.literal("give")
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .then(Commands.argument("effect", new EffectArgument())
                                        .then(Commands.argument("duration", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                                .then(Commands.argument("amplifier", IntegerArgumentType.integer(0, 255))
                                                        .then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                                                .executes(DEffect::applyEffect)
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("clear")
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(DEffect::clearEffects)
                                .then(Commands.argument("effect", new EffectArgument())
                                        .executes(DEffect::clearEffect)
                                )
                        )
                )
                .build();
    }

    private static int applyEffect(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final Player target = getPlayer(ctx);

        final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

        final int duration = IntegerArgumentType.getInteger(ctx, "duration");
        final int amplifier = IntegerArgumentType.getInteger(ctx, "amplifier");
        final boolean particles = !BoolArgumentType.getBool(ctx, "hideParticles");

        effect.start(target, duration, amplifier, particles);

        ctx.getSource().getSender().sendMessage(
                Component.translatable("commands.effect.give.success.single").arguments(
                        Component.translatable(effect.translationKey()),
                        target.displayName()
                )
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int clearEffect(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final Player target = getPlayer(ctx);

        final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

        effect.end(target);

        ctx.getSource().getSender().sendMessage(
                Component.translatable("commands.effect.clear.specific.success.single").arguments(
                        Component.translatable(effect.translationKey()),
                        target.displayName()
                )
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int clearEffects(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final Player target = getPlayer(ctx);

        EffectManager.clearAllEffects(target);

        ctx.getSource().getSender().sendMessage(
                Component.translatable("commands.effect.clear.everything.success.single").arguments(
                        target.displayName()
                )
        );

        return Command.SINGLE_SUCCESS;
    }

    private static @NonNull Player getPlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final PlayerSelectorArgumentResolver resolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
        return resolver.resolve(ctx.getSource()).getFirst();
    }
}
