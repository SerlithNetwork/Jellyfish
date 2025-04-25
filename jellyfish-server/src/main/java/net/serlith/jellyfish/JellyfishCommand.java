package net.serlith.jellyfish;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JellyfishCommand extends Command {

    private final Component prefix = MiniMessage.miniMessage().deserialize("<white><gradient:#feabff:#94ffeb>Jellyfish</gradient> <#e79eff>⮞</#e79eff> </white>");

    public JellyfishCommand() {
        super("jellyfish");
        this.description = "Jellyfish related commands";
        this.usageMessage = "/jellyfish [reload | version]";
        this.setPermission("bukkit.command.jellyfish");
    }

    public static void init() {
        MinecraftServer.getServer().server.getCommandMap().register("jellyfish", "serlith", new JellyfishCommand());
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length == 1) {
            return Stream.of("reload", "version")
                .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args) {
        if (!testPermission(sender)) return true;

        if (args.length != 1) {
            sender.sendMessage(prefix.append(Component.text("Usage: " + usageMessage, NamedTextColor.WHITE)));
            args = new String[]{"version"};
        }

        if (args[0].equalsIgnoreCase("reload")) {
            MinecraftServer console = MinecraftServer.getServer();
            try {
                JellyfishConfig.INSTANCE.load();
            } catch (Exception e) {
                sender.sendMessage(prefix.append(Component.text("Failed to reload.", NamedTextColor.RED)));
                console.server.getLogger().severe(e.getMessage());
                return true;
            }
            console.server.reloadCount++;

            Command.broadcastCommandMessage(sender, prefix.append(Component.text("Jellyfish configuration has been reloaded.", NamedTextColor.WHITE)));
        } else if (args[0].equalsIgnoreCase("version")) {
            Command.broadcastCommandMessage(sender, prefix.append(Component.text("This server is running " + Bukkit.getName() + " version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ")", NamedTextColor.WHITE)));
        }

        return true;
    }

}
