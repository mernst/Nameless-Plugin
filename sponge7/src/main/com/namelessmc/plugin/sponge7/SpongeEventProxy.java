package com.namelessmc.plugin.sponge7;

import com.namelessmc.plugin.common.audiences.NamelessPlayer;
import com.namelessmc.plugin.common.NamelessPlugin;
import com.namelessmc.plugin.common.event.NamelessJoinEvent;
import com.namelessmc.plugin.common.event.NamelessPlayerQuitEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class SpongeEventProxy {

	private final NamelessPlugin plugin;

	SpongeEventProxy(final NamelessPlugin plugin) {
		this.plugin = plugin;
	}

	public void onJoin(ClientConnectionEvent.Join event) {
		final NamelessPlayer player = plugin.audiences().player(event.getTargetEntity().getUniqueId());
		if (player == null) {
			this.plugin.logger().severe("Skipped join event for player " + event.getTargetEntity().getName() +
					", Audience is null");
			return;
		}
		final NamelessJoinEvent event2 = new NamelessJoinEvent(player);
		this.plugin.events().post(event2);
	}

	public void onQuit(ClientConnectionEvent.Disconnect event) {
		final NamelessPlayerQuitEvent event2 = new NamelessPlayerQuitEvent(event.getTargetEntity().getUniqueId());
		this.plugin.events().post(event2);
	}

}
