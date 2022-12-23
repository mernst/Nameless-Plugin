package com.namelessmc.plugin.common;

import com.namelessmc.plugin.common.audiences.NamelessPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

public class LuckPermsPermissions extends AbstractPermissions {

	private LuckPerms api;

	@Override
	public void unload() {
		this.api = null;
	}

	@Override
	public void load() {
		try {
			this.api = LuckPermsProvider.get();
		} catch (IllegalStateException | NoClassDefFoundError e) {}
	}

	@Override
	public boolean isUsable() {
		return this.api != null;
	}

	@Override
	public Set<String> getGroups() {
		if (this.api == null) {
			throw new ProviderNotUsableException();
		}
		return this.api.getGroupManager().getLoadedGroups().stream()
				.map(Group::getName)
				.collect(Collectors.toUnmodifiableSet());
	}

	@Override
	public Set<String> getPlayerGroups(NamelessPlayer player) {
		if (this.api == null) {
			throw new ProviderNotUsableException();
		}
		final User user = this.api.getUserManager().getUser(player.uuid());
		if (user == null) {
			return null;
		}
		return user.getNodes().stream()
				.filter(NodeType.INHERITANCE::matches)
				.map(NodeType.INHERITANCE::cast)
				.map(InheritanceNode::getGroupName)
				.collect(Collectors.toSet());
	}
}
