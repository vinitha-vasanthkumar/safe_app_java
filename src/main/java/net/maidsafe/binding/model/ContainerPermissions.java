package net.maidsafe.binding.model;

import com.sun.jna.IntegerType;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import net.maidsafe.api.model.ContainerPermission;

import java.util.Arrays;
import java.util.List;

public class ContainerPermissions extends Structure {

	public String cont_name;
	public Pointer access;
	// public int[] access = new int[1];
	public long access_len;
	public long access_cap;

	public ContainerPermissions() {
	}

	public ContainerPermissions(ContainerPermission permission) {
		int permissionsCount = permission.getPermissions().size();
		cont_name = permission.getContainerName();
		access_len = permissionsCount;
		access_cap = permissionsCount;
		int SIZE = Native.getNativeSize(Integer.class);
		access = new Memory(SIZE * permissionsCount);
		for (int i = 0; i < permissionsCount; i++) {
			access.setInt(i * SIZE, permission.getPermissions().get(i)
					.ordinal());
		}
		allocateMemory();
	}

	// public ContainerPermissions(ContainerPermission permission) {
	// int permissionsCount = permission.getPermissions().size();
	// cont_name = permission.getContainerName();
	// access_len = new SizeT(permissionsCount);
	// access_cap = new SizeT(permissionsCount);
	// // int SIZE = Native.getNativeSize(Integer.class);
	// access = new int[permissionsCount == 0 ? 1 : permissionsCount];
	// for (int i = 0; i < permissionsCount; i++) {
	// access[i] = permission.getPermissions().get(i)
	// .ordinal();
	// }
	// allocateMemory();
	// }

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("cont_name", "access", "access_len", "access_cap");
	}
}
