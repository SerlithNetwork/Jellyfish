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

        @Comment({
            "Should players be able to fly when they join",
            "Be sure to toggle off allow-flight in server.properties as well"
        })
        public static boolean FLY_ON_JOIN = true;

    }

    @Comment("Async features to save some ticks on the main thread")
    public static class ASYNC {

        public static class CHUNK_SENDING {
            public static boolean ENABLED = true;

            public static int THREADS = 1;

            public static int QUEUE_SIZE = 0;

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

    @Comment("Dimension-related configurations")
    public static class DIMENSIONS {

        @Comment("Overworld dimension configurations")
        public static class OVERWORLD {

            @Comment({
                "Keeping it at 0 helps pairing 1.8 with modern versions",
                "Min height, players below this limit will be considered \"in the void\""
            })
            public static int MIN_HEIGHT = -64;

            @Comment("Max world height, keep it below 384")
            public static int MAX_HEIGHT = 256;

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

            @Comment("Min height, players below this limit will be considered \"in the void\"")
            public static int MIN_HEIGHT = 0;

            @Comment("Max world height, keep it below 256")
            public static int MAX_HEIGHT = 256;

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

            @Comment("Min height, players below this limit will be considered \"in the void\"")
            public static int MIN_HEIGHT = 0;

            @Comment("Max world height, keep it below 256")
            public static int MAX_HEIGHT = 256;

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

            @Comment("Min height, players below this limit will be considered \"in the void\"")
            public static int MIN_HEIGHT = -64;

            @Comment("Max world height, keep it below 384")
            public static int MAX_HEIGHT = 256;

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

    @Comment("Take control of some of the events in your server")
    public static class EVENTS {

        @Comment("Events to disable, this will change/break plugin behavior")
        public static class DISABLE {

            @Comment("Will not trigger neither the event or the pre-post calculations to make it work")
            public static boolean PLAYER_MOVE_EVENT = false;

            public static boolean PLAYER_BELOW_WORLD_EVENT = false;

        }

    }

    @Comment({
        "Download, cache and load Via plugins automatically",
        "Make sure to toggle this off if you have early access builds (https://github.com/sponsors/kennytv)"
    })
    public static class VIA {

        @Comment({
            "Plugin hosting service, in case one goes offline",
            "Possible values: HANGAR, MODRINTH"
        })
        public static Provider PROVIDER = Provider.HANGAR;

        @Ignore
        public enum Provider { HANGAR, MODRINTH };
        @Ignore
        public enum Channel { ALPHA, BETA, SNAPSHOT, RELEASE };

        @Comment("ViaVersion: Allow players to join using newer versions of Minecraft")
        public static class VIA_VERSION {
            public static boolean ENABLED = false;

            @Comment({
                "Recommended value: RELEASE",
                "Possible values: ALPHA, BETA, SNAPSHOT, RELEASE",
                "Modrinth uses BETA while hangar uses SNAPSHOT"
            })
            public static Channel CHANNEL = Channel.RELEASE;

            @Comment("Recommended value: latest")
            public static String VERSION = "latest";

        }

        @Comment({
            "Depends on ViaVersion",
            "ViaBackwards: Allow players to join using older versions of Minecraft"
        })
        public static class VIA_BACKWARDS {
            public static boolean ENABLED = false;

            @Comment({
                "Recommended value: RELEASE",
                "Possible values: ALPHA, BETA, SNAPSHOT, RELEASE",
                "Modrinth uses BETA while hangar uses SNAPSHOT"
            })
            public static Channel CHANNEL = Channel.RELEASE;

            @Comment("Recommended value: latest")
            public static String VERSION = "latest";

        }

        @Comment({
            "Depends on ViaVersion and ViaBackwards",
            "ViaRewind: Allow players to join using now-legacy versions of Minecraft like 1.8 or 1.7"
        })
        public static class VIA_REWIND {
            public static boolean ENABLED = false;

            @Comment({
                "Recommended value: RELEASE",
                "Possible values: ALPHA, BETA, SNAPSHOT, RELEASE",
                "Modrinth uses BETA while hangar uses SNAPSHOT"
            })
            public static Channel CHANNEL = Channel.RELEASE;

            @Comment("Recommended value: latest")
            public static String VERSION = "latest";

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
