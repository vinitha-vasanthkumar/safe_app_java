package net.maidsafe.api.model;

import java.util.List;

public class ContainerPermission {

	private String containerName;
	private List<Permission> permissions;
	
	public ContainerPermission(String containerName, List<Permission> permissions) {
		this.containerName = containerName;
		this.permissions = permissions;
	}
	
	public String getContainerName() {
		return containerName;
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}
	
	public void addPermission(Permission permission) {
		this.permissions.add(permission);
	}
	
	public void removePermission(Permission permission) {
		this.permissions.remove(permission);
	}
	
}
