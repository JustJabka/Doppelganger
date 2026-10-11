package justjabka.doppelganger.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.command.argument.EffectArgument;
import justjabka.doppelganger.effect.Effect;
import justjabka.doppelganger.effect.EffectInstance;
import justjabka.doppelganger.effect.EffectManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class DEffect {
    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.effect.give.failed")
            )
    );
    private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.effect.clear.everything.failed")
            )
    );
    private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.effect.clear.specific.failed")
            )
    );
    public static final TranslatableComponent RESPONSE_GIVE_SINGLE = Component.translatable("commands.effect.give.success.single");
    public static final TranslatableComponent RESPONSE_GIVE_MULTIPLE = Component.translatable("commands.effect.give.success.multiple");

    public static final TranslatableComponent RESPONSE_CLEAR_EVERYTHING_SINGLE = Component.translatable("commands.effect.clear.everything.success.single");
    public static final TranslatableComponent RESPONSE_CLEAR_EVERYTHING_MULTIPLE = Component.translatable("commands.effect.clear.everything.success.multiple");

    public static final TranslatableComponent RESPONSE_CLEAR_SPECIFIC_SINGLE = Component.translatable("commands.effect.clear.specific.success.single");
    public static final TranslatableComponent RESPONSE_CLEAR_SPECIFIC_MULTIPLE = Component.translatable("commands.effect.clear.specific.success.multiple");

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("deffect")
                .requires(ctx -> ctx.getSender().hasPermission("%s.command.deffect".formatted(Doppelganger.NAMESPACE)))
                .then(Commands.literal("clear")
                        .executes(ctx ->
                                clearEffects(ctx.getSource(), ImmutableList.of(ctx.getSource().getEntityOrThrow()))
                        )
                        .then(Commands.argument("targets", ArgumentTypes.entities())
                                .executes(ctx ->
                                        clearEffects(ctx.getSource(), getEntities(ctx))
                                )
                                .then(Commands.argument("effect", new EffectArgument())
                                        .executes(ctx ->
                                                clearEffect(ctx.getSource(), getEntities(ctx), getEffect(ctx))
                                        )
                                )
                        )
                )
                .then(Commands.literal("give")
                        .then(Commands.argument("targets", ArgumentTypes.entities())
                                .then(Commands.argument("effect", new EffectArgument())
                                        .executes(
                                                ctx -> giveEffect(
                                                        ctx.getSource(),
                                                        getEntities(ctx),
                                                        getEffect(ctx),
                                                        EffectManager.DEFAULT_DURATION,
                                                        EffectManager.DEFAULT_AMPLIFIER,
                                                        EffectManager.DEFAULT_VISIBILITY
                                                )
                                        )
                                        .then(Commands.argument("duration", IntegerArgumentType.integer(1, EffectManager.MAX_DURATION))
                                                .executes(
                                                        ctx -> giveEffect(
                                                                ctx.getSource(),
                                                                getEntities(ctx),
                                                                getEffect(ctx),
                                                                IntegerArgumentType.getInteger(ctx, "duration"),
                                                                EffectManager.DEFAULT_AMPLIFIER,
                                                                EffectManager.DEFAULT_VISIBILITY
                                                        )
                                                )
                                                .then(Commands.argument("amplifier", IntegerArgumentType.integer(0, EffectManager.MAX_AMPLIFIER))
                                                        .executes(
                                                                ctx -> giveEffect(
                                                                        ctx.getSource(),
                                                                        getEntities(ctx),
                                                                        getEffect(ctx),
                                                                        IntegerArgumentType.getInteger(ctx, "duration"),
                                                                        IntegerArgumentType.getInteger(ctx, "amplifier"),
                                                                        EffectManager.DEFAULT_VISIBILITY
                                                                )
                                                        )
                                                        .then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                                                .executes(
                                                                        ctx -> giveEffect(
                                                                                ctx.getSource(),
                                                                                getEntities(ctx),
                                                                                getEffect(ctx),
                                                                                IntegerArgumentType.getInteger(ctx, "duration"),
                                                                                IntegerArgumentType.getInteger(ctx, "amplifier"),
                                                                                !BoolArgumentType.getBool(ctx, "hideParticles")
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("infinite")
                                                .executes(
                                                        ctx -> giveEffect(
                                                                ctx.getSource(),
                                                                getEntities(ctx),
                                                                getEffect(ctx),
                                                                EffectManager.INFINITE_DURATION,
                                                                EffectManager.DEFAULT_AMPLIFIER,
                                                                EffectManager.DEFAULT_VISIBILITY
                                                        )
                                                )
                                                .then(Commands.argument("amplifier", IntegerArgumentType.integer(0, EffectManager.MAX_AMPLIFIER))
                                                        .executes(
                                                                ctx -> giveEffect(
                                                                        ctx.getSource(),
                                                                        getEntities(ctx),
                                                                        getEffect(ctx),
                                                                        EffectManager.INFINITE_DURATION,
                                                                        IntegerArgumentType.getInteger(ctx, "amplifier"),
                                                                        EffectManager.DEFAULT_VISIBILITY
                                                                )
                                                        )
                                                        .then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                                                .executes(
                                                                        ctx -> giveEffect(
                                                                                ctx.getSource(),
                                                                                getEntities(ctx),
                                                                                getEffect(ctx),
                                                                                EffectManager.INFINITE_DURATION,
                                                                                IntegerArgumentType.getInteger(ctx, "amplifier"),
                                                                                !BoolArgumentType.getBool(ctx, "hideParticles")
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .build();
    }

    private static @NonNull List<Entity> getEntities(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final EntitySelectorArgumentResolver resolver = ctx.getArgument("targets", EntitySelectorArgumentResolver.class);
        return resolver.resolve(ctx.getSource());
    }

    private static Effect getEffect(CommandContext<CommandSourceStack> ctx) {
        return ctx.getArgument("effect", Effect.class);
    }

    private static void sendResponse(CommandSourceStack source, int successCount, TranslatableComponent single, TranslatableComponent multiple) {
        source.getSender().sendMessage(
                successCount == 1 ? single : multiple
        );
    }

    private static int giveEffect(
            final CommandSourceStack source,
            final List<? extends Entity> entities,
            final Effect effect,
            final int duration,
            final int amplifier,
            final boolean particles
    ) throws CommandSyntaxException {
        int successCount = 0;

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            EffectInstance instance = new EffectInstance(effect, duration, amplifier, particles);

            if (!EffectManager.applyEffect(livingEntity, instance)) continue;
            successCount++;
        }

        if (successCount == 0) {
            throw ERROR_GIVE_FAILED.create();
        }

        sendResponse(source, successCount, RESPONSE_GIVE_SINGLE.arguments(
                effect.getDisplayName(),
                entities.getFirst().name()
        ), RESPONSE_GIVE_MULTIPLE.arguments(
                effect.getDisplayName(),
                Component.text(successCount)
        ));

        return successCount;
    }

    private static int clearEffects(final CommandSourceStack source, final List<? extends Entity> entities) throws CommandSyntaxException {
        int successCount = 0;

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;

            if (!EffectManager.clearAllEffects(livingEntity)) continue;
            successCount++;
        }

        if (successCount == 0) {
            throw ERROR_CLEAR_EVERYTHING_FAILED.create();
        }

        sendResponse(source, successCount, RESPONSE_CLEAR_EVERYTHING_SINGLE.arguments(
                entities.getFirst().name()
        ), RESPONSE_CLEAR_EVERYTHING_MULTIPLE.arguments(
                Component.text(successCount)
        ));

        return successCount;
    }

    private static int clearEffect(final CommandSourceStack source, final List<? extends Entity> entities, final Effect effect) throws CommandSyntaxException {
        int successCount = 0;

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;

            if (!EffectManager.clearEffect(livingEntity, effect)) continue;
            successCount++;
        }

        if (successCount == 0) {
            throw ERROR_CLEAR_SPECIFIC_FAILED.create();
        }

        sendResponse(source, successCount, RESPONSE_CLEAR_SPECIFIC_SINGLE.arguments(
                effect.getDisplayName(),
                entities.getFirst().name()
        ), RESPONSE_CLEAR_SPECIFIC_MULTIPLE.arguments(
                effect.getDisplayName(),
                Component.text(successCount)
        ));

        return successCount;
    }
}
