package net.maidsafe.binding.model;

import com.sun.jna.Structure;

import net.maidsafe.api.model.ContainerPermission;

import java.util.Arrays;
import java.util.List;

public class ContainerPermissions extends Structure {

	public String cont_name;
	public int[] access = new int[1];
	public long access_len;
	public long access_cap;

	public ContainerPermissions() {
	}

	public ContainerPermissions(ContainerPermission permission) {
		int[] access;
		cont_name = permission.getContainerName();
		access_cap = access_len = permission.getPermissions().size();
		access = new int[(int) (access_cap == 0 ? 1 : access_cap)];

		for (int i = 0; i < access_len; i++) {
			access[i] = permission.getPermissions().get(i).ordinal();
		}
		allocateMemory();
	}

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("cont_name", "access", "access_len", "access_cap");
	}
}
