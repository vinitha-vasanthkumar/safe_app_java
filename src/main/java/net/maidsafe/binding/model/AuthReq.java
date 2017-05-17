package net.maidsafe.binding.model;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

import net.maidsafe.api.model.ContainerPermission;

public class AuthReq extends Structure {

	public AppExchangeInfo app_exchange_info;
	public boolean app_container;
	public FfiContainerPermission.ByReference container_permissions;
	public long containers_len;
	public long containers_cap;

	public AuthReq(AppExchangeInfo appInfo,
			List<ContainerPermission> permissions, boolean createAppContainer) {
		app_exchange_info = appInfo;
		app_container = createAppContainer;
		containers_cap = permissions.size();
		containers_len = permissions.size();
		
		container_permissions = new FfiContainerPermission.ByReference();
		if (permissions == null || permissions.isEmpty()) {
			return;
		}
		FfiContainerPermission[] arr = (FfiContainerPermission[]) container_permissions
				.toArray(permissions.size());

		FfiContainerPermission temp;
		for (int i = 0; i < permissions.size(); i++) {
			temp = new FfiContainerPermission(permissions.get(i));
			arr[i].access = temp.access;
			arr[i].cont_name = temp.cont_name;
			arr[i].access_len = temp.access_len;
			arr[i].access_cap = temp.access_cap;
		}
	}

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("app_exchange_info", "app_container",
				"container_permissions", "containers_len", "containers_cap");
	}

}
