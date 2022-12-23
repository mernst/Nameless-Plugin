package com.namelessmc.plugin.common;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.exception.NamelessException;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.plugin.common.event.NamelessJoinEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.event.EventSubscription;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

import static com.namelessmc.plugin.common.LanguageHandler.Term.JOIN_NOTIFICATIONS;

public class JoinNotificationsMessage implements Reloadable {

	private final NamelessPlugin plugin;

	private EventSubscription subscription;

	JoinNotificationsMessage(final NamelessPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void unload() {
		if (this.subscription != null) {
			this.subscription.unsubscribe();
			this.subscription = null;
		}
	}

	@Override
	public void load() {
		if (this.plugin.config().main().node("join-notifications").getBoolean()) {
			this.subscription = this.plugin.events().subscribe(NamelessJoinEvent.class, event ->
					onJoin(event.player().uuid()));
		}
	}

	private void onJoin(final UUID uuid) {
		this.plugin.scheduler().runAsync(() -> {
			final NamelessAPI api = this.plugin.apiProvider().api();
			if (api == null) {
				return;
			}
			try {
				final NamelessUser user = api.userByMinecraftUuid(uuid);
				if (user == null) {
					return;
				}

				int notifications = user.notificationCount();
				if (notifications == 0) {
					return;
				}

				this.plugin.scheduler().runSync(() -> {
					final String notificationsCommand = this.plugin.config().commands()
							.node("get-notifications").getString();
					if (notificationsCommand == null) {
						this.plugin.logger().warning("Notifications command must be enabled for join-notifications feature");
						return;
					}

					final Audience audience = this.plugin.audiences().player(uuid);

					if (audience == null) {
						this.plugin.logger().warning("Player left before we were able to send notifications message");
						return;
					}

					final Component message = this.plugin.language()
							.get(JOIN_NOTIFICATIONS, "notifications_command", notificationsCommand);
					audience.sendMessage(message);
				});

			} catch (final NamelessException e) {
				this.plugin.logger().logException(e);
			}
		});
	}
}
