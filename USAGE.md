
# ⚙️ Jellyfish Config
TODO

# ⚡ Jellyfish Events
Any of the events listed here are ~~probably~~ exclusive to Jellyfish and besides actually using them,
can also be useful to detect if your plugin is running in a Jellyfish environment similar to detecting [Folia](https://docs.papermc.io/paper/dev/folia-support).
```java
private static boolean isJellyfish() {
    try {
        Class.forName("net.serlith.jellyfish.event.player.PlayerBelowWorldEvent");
        return true;
    } catch (ClassNotFoundException ignored) {
        return false;
    }
}
```

## PlayerBelowWorldEvent
This is a convenience event that will only trigger for players when they're below the world. \
You won't need to listen to EntityDamageEvent and filter for Player entity and Void damage type.

# ⚡ Fork Events
To ensure plugin compatibility with other server software, we'll try to keep an eye if any event we want to implement
already exists in other server software, and we'll implement it here with their same namespaces.

## Purpur: PlayerAFKEvent
Class: `org.purpurmc.purpur.event.PlayerAFKEvent` \
Triggers when the player goes AFK.
