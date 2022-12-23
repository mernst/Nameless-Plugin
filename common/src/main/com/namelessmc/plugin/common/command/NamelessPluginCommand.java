package com.namelessmc.plugin.common.command;

import com.namelessmc.plugin.common.NamelessPlugin;
import com.namelessmc.plugin.common.Permission;
import com.namelessmc.plugin.common.audiences.NamelessCommandSender;
import com.namelessmc.plugin.common.audiences.NamelessPlayer;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.namelessmc.plugin.common.LanguageHandler.Term.*;

public class NamelessPluginCommand extends CommonCommand {

	public NamelessPluginCommand(final NamelessPlugin plugin) {
		super(
				plugin,
				"plugin",
				COMMAND_PLUGIN_USAGE,
				COMMAND_PLUGIN_DESCRIPTION,
				Permission.COMMAND_PLUGIN
		);

		if (this.actualName() == null) {
			plugin.logger().warning("The commands config file is missing the plugin command");
		}
	}

	@Override
	public void execute(final NamelessCommandSender sender, final String[] args) {
		if (args.length == 1) {
			switch(args[0]) {
				case "reload":
				case "rl":
					this.plugin().unload();
					this.plugin().load();
					sender.sendMessage(this.language().get(COMMAND_PLUGIN_OUTPUT_RELOAD_SUCCESSFUL));
					return;
				case "last_error":
					final String stackTrace = this.plugin().logger().getLastExceptionStackTrace();
					if (stackTrace == null) {
						sender.sendMessage(Component.text("No error since last plugin reload."));
						return;
					}

					this.plugin().logger().warning(stackTrace);

					if (sender instanceof NamelessPlayer) {
						sender.sendMessage(Component.text("Last error has been printed to the console"));
					}
					return;
			}
		}

		sender.sendMessage(this.usage());
	}

	@Override
	public List<String> complete(NamelessCommandSender sender, String[] args) {
		if (args.length == 1) {
			return Arrays.stream(new String[]{"reload", "rl", "last_api_error"}).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}
