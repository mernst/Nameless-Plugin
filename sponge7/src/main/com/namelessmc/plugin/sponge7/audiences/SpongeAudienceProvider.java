package com.namelessmc.plugin.sponge7.audiences;

import com.namelessmc.plugin.common.audiences.AbstractAudienceProvider;
import com.namelessmc.plugin.common.audiences.NamelessConsole;
import com.namelessmc.plugin.common.audiences.NamelessPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpongeAudienceProvider extends AbstractAudienceProvider {

	private final SpongeAudiences audiences;
	private final Server server;

	public SpongeAudienceProvider(final SpongeAudiences audiences,
						   final Server server) {
		this.audiences = audiences;
		this.server = server;
	}

	@Override
	public NamelessConsole console() {
		return new SpongeNamelessConsole(this.audiences);
	}

	@Override
	public Audience broadcast() {
		return this.audiences.all();
	}

	private NamelessPlayer spongeToNamelessPlayer(final Optional<Player> optionalPlayer) {
		if (optionalPlayer.isPresent()) {
			final Player player = optionalPlayer.get();
			return new SpongeNamelessPlayer(this.audiences, player);
		}
		return null;
	}

	@Override
	public NamelessPlayer player(UUID uuid) {
		return spongeToNamelessPlayer(this.server.getPlayer(uuid));
	}

	@Override
	public NamelessPlayer playerByUsername(String username) {
		return spongeToNamelessPlayer(this.server.getPlayer(username));
	}

	@Override
	public Collection<NamelessPlayer> onlinePlayers() {
		return this.server.getOnlinePlayers().stream()
				.map(p -> new SpongeNamelessPlayer(this.audiences, p))
				.collect(Collectors.toUnmodifiableList());
	}
}
