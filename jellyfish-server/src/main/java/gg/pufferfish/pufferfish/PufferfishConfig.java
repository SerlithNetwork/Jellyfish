package gg.pufferfish.pufferfish;

import java.io.File;
import java.net.URI;
import java.util.List;
import gg.pufferfish.pufferfish.flare.FlareCommand;
import gg.pufferfish.pufferfish.flare.FlareSetup;
import gg.pufferfish.pufferfish.sentry.SentryManager;
import gg.pufferfish.pufferfish.simd.SIMDDetection;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.StaticConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import org.bukkit.command.SimpleCommandMap;

@StaticConfig.Header({
    "Pufferfish Configuration",
    "Check out Pufferfish Host for maximum performance server hosting: https://pufferfish.host",
    "Join our Discord for support: https://discord.gg/reZw4vQV9H",
    "Download new builds at https://ci.pufferfish.host/job/Pufferfish"
})
@SuppressWarnings({"unused", "deprecation"})
public class PufferfishConfig extends StaticConfig {

    @Ignore
    public static final ConfigHandler HANDLER = new ConfigHandler();

    @Ignore
    public static PufferfishConfig INSTANCE;

    public PufferfishConfig() {
        super(new File("pufferfish.yml"), HANDLER);
        INSTANCE = this;

        // Attempt to detect vectorization
        try {
            SIMDDetection.isEnabled = SIMDDetection.canEnable(PufferfishLogger.LOGGER);
            SIMDDetection.versionLimited = SIMDDetection.getJavaVersion() < 17 || SIMDDetection.getJavaVersion() > 25;
        } catch (NoClassDefFoundError | Exception e) {
            MinecraftServer.LOGGER.error(e.getMessage());
        }

        if (SIMDDetection.isEnabled) {
            PufferfishLogger.LOGGER.info("SIMD operations detected as functional. Will replace some operations with faster versions.");
        } else if (SIMDDetection.versionLimited) {
            PufferfishLogger.LOGGER.warning("Will not enable SIMD! These optimizations are only safely supported on Java 17-24.");
        } else {
            PufferfishLogger.LOGGER.warning("SIMD operations are available for your server, but are not configured!");
            PufferfishLogger.LOGGER.warning("To enable additional optimizations, add \"--add-modules=jdk.incubator.vector\" to your startup flags, BEFORE the \"-jar\".");
            PufferfishLogger.LOGGER.warning("If you have already added this flag, then SIMD operations are not supported on your JVM or CPU.");
            PufferfishLogger.LOGGER.warning("Debug: Java: " + System.getProperty("java.version") + ", test run: " + SIMDDetection.testRun);
        }

    }

    @Order(1)
    public static class INFO {
        public static String VERSION = "1.0";
    }

    @Order(2)
    @Comment({
        "Whether or not books should be writeable.",
        "Servers that anticipate being a target for duping may want to consider",
        "disabling this option.",
        "This can be overridden per-player with the permission pufferfish.usebooks"
    })
    public static boolean ENABLE_BOOKS = false;

    @Order(3)
    @Comment({
        "Optimizes the suffocation check by selectively skipping",
        "the check in a way that still appears vanilla. This should",
        "be left enabled on most servers, but is provided as a",
        "configuration option if the vanilla deviation is undesirable."
    })
    public static boolean ENABLE_SUFFOCATION_OPTIMIZATION = true;

    @Order(4)
    @Comment({
        "Whether or not asynchronous mob spawning should be enabled.",
        "On servers with many entities, this can improve performance by up to 15%. You must have",
        "paper's per-player-mob-spawns setting set to true for this to work.",
        "One quick note - this does not actually spawn mobs async (that would be very unsafe).",
        "This just offloads some expensive calculations that are required for mob spawning."
    })
    public static boolean ENABLE_ASYNC_MOB_SPAWNING = false;
    @Ignore
    public static boolean _ENABLE_ASYNC_MOB_SPAWNING = false;
    @Ignore
    public static boolean ASYNC_MOB_SPAWNING_INITIALIZED = false;

    @Order(5)
    @Comment("Optimizes projectile settings")
    public static class PROJECTILE {

        @Comment({
            "Controls how many chunks are allowed",
            "to be sync loaded by projectiles in a tick."
        })
        public static int MAX_LOADS_PER_TICK = 10;

        @Comment({
            "Controls how many chunks a projectile",
            "can load in its lifetime before it gets", "automatically removed."
        })
        public static int MAX_LOADS_PER_PROJECTILE = 10;

    }

    @Order(6)
    @Comment({
        "Optimizes entity brains when",
        "they're far away from the player"
    })
    public static class DAB {

        public static boolean ENABLED = true;

        @Comment({
            "This value determines how far away an entity has to be",
            "from the player to start being effected by DEAR."
        })
        public static int START_DISTANCE = 12;
        @Ignore
        public static int START_DISTANCE_SQUARED = 144;

        @Comment({
            "This value defines how often in ticks, the furthest entity",
            "will get their pathfinders and behaviors ticked. 20 = 1s"
        })
        public static int MAX_TICK_FREQ = 20;

        @Comment({
            "This value defines how much distance modifies an entity's",
            "tick frequency. freq = (distanceToPlayer^2) / (2^value)",
            "If you want further away entities to tick less often, use 7.",
            "If you want further away entities to tick more often, try 9."
        })
        public static int ACTIVATION_DIST_MOD = 8;

        @Comment("A list of entities to ignore for activation")
        public static List<String> BLACKLISTED_ENTITIES = List.of();

    }

    @Order(7)
    @Comment("Configures Flare, the built-in profiler")
    public static class FLARE {

        @Comment("Sets the server to use for profiles.")
        public static URI URL = URI.create("https://flare.airplane.gg");

    }

    @Order(8)
    @Comment("Options for connecting to Pufferfish/Airplane's online utilities")
    public static class WEB_SERVICES {

        public static String TOKEN = "";

    }

    @Order(9)
    @Comment("Settings for things that don't belong elsewhere")
    public static class MISC {

        public static boolean DISABLE_METHOD_PROFILER = true;

    }

    @Order(10)
    @Comment({
        "Throttles the AI goal selector in entity inactive ticks.",
        "This can improve performance by a few percent, but has minor gameplay implications."
    })
    public static boolean INACTIVE_GOAL_SELECTOR_THROTTLE = true;

    @Order(11)
    @Comment({
        "If this setting is true, the server will run faster after a lag spike in",
        "an attempt to maintain 20 TPS. This option (defaults to true per",
        "spigot/paper) can cause mobs to move fast after a lag spike."
    })
    public static boolean TPS_CATCHUP = true;

    @Order(12)
    @Comment({
        "Allows end crystals to respawn the ender dragon.",
        "On servers that expect end crystal fights in the end dimension, disabling this",
        "will prevent the server from performing an expensive search to attempt respawning",
        "the ender dragon whenever a player places an end crystal."
    })
    public static boolean ALLOW_END_CRYSTAL_RESPAWN = false;

    @Order(13)
    @Comment({
        "Sentry DSN for improved error logging, leave blank to disable",
        "Obtain from https://sentry.io/"
    })
    public static String SENTRY_DSN = "";

    @Override
    public void load() {
        super.load();

        String sentryEnvironment = System.getenv("SENTRY_DSN");
        if (!SENTRY_DSN.isBlank() || (sentryEnvironment != null && !sentryEnvironment.isBlank())) {
            SentryManager.init();
        }

        if (!ASYNC_MOB_SPAWNING_INITIALIZED) {
            ASYNC_MOB_SPAWNING_INITIALIZED = true;
            _ENABLE_ASYNC_MOB_SPAWNING = ENABLE_ASYNC_MOB_SPAWNING;
        }

        DAB.START_DISTANCE_SQUARED = DAB.START_DISTANCE * DAB.START_DISTANCE;


        BuiltInRegistries.ENTITY_TYPE.forEach(e -> e.dabEnabled = true);
        DAB.BLACKLISTED_ENTITIES.forEach(name -> EntityType.byString(name).ifPresentOrElse(
            e -> e.dabEnabled = false,
            () -> MinecraftServer.LOGGER.warn("Unknown entity \"{}\"", name)
        ));


        if (!WEB_SERVICES.TOKEN.isBlank()) {
            FlareSetup.init();
            SimpleCommandMap commandMap = MinecraftServer.getServer().server.getCommandMap();
            if (commandMap.getCommand("flare") == null) {
                commandMap.register("flare", "Pufferfish", new FlareCommand());
            }
        }

    }

}
