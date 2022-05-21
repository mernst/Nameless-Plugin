package com.namelessmc.plugin.sponge7;

import com.google.inject.Inject;
import com.namelessmc.plugin.common.MavenConstants;
import com.namelessmc.plugin.common.NamelessPlugin;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import org.bstats.sponge.Metrics;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

@Plugin(id = "namelessmc",
		name = "NamelessMC",
		version = MavenConstants.PROJECT_VERSION,
		description = "Integration with NamelessMC websites")
public class SpongeNamelessPlugin {

	private final @NonNull NamelessPlugin plugin;
	private final Metrics.@NonNull Factory metricsFactory;

	@Inject
	public SpongeNamelessPlugin(final @NonNull SpongeAudiences audiences,
								final @ConfigDir(sharedRoot = false) @NonNull Path dataDirectory,
								final @NonNull Logger logger,
								final Metrics.@NonNull Factory metricsFactory,
								final @NonNull Game game) {
		this.metricsFactory = metricsFactory;
		this.plugin = new NamelessPlugin(
				dataDirectory,
				new SpongeScheduler(this),
				config -> new Slf4jLogger(config, logger),
				Path.of("logs", "latest.log")
		);
		this.plugin.setAudienceProvider(new SpongeAudienceProvider(audiences, game.getServer()));
		this.plugin.registerReloadable(new SpongeDataSender(this.plugin));
		Sponge.getEventManager().registerListeners(this, new SpongeEventProxy(this.plugin));
	}

	@Listener
	public void onServerStart(final GameStartedServerEvent event) {
		this.plugin.reload();

		Metrics metrics = this.metricsFactory.make(14865);
		this.plugin.registerCustomCharts(metrics, Metrics.class);

		SpongeCommandProxy.registerCommands(this.plugin, this);
	}

	@Listener
	public void reload(final GameReloadEvent event) {
		this.plugin.reload();
	}

}