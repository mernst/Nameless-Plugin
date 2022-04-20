package com.namelessmc.plugin.bukkit.hooks.maintenance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KennyMaintenance implements MaintenanceStatusProvider {

	private Method instanceGetMethod;
	private Method isMaintenanceMethod;

	public KennyMaintenance() {
		try {
			Class<?> maintenanceProviderClass = Class.forName("eu.kennytv.maintenance.api.MaintenanceProvider");
			this.instanceGetMethod = maintenanceProviderClass.getMethod("get");
			Class<?> maintenanceInterface = Class.forName("eu.kennytv.maintenance.api.Maintenance");
			this.isMaintenanceMethod = maintenanceInterface.getMethod("isMaintenance");
		} catch (ClassNotFoundException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean maintenanceEnabled() {
		if (instanceGetMethod == null || isMaintenanceMethod == null) {
			// Not initialized properly
			return false;
		}

		try {
			Object instance = instanceGetMethod.invoke(null);
			Object isMaintenance = isMaintenanceMethod.invoke(instance);
			if (isMaintenance instanceof Boolean) {
				return (boolean) isMaintenance;
			} else {
				System.err.println("Unexpected return value from isMaintenance method");
				return false;
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
