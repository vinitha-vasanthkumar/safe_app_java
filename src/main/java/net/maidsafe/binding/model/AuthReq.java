package net.maidsafe.binding.model;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class AuthReq extends Structure {

	public AppExchangeInfo app_exchange_info;
	public boolean app_container;
	public ContainerPermissions.ByReference container_permissions;
	public long containers_len;
	public long containers_cap;

	public AuthReq(AppExchangeInfo appInfo,
			List<ContainerPermissions> permissions, boolean createAppContainer) {
		app_exchange_info = appInfo;
		app_container = createAppContainer;
		containers_cap = permissions.size();
		containers_len = permissions.size();
		
		container_permissions = new ContainerPermissions.ByReference();
		if (permissions.isEmpty()) {
			return;
		}
		ContainerPermissions[] arr = (ContainerPermissions[]) container_permissions
				.toArray(permissions.size());

		for (int i = 0; i < permissions.size(); i++) {
			arr[i].access = permissions.get(i).access;
			arr[i].cont_name = permissions.get(i).cont_name;
			arr[i].access_len = permissions.get(i).access_len;
			arr[i].access_cap = permissions.get(i).access_cap;
		}
	}

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("app_exchange_info", "app_container",
				"container_permissions", "containers_len", "containers_cap");
	}

}
