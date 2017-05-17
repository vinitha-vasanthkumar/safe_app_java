package net.maidsafe.binding.model;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class ContainerRequest extends Structure {
	
	public AppExchangeInfo app_exchange_info;
	public ContainerPermissions.ByReference container_permissions;
	public long containers_len;
	public long containers_cap;
	
	public ContainerRequest(AppExchangeInfo appInfo, List<ContainerPermissions> permissions) {
		this.app_exchange_info = appInfo;
		containers_cap = containers_len = permissions.size();
		
		container_permissions = new ContainerPermissions.ByReference();
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
		return Arrays.asList("app_exchange_info", 
				"container_permissions", "containers_len", 
				"containers_cap");
	}
}
