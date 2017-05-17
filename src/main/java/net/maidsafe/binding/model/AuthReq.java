package net.maidsafe.binding.model;

import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class AuthReq extends Structure {

	public AppExchangeInfo app_exchange_info;
	public boolean app_container;
	public ContainerPermissions[] container_permissions = new ContainerPermissions[1];
	public long containers_len;
	public long containers_cap;

	public AuthReq(AppExchangeInfo appInfo,
			List<ContainerPermissions> permissions, boolean createAppContainer) {
		app_exchange_info = appInfo;
		app_container = createAppContainer;
		containers_cap = permissions.size();
		containers_len = permissions.size();
		container_permissions = new ContainerPermissions[permissions.size() == 0 ? 1
				: permissions.size()];
		for (int i = 0; i < permissions.size(); i++) {
			container_permissions[i] = permissions.get(i);
		}
		System.out.println("AR perm len :" + containers_len);
		allocateMemory();
		write();
	}

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("app_exchange_info", "app_container",
				"container_permissions", "containers_len", "containers_cap");
	}

}
