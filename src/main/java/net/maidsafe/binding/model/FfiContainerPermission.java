package net.maidsafe.binding.model;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import net.maidsafe.api.model.ContainerPermission;

import java.util.Arrays;
import java.util.List;

public class FfiContainerPermission extends Structure {

	public static class ByReference extends FfiContainerPermission implements
			Structure.ByReference {
	}

	public String cont_name;
	public Pointer access = Pointer.NULL;
	public long access_len;
	public long access_cap;

	public FfiContainerPermission() {
	}

	public FfiContainerPermission(ContainerPermission permission) {
		final int SIZE = Native.getNativeSize(Integer.class);
		int permissionsCount = permission.getPermissions().size();
		cont_name = permission.getContainerName();
		access_len = permissionsCount;
		access_cap = permissionsCount;
		if (permissionsCount == 0) {
			return;
		}
		access = new Memory(SIZE * permissionsCount);
		for (int i = 0; i < permissionsCount; i++) {
			access.setInt(i * SIZE, permission.getPermissions().get(i)
					.ordinal());
		}
		// allocateMemory();
	}

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("cont_name", "access", "access_len", "access_cap");
	}
}
