package net.serlith.jellyfish;

import com.google.common.base.Throwables;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.serlith.jellyfish.command.JellyfishCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class JellyfishConfig {

    private static final List<String> HEADER = List.of(
        "Main configuration file for Jellyfish.",
        "There are a couple of useful configurations for a Network Lobby.",
        "Some optional configurations can re-enable some features for compatibility."
    );

    private static File CONFIG_FILE;
    public static YamlConfiguration config;

    public static int version;
    static boolean verbose;

    private static Map<String, Command> commands;

    @SuppressWarnings("deprecation")
    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();

        try {
            config.load(CONFIG_FILE);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load jellyfish.yml, please correct your syntax errors", e);
            throw Throwables.propagate(e);
        }

        config.options().setHeader(HEADER);
        config.options().copyDefaults(true);
        verbose = config.getBoolean("verbose", false);

        commands = new HashMap<>();
        commands.put("jellyfish", new JellyfishCommand("jellyfish"));

        version = config.getInt("config-version", 1);
        set("config-version", 1);

        readConfig(JellyfishConfig.class, null);

        Block.BLOCK_STATE_REGISTRY.forEach(BlockBehaviour.BlockStateBase::initCache);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry: commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Jellyfish", entry.getValue());
        }
    }

    @SuppressWarnings("deprecation")
    protected static void readConfig(Class<?> clazz, Object inst) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(inst);
                    } catch (InvocationTargetException e) {
                        throw Throwables.propagate(e.getCause());
                    } catch (Exception e) {
                        Bukkit.getLogger().log(Level.SEVERE, "Could not invoke method " + method.getName(), e);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save jellyfish config", e);
        }
    }

    private static void set(String path, Object obj) {
        config.addDefault(path, obj);
        config.set(path, obj);
    }

}
