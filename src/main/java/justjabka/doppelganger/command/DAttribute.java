package justjabka.doppelganger.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import justjabka.doppelganger.Doppelganger;
import org.bukkit.entity.Player;

public class DAttribute {
    public static LiteralCommandNode<CommandSourceStack> build () {
        return Commands.literal("dattribute")
                .requires(ctx -> ctx.getSender().hasPermission(
                        "%s.command.dattribute".formatted(Doppelganger.NAMESPACE))
                )
                .executes(ctx -> {
                    Player player = ctx.getSource().getPlayerOrThrow();
                    player.sendMessage("test");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
