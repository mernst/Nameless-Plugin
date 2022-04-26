package com.namelessmc.plugin.common.event;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class ServerQuitEvent extends AbstractEvent {

	private final @NonNull UUID uuid;

	public ServerQuitEvent(final @NonNull UUID uuid) {
		this.uuid = uuid;
	}

	public @NonNull UUID uuid() {
		return this.uuid;
	}

}
