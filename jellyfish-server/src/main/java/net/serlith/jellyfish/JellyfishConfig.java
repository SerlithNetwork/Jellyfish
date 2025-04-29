package net.serlith.jellyfish;

import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.StaticConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

        @Comment({
            "Builds chunk packet and sends chunks off the main thread",
            "Not mandatory to use, even at high player-counts, but can be useful for huge lobbies"
        })
        public static class CHUNK_SENDING {
            public static boolean ENABLED = true;

            @Comment("Threads dedicated to send chunks")
            public static int THREADS = 1;

            @Comment("Amount of chunk sending tasks that can be scheduled, if 0 will default to threads * 512")
            public static int QUEUE_SIZE = 0;

            @Comment("Time in seconds to wait for a task to end")
            public static int KEEPALIVE = 8;

        }

    }

    @Comment("Ticking-related behaviors for the server")
    public static class TICKING {

        @Comment("Entity ticking")
        public static class ENTITIES {

            @Comment("Entities allowed to tick")
            public static List<String> ALLOW_TICKING = List.of(
                "minecraft:arrow",
                "minecraft:spectral_arrow",
                "minecraft:trident",
                "minecraft:wind_charge",
                "minecraft:wither_skull",
                "minecraft:fishing_bobber",
                "minecraft:fireball",
                "minecraft:small_fireball",
                "minecraft:dragon_fireball",
                "minecraft:snowball",
                "minecraft:egg",
                "minecraft:leash_knot",
                "minecraft:lightning_bolt",
                "minecraft:splash_potion",
                "minecraft:lingering_potion"
            );

        }

        @Comment("Tile Entity ticking")
        public static class TILE_ENTITIES {

            @Comment("FIXME: Tile entities allowed to tick")
            public static List<String> ALLOW_TICKING = List.of(
                "minecraft:sign",
                "minecraft:hanging_sign"
            );

        }

        @Comment("Block ticking")
        public static class BLOCKS {

            @Comment("Farmland will stay moisten and will not dry out")
            public static boolean KEEP_FARMLAND_MOISTEN = true;

            @Comment("Sugar cane can be placed anywhere, on top of any block")
            public static boolean PREVENT_SUGAR_CANE_BREAK = true;

            @Comment("Leaves will not decay")
            public static boolean PREVENT_LEAVES_DECAY = true;

            @Comment("Grass/Mycelium will not tick at all")
            public static boolean PREVENT_GRASS_TICKS = true;

            @Comment("Snow layers will never melt")
            public static boolean PREVENT_SNOW_MELT = true;

            @Comment("Ice will never melt")
            public static boolean PREVENT_ICE_MELT = true;

            @Comment("Water/Lava will not spread")
            public static boolean PREVENT_LIQUID_SPREAD = false;

            @Comment("Hoppers will not push/pull items from/to the world")
            public static boolean PREVENT_HOPPER_TICKS = true;

        }

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

    @Comment("Player-related configurations, toggling some off will not trigger their events")
    public static class PLAYERS {

        @Comment("Toggle players getting damaged by any source")
        public static boolean TAKE_DAMAGE = false;

        @Comment("Toggle players hungry")
        public static boolean GET_HUNGRY = false;

        @Comment("FIXME: Toggle players losing oxygen bubbles underwater")
        public static boolean DROWN = false;

        @Comment("Toggle players getting armor points (doesn't work visually)")
        public static boolean GET_ARMOR = false;

        @Comment("Toggle players getting experience and leveling up")
        public static boolean GAIN_EXPERIENCE = false;

        @Comment("Toggle advancement criterion triggers")
        public static boolean TRIGGER_ADVANCEMENTS = false;

        @Comment("FIXME: Toggle players being pushed by fluids, might flag anticheats (why ac in a lobby?)")
        public static boolean BE_DRAGGED_BY_FLUIDS = true;

    }

    @Comment("FIXME?: Prevents sending particle packets, but are still visible client-side?")
    public static class PARTICLES {

        @Comment("Toggle block particles when falling from a great distance")
        public static boolean FALL_IMPACT = false;

        @Comment("Toggle damage particles when attacking something")
        public static boolean DEAL_DAMAGE = false;

        @Comment("Toggle splash particles when falling into water")
        public static boolean WATER_SPLASH = false;

        @Comment("Toggle bubble column particles")
        public static boolean WATER_BUBBLES = false;

        @Comment("Toggle honey dripping particles from hives")
        public static boolean DRIPPING_HONEY = false;

        @Comment("FIXME: Toggle water dripping particles from blocks")
        public static boolean DRIPPING_WATER = false;

        @Comment("FIXME: Toggle lava dripping particles from blocks")
        public static boolean DRIPPING_LAVA = false;

        @Comment("Toggle water/lava particles dripping from dripstone")
        public static boolean DRIPSTONE_DRIPPING = false;

        @Comment("Toggle falling leaves particles")
        public static boolean FALLING_LEAVES = true;

        @Comment("Toggle water dripping particles from sponges")
        public static boolean WET_SPONGE = false;

        @Comment("Toggle water dripping particles from leaves")
        public static boolean WET_LEAVES = false;

        @Comment("Toggle underwater ambient particles")
        public static boolean UNDERWATER = false;

    }

    @Comment("Take control of some of the events in your server")
    public static class EVENTS {

        @Comment("Events to disable, this will change/break plugin behavior")
        public static class DISABLE {

            @Comment("Will not trigger neither the event or the pre-post calculations to make it work")
            public static boolean PLAYER_MOVE_EVENT = false;

            @Comment("Will not trigger our own PlayerBelowWorldEvent")
            public static boolean PLAYER_BELOW_WORLD_EVENT = false;

        }

        @Comment("Customize some existing events")
        public static class CUSTOMIZATIONS {

            @Comment("Change some deltas to make it less/more likely to trigger PlayerMoveEvent")
            public static class PLAYER_MOVE_EVENT {

                @Comment("How much the player have to move before triggering the event")
                public static float MOVE_DELTA = 1f / 256;

                @Comment("How much the player have to rotate their camera before triggering the event")
                public static float ANGLE_DELTA = 10f;

            }

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

        List<EntityType<?>> blacklisted = List.of(EntityType.PLAYER, EntityType.FIREWORK_ROCKET, EntityType.ITEM);
        BuiltInRegistries.ENTITY_TYPE.forEach(e -> {
            if (!blacklisted.contains(e)) e.noTick = true;
        });
        TICKING.ENTITIES.ALLOW_TICKING.forEach(s -> EntityType.byString(s).ifPresentOrElse(
            e -> e.noTick = false,
            () -> MinecraftServer.LOGGER.warn("Unknown entity \"{}\" will not tick", s))
        );

        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(e -> e.noTick = true);
        TICKING.TILE_ENTITIES.ALLOW_TICKING.forEach(s -> BlockEntityType.byString(s).ifPresentOrElse(
            e -> e.noTick = false,
            () -> MinecraftServer.LOGGER.warn("Unknown tile entity \"{}\" will not tick", s))
        );

        initialized = true;
    }

}
