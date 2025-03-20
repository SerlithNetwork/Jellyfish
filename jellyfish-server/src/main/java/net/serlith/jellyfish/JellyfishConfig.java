package net.serlith.jellyfish;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.serlith.jellyfish.command.JellyfishCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
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

@SuppressWarnings("unused")
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

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List<?> getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    static Map<String, Object> getMap(String path, Map<String, Object> def) {
        if (def != null && config.getConfigurationSection(path) == null) {
            config.addDefault(path, def);
            return def;
        }
        return toMap(config.getConfigurationSection(path));
    }

    private static Map<String, Object> toMap(ConfigurationSection section) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Object obj = section.get(key);
                if (obj != null) {
                    builder.put(key, obj instanceof ConfigurationSection val ? toMap(val) : obj);
                }
            }
        }
        return builder.build();
    }

    public static boolean flyOnJoin = true;
    private static void development() {
        flyOnJoin = getBoolean("development.fly-on-join", flyOnJoin);
    }

    public static int maxProjectileLoadsPerTick = 10;
    public static int maxProjectileLoadsPerProjectile = 10;
    public static boolean enableBooks = false;
    private static void pufferfish() {
        maxProjectileLoadsPerTick = getInt("pufferfish.projectiles.max-loads-per-tick", maxProjectileLoadsPerTick);
        maxProjectileLoadsPerProjectile = getInt("pufferfish.projectiles.max-loads-per-projectile", maxProjectileLoadsPerProjectile);
        enableBooks = getBoolean("pufferfish.books.enable", enableBooks);
    }

    public static boolean globalSpawnProtection = false;
    private static void protection() {
        globalSpawnProtection = getBoolean("protection.global-spawn-protection", globalSpawnProtection);
    }

}
