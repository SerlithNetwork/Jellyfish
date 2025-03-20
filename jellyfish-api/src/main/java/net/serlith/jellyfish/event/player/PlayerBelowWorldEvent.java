package net.serlith.jellyfish.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered when the player enters the void <br>
 * Similar behavior can be achieved using EntityDamageEvent and filter by player and void OUT_OF_WORLD cause
 */
public class PlayerBelowWorldEvent extends PlayerEvent {
    private static final HandlerList handler = new HandlerList();

    public PlayerBelowWorldEvent(final @NotNull Player who) {
        super(who);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handler;
    }

}
