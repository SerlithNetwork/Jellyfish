package net.serlith.jellyfish.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JellyfishCommand extends Command {

    public JellyfishCommand(String name) {
        super(name);
        this.description = "Jellyfish related commands";
        this.usageMessage = "/jellyfish [reload | version]";
        this.setPermission("bukkit.command.jellyfish");
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String commandLabel, final @NotNull String @NotNull [] args) {
        if (!testPermission(sender)) return false;

        if (args.length != 1) {
            sender.sendMessage("§cUsage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                return true;
            case "version":
                return true;
        }

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("reload", "version")
                .filter(i -> i.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
