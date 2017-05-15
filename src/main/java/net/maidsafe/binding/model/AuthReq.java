package net.maidsafe.binding.model;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class AuthReq extends Structure {

	public AppExchangeInfo app_exchange_info;
	public boolean app_container;
	public ContainerPermissions[] container_permissions;
	public long containers_len;
	public long containers_cap;

	public AuthReq(AppExchangeInfo appInfo,
                   List<ContainerPermissions> permissions, boolean createAppContainer) {
		app_exchange_info = appInfo;
		app_container = createAppContainer;
		containers_cap = containers_len = permissions.size();
		container_permissions = new ContainerPermissions[(int) (containers_cap == 0 ? 1
				: containers_cap)];
		for (int i = 0; i < containers_cap; i++) {
			container_permissions[i] = permissions.get(i);
		}
		allocateMemory();
	}

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("app_exchange_info", "app_container",
				"container_permissions", "containers_len", "containers_cap");
	}

}
