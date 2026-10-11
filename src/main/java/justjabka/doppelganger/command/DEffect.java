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
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.command.argument.EffectArgument;
import justjabka.doppelganger.effect.EffectManager;
import justjabka.doppelganger.effect.generic.BaseEffect;
import justjabka.doppelganger.effect.generic.EffectInstance;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

// TODO: add exceptions
public class DEffect {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("deffect")
                .requires(ctx -> ctx.getSender().hasPermission(
                        "%s.command.deffect".formatted(Doppelganger.NAMESPACE))
                )
                .then(Commands.literal("give")
                        .then(Commands.argument("target", ArgumentTypes.entity())
                                .then(Commands.argument("effect", new EffectArgument())
                                        .executes(ctx -> {
                                            final LivingEntity target = getTarget(ctx);
                                            final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

                                            EffectInstance instance = new EffectInstance(effect);
                                            EffectManager.applyEffect(target, instance);

                                            sendGiveSuccessMessage(ctx, effect, target);

                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("duration", IntegerArgumentType.integer(0, EffectManager.MAX_DURATION))
                                                .executes(ctx -> {
                                                    final LivingEntity target = getTarget(ctx);
                                                    final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

                                                    final int duration = IntegerArgumentType.getInteger(ctx, "duration");

                                                    EffectInstance instance = new EffectInstance(effect, duration);
                                                    EffectManager.applyEffect(target, instance);

                                                    sendGiveSuccessMessage(ctx, effect, target);

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                                .then(Commands.argument("amplifier", IntegerArgumentType.integer(0, EffectManager.MAX_AMPLIFIER))
                                                        .executes(ctx -> {
                                                            final LivingEntity target = getTarget(ctx);
                                                            final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

                                                            final int duration = IntegerArgumentType.getInteger(ctx, "duration");
                                                            final int amplifier = IntegerArgumentType.getInteger(ctx, "amplifier");

                                                            EffectInstance instance = new EffectInstance(effect, duration, amplifier);
                                                            EffectManager.applyEffect(target, instance);

                                                            sendGiveSuccessMessage(ctx, effect, target);

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                        .then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                                                .executes(ctx -> {
                                                                    final LivingEntity target = getTarget(ctx);
                                                                    final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

                                                                    final int duration = IntegerArgumentType.getInteger(ctx, "duration");
                                                                    final int amplifier = IntegerArgumentType.getInteger(ctx, "amplifier");
                                                                    final boolean visibility = !BoolArgumentType.getBool(ctx, "hideParticles");

                                                                    EffectInstance instance = new EffectInstance(effect, duration, amplifier, visibility);
                                                                    EffectManager.applyEffect(target, instance);

                                                                    sendGiveSuccessMessage(ctx, effect, target);

                                                                    return Command.SINGLE_SUCCESS;
                                                                })
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("infinite")
                                                .executes(ctx -> {
                                                    final LivingEntity target = getTarget(ctx);
                                                    final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

                                                    EffectInstance instance = new EffectInstance(effect, EffectManager.INFINITE_DURATION);
                                                    EffectManager.applyEffect(target, instance);

                                                    sendGiveSuccessMessage(ctx, effect, target);

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                                .then(Commands.argument("amplifier", IntegerArgumentType.integer(0, EffectManager.MAX_AMPLIFIER))
                                                        .executes(ctx -> {
                                                            final LivingEntity target = getTarget(ctx);
                                                            final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

                                                            final int amplifier = IntegerArgumentType.getInteger(ctx, "amplifier");

                                                            EffectInstance instance = new EffectInstance(effect, EffectManager.INFINITE_DURATION, amplifier);
                                                            EffectManager.applyEffect(target, instance);

                                                            sendGiveSuccessMessage(ctx, effect, target);

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                        .then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                                                .executes(ctx -> {
                                                                    final LivingEntity target = getTarget(ctx);
                                                                    final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

                                                                    final int amplifier = IntegerArgumentType.getInteger(ctx, "amplifier");
                                                                    final boolean visibility = !BoolArgumentType.getBool(ctx, "hideParticles");

                                                                    EffectInstance instance = new EffectInstance(effect, EffectManager.INFINITE_DURATION, amplifier, visibility);
                                                                    EffectManager.applyEffect(target, instance);

                                                                    sendGiveSuccessMessage(ctx, effect, target);

                                                                    return Command.SINGLE_SUCCESS;
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("clear")
                        .executes(ctx -> {
                            final Player target = ctx.getSource().getPlayerOrThrow();

                            EffectManager.clearAllEffects(target);
                            sendClearAllSuccessMessage(ctx, target);

                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(ctx -> {
                                    final LivingEntity target = getTarget(ctx);

                                    EffectManager.clearAllEffects(target);
                                    sendClearAllSuccessMessage(ctx, target);

                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("effect", new EffectArgument())
                                        .executes(ctx -> {
                                            final LivingEntity target = getTarget(ctx);

                                            final BaseEffect effect = ctx.getArgument("effect", BaseEffect.class);

                                            EffectInstance activeEffect = EffectManager.getActiveEffect(target, effect);
                                            if (activeEffect != null) {
                                                activeEffect.end(target);
                                                sendClearSuccessMessage(ctx, effect, target);
                                            }

                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                )
                .build();
    }

    private static @NonNull LivingEntity getTarget(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final EntitySelectorArgumentResolver resolver = ctx.getArgument("target", EntitySelectorArgumentResolver.class);
        return (LivingEntity) resolver.resolve(ctx.getSource()).getFirst();
    }

    private static void sendGiveSuccessMessage(CommandContext<CommandSourceStack> ctx, BaseEffect effect, LivingEntity target) {
        ctx.getSource().getSender().sendMessage(
                Component.translatable("commands.effect.give.success.single").arguments(
                        Component.translatable(effect.translationKey()),
                        target.name()
                )
        );
    }

    private static void sendClearAllSuccessMessage(CommandContext<CommandSourceStack> ctx, LivingEntity target) {
        ctx.getSource().getSender().sendMessage(
                Component.translatable("commands.effect.clear.everything.success.single").arguments(
                        target.name()
                )
        );
    }

    private static void sendClearSuccessMessage(CommandContext<CommandSourceStack> ctx, BaseEffect effect, LivingEntity target) {
        ctx.getSource().getSender().sendMessage(
                Component.translatable("commands.effect.clear.specific.success.single").arguments(
                        Component.translatable(effect.translationKey()),
                        target.name()
                )
        );
    }
}
