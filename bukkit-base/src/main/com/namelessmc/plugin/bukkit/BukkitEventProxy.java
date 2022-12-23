package com.namelessmc.plugin.bukkit;

import com.namelessmc.plugin.common.NamelessPlugin;
import com.namelessmc.plugin.common.audiences.NamelessPlayer;
import com.namelessmc.plugin.common.event.NamelessJoinEvent;
import com.namelessmc.plugin.common.event.NamelessPlayerBanEvent;
import com.namelessmc.plugin.common.event.NamelessPlayerQuitEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class BukkitEventProxy implements Listener {

	private final NamelessPlugin plugin;

	BukkitEventProxy(final NamelessPlugin plugin) {
		this.plugin = plugin;
	}


	public void onJoin(final PlayerJoinEvent event) {
		final Player bukkitPlayer = event.getPlayer();
		final NamelessPlayer player = this.plugin.audiences().player(bukkitPlayer.getUniqueId());
		if (player == null) {
			this.plugin.logger().severe("Skipped join event for " + bukkitPlayer.getName() + ", audience is null");
			return;
		}
		this.plugin.events().post(new NamelessJoinEvent(player));
	}

	public void onQuit(final PlayerQuitEvent event) {
		final UUID uuid = event.getPlayer().getUniqueId();
		this.plugin.events().post(new NamelessPlayerQuitEvent(uuid));
	}

	public void onBan(final PlayerKickEvent event) {
		if (event.getPlayer().isBanned()) {
			this.plugin.events().post(new NamelessPlayerBanEvent(event.getPlayer().getUniqueId()));
		}
	}

}
