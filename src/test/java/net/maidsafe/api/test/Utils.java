package net.maidsafe.api.test;

import java.util.List;

import net.maidsafe.api.SafeClient;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.ContainerPermission;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.model.FfiContainerPermission;

import com.sun.jna.ptr.PointerByReference;

public class Utils {

	public static SafeClient getTestApp() {
		App app = new App(null, null, null);
		PointerByReference appPointerRef = new PointerByReference();
		BindingFactory.getInstance().getAuth().test_create_app(appPointerRef);
		app.setAppHandle(appPointerRef.getValue());
		return new SafeClient(app);
	}

	public static SafeClient getTestAppWithAccess() {
		App app = new App(null, null, null);
		PointerByReference appPointerRef = new PointerByReference();

		BindingFactory
				.getInstance()
				.getAuth()
				.test_create_app_with_access(new FfiContainerPermission[1], 0,
						appPointerRef);
		app.setAppHandle(appPointerRef.getValue());
		return new SafeClient(app);
	}

	public static SafeClient getTestAppWithAccess(
			List<ContainerPermission> access) {
		App app = new App(null, null, null);
		PointerByReference appPointerRef = new PointerByReference();
		final FfiContainerPermission[] accessPermissions = new FfiContainerPermission[access
				.size()];
		int i = 0;
		for (ContainerPermission container : access) {
			accessPermissions[i] = new FfiContainerPermission(container);
			i++;
		}

		BindingFactory
				.getInstance()
				.getAuth()
				.test_create_app_with_access(accessPermissions, access.size(),
						appPointerRef);
		app.setAppHandle(appPointerRef.getValue());
		return new SafeClient(app);
	}

}
