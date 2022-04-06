package com.namelessmc.plugin.sponge;

import com.google.inject.Inject;
import com.namelessmc.plugin.common.NamelessPlugin;
import com.namelessmc.plugin.common.logger.Slf4jLogger;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.ArrayList;

@Plugin(id="nameless-plugin", name="Nameless Plugin", version="test", description="test")
public class NamelessPluginSponge {

	private final @NotNull NamelessPlugin plugin;
	private final SpongeAudiences adventure;
	private final ArrayList<CommandMapping> registeredCommands = new ArrayList<>();

	@Inject
	public NamelessPluginSponge(final SpongeAudiences adventure,
								final @NotNull @ConfigDir(sharedRoot = false) Path dataDirectory,
								final Logger logger) {
		this.adventure = adventure;
		this.plugin = new NamelessPlugin(
				dataDirectory,
				new SpongeScheduler(this),
				config -> new Slf4jLogger(config, logger)
		);
		this.plugin.registerReloadable(new SpongeCommandProxy(this.plugin, this));
	}

	SpongeAudiences adventure() {
		return this.adventure;
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		this.plugin.reload();
	}

}