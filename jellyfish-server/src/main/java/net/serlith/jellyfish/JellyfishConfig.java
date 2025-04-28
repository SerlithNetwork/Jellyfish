package net.serlith.jellyfish;

import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.StaticConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import java.io.File;
import java.util.List;

@StaticConfig.Header({
    "Main configuration file for Jellyfish.",
    "There are a couple of useful configurations for a Network Lobby.",
    "Some optional configurations can re-enable some features for compatibility."
})
@SuppressWarnings("unused")
public class JellyfishConfig extends StaticConfig {

    @Ignore
    public static ConfigHandler HANDLER = new ConfigHandler();

    @Ignore
    public static JellyfishConfig INSTANCE;

    public JellyfishConfig() {
        super(new File("jellyfish.yml"), HANDLER);
        INSTANCE = this;
    }

    public static class INFO {
        public static String VERSION = "1.0";
    }

    @Comment({
        "** DEVELOPMENT FEATURES **",
        "BE SURE TO DISABLE THEM ONCE IN PRODUCTION"
    })
    public static class DEVELOPMENT {

        @Comment("Should players be able to fly when they join")
        public static boolean FLY_ON_JOIN = true;

    }

    @Comment("Async features to save some ticks on the main thread")
    public static class ASYNC {

        public static class CHUNK_SENDING {
            public static boolean ENABLED = true;

            public static int THREADS = 1;

            public static int QUEUE_SIZE = 0;

            public static int KEEPALIVE = 60;

        }

    }

    @Comment("")
    public static class TICKING {

        public static class ENTITIES {
            public static List<String> NO_TICK = List.of(
                "minecraft:item_display",
                "minecraft:block_display",
                "minecraft:text_display"
                );
        }

        public static class TILE_ENTITIES {}

        public static class BLOCKS {}

    }

    @Comment("Features that prevent players from interacting with the world")
    public static class SECURITY {

        @Comment("The whole world will have spawn protection")
        public static boolean GLOBAL_SPAWN_PROTECTION = true;

    }

    @Comment("Enables caching some results")
    public static class CACHES {

        @Comment("Useful for plugins that decorate with heads (blocks or tablist)")
        public static class PLAYER_PROFILE {
            public static boolean ENABLED = true;

            @Comment("Minutes before expiring the cache")
            public static long TIMEOUT = 360L;

        }

    }

    @Comment("Dimension-related configurations")
    public static class DIMENSIONS {

        @Comment("Overworld dimension configurations")
        public static class OVERWORLD {

            @Comment({
                "Keeping it at 0 helps pairing 1.8 with modern versions",
                "Min height, players below this limit will be considered \"in the void\", has to be a multiple of 16"
            })
            public static int MIN_HEIGHT = -64;

            @Comment("Max world actual height, keep it below 384, has to be a multiple of 16")
            public static int MAX_HEIGHT = 384;

            @Comment("Max world height, keep it below max-height")
            public static int MAX_HEIGHT_LOGICAL = 384;

            @Comment("Default minimum ambient light")
            public static float AMBIENT_LIGHT = 0.1F;

            @Comment("Should be able to place water")
            public static boolean PLACE_WATER = true;

            @Comment("Should piglins wiggle until eventually being zombified")
            public static boolean PIGLIN_SAFE = true;

            @Comment("Should beds attempt to trigger an explosion")
            public static boolean BED_WORKS = true;

            @Comment("Should respawn anchors attempt to trigger an explosion")
            public static boolean RESPAWN_ANCHOR_WORKS = true;

        }

        @Comment("Nether dimension configurations")
        public static class NETHER {

            @Comment("Min height, players below this limit will be considered \"in the void\", has to be a multiple of 16")
            public static int MIN_HEIGHT = 0;

            @Comment("Max world actual height, keep it below 256, has to be a multiple of 16")
            public static int MAX_HEIGHT = 256;

            @Comment("Max world height, keep it below max-height")
            public static int MAX_HEIGHT_LOGICAL = 128;

            @Comment("Default minimum ambient light")
            public static float AMBIENT_LIGHT = 0.1F;

            @Comment("Should be able to place water")
            public static boolean PLACE_WATER = false;

            @Comment("Should piglins wiggle until eventually being zombified")
            public static boolean PIGLIN_SAFE = true;

            @Comment("Should beds attempt to trigger an explosion")
            public static boolean BED_WORKS = true;

            @Comment("Should respawn anchors attempt to trigger an explosion")
            public static boolean RESPAWN_ANCHOR_WORKS = true;

        }

        @Comment("The end dimension configurations")
        public static class THE_END {

            @Comment("Min height, players below this limit will be considered \"in the void\", has to be a multiple of 16")
            public static int MIN_HEIGHT = 0;

            @Comment("Max world actual height, keep it below 256, has to be a multiple of 16")
            public static int MAX_HEIGHT = 256;

            @Comment("Max world height, keep it below max-height")
            public static int MAX_HEIGHT_LOGICAL = 256;

            @Comment("Default minimum ambient light")
            public static float AMBIENT_LIGHT = 0.1F;

            @Comment("Should be able to place water")
            public static boolean PLACE_WATER = true;

            @Comment("Should piglins wiggle until eventually being zombified")
            public static boolean PIGLIN_SAFE = true;

            @Comment("Should beds attempt to trigger an explosion")
            public static boolean BED_WORKS = true;

            @Comment("Should respawn anchors attempt to trigger an explosion")
            public static boolean RESPAWN_ANCHOR_WORKS = true;

        }

        @Comment("Overworld Caves configurations, same as overworld, but with ceiling")
        public static class OVERWORLD_CAVES {

            @Comment("Min height, players below this limit will be considered \"in the void\", has to be a multiple of 16")
            public static int MIN_HEIGHT = -64;

            @Comment("Max world actual height, keep it below 384, has to be a multiple of 16")
            public static int MAX_HEIGHT = 384;

            @Comment("Max world logical height, keep it below max-height")
            public static int MAX_HEIGHT_LOGICAL = 384;

            @Comment("Default minimum ambient light")
            public static float AMBIENT_LIGHT = 0.1F;

            @Comment("Should be able to place water")
            public static boolean PLACE_WATER = true;

            @Comment("Should piglins wiggle until eventually being zombified")
            public static boolean PIGLIN_SAFE = true;

            @Comment("Should beds attempt to trigger an explosion")
            public static boolean BED_WORKS = true;

            @Comment("Should respawn anchors attempt to trigger an explosion")
            public static boolean RESPAWN_ANCHOR_WORKS = true;

        }

    }

    @Comment("")
    public static class PLAYERS {

        public static boolean TAKE_DAMAGE = false;

        public static boolean GET_HUNGRY = false;

    }

    @Comment("Take control of some of the events in your server")
    public static class EVENTS {

        @Comment("Events to disable, this will change/break plugin behavior")
        public static class DISABLE {

            @Comment("Will not trigger neither the event or the pre-post calculations to make it work")
            public static boolean PLAYER_MOVE_EVENT = false;

            public static boolean PLAYER_BELOW_WORLD_EVENT = false;

        }

    }

    @Override
    public void load() {
        super.load();
        if (initialized) init();
    }

    @Ignore
    private static boolean initialized = false;
    public static void init() {

        BuiltInRegistries.ENTITY_TYPE.forEach(e -> {
            e.noTick = false;
        });

        TICKING.ENTITIES.NO_TICK.forEach(s -> {
            EntityType.byString(s).ifPresentOrElse(e -> {
                e.noTick = true;
            }, () -> MinecraftServer.LOGGER.warn("Unknown entity \"{}\" will tick", s));
        });

        initialized = true;
    }

}
