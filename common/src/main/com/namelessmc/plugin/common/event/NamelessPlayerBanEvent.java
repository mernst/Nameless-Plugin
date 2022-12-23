package com.namelessmc.plugin.common.event;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class NamelessPlayerBanEvent extends NamelessOfflinePlayerEvent {

	public NamelessPlayerBanEvent(final UUID uuid) {
		super(uuid);
	}

}
