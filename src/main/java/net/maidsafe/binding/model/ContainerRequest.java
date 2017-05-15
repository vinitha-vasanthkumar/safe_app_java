package net.maidsafe.binding.model;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class ContainerRequest extends Structure {
	
	public AppExchangeInfo app_exchange_info;
	public ContainerPermissions[] conatiner_permissions;
	public long containers_len;
	public long containers_cap;

	@Override
	protected List<String> getFieldOrder() { 
		return Arrays.asList("app_exchange_info", 
				"conatiner_permissions", "containers_len", 
				"containers_cap");
	}
}
