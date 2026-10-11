package justjabka.doppelganger.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.doppelganger.Doppelganger;
import justjabka.doppelganger.DoppelgangerItems;
import justjabka.doppelganger.command.argument.EffectArgument;
import justjabka.doppelganger.command.argument.EffectHolderArgument;
import justjabka.doppelganger.common.EffectHolder;
import justjabka.doppelganger.effect.Effect;
import justjabka.doppelganger.effect.EffectInstance;
import justjabka.doppelganger.effect.EffectManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class DGive {
    private static final Dynamic2CommandExceptionType TOO_MANY_ITEMS = new Dynamic2CommandExceptionType(
            (maxCount, item) -> MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.give.failed.toomanyitems").arguments(
                            Component.text(maxCount.toString()),
                            Component.text(item.toString())
                    )
            )
    );

    private static final TranslatableComponent RESPONSE_GIVE_SINGLE = Component.translatable("commands.give.success.single");
    private static final TranslatableComponent RESPONSE_GIVE_MULTIPLE = Component.translatable("commands.give.success.multiple");

    public static final int MAX_ALLOWED_ITEMSTACKS = 100;

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("dgive")
                .requires(ctx -> ctx.getSender().hasPermission("%s.command.dgive".formatted(Doppelganger.NAMESPACE)))
                .then(Commands.argument("targets", ArgumentTypes.players())
                        .then(Commands.literal("effect_holder")
                                .then(Commands.argument("effect", new EffectArgument())
                                        .then(Commands.argument("duration", IntegerArgumentType.integer(1, EffectManager.MAX_DURATION))
                                                .then(Commands.argument("amplifier", IntegerArgumentType.integer(0, EffectManager.MAX_AMPLIFIER))
                                                        .then(Commands.argument("holder", new EffectHolderArgument())
                                                                .executes(ctx -> {
                                                                    final Effect effect = ctx.getArgument("effect", Effect.class);
                                                                    final EffectHolder holder = ctx.getArgument("holder", EffectHolder.class);

                                                                    final int duration = IntegerArgumentType.getInteger(ctx, "duration");
                                                                    final int amplifier = IntegerArgumentType.getInteger(ctx, "amplifier");
                                                                    EffectInstance instance = new EffectInstance(effect, duration, amplifier, true);

                                                                    ItemStack item = DoppelgangerItems.getEffectHolder(instance, holder);

                                                                    return giveItem(ctx.getSource(), item, getPlayers(ctx), 1);
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                ).build();
    }

    private static @NonNull List<Player> getPlayers(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final PlayerSelectorArgumentResolver resolver = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class);
        return resolver.resolve(ctx.getSource());
    }

    private static int giveItem(final CommandSourceStack source, final ItemStack itemStack, final List<Player> players, final int count) throws CommandSyntaxException {
        int successCount = 0;

        int maxStackSize = itemStack.getMaxStackSize();
        int maxAllowedCount = maxStackSize * 100;
        if (count > maxAllowedCount) {
            throw TOO_MANY_ITEMS.create(maxAllowedCount, itemStack.displayName());
        }

        for (Player player : players) {
            int remaining = count;

            while (remaining > 0) {
                int size = Math.min(maxStackSize, remaining);
                remaining -= size;
                ItemStack stackToGive = itemStack.clone();
                player.give(stackToGive);
            }

            successCount++;
        }

        return successCount;
    }
}
