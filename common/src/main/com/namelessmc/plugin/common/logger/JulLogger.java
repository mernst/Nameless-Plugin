package com.namelessmc.plugin.common.logger;

import com.namelessmc.plugin.common.ConfigurationHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class JulLogger extends AbstractLogger {

	private final Logger logger;

	public JulLogger(final ConfigurationHandler config,
					 final Logger logger) {
		super(config);
		this.logger = logger;
	}

	@Override
	public void info(String string) {
		this.logger.info(string);
	}

	@Override
	public void warning(String string) {
		this.logger.warning(string);
	}

	@Override
	public void severe(String string) {
		this.logger.severe(string);
	}

	@Override
	public void info(Supplier<String> stringSupplier) {
		this.logger.info(stringSupplier);
	}

	@Override
	public void warning(Supplier<String> stringSupplier) {
		this.logger.warning(stringSupplier);
	}

	@Override
	public void severe(Supplier<String> stringSupplier) {
		this.logger.severe(stringSupplier);
	}

}
