package net.maidsafe.api;

import com.sun.jna.Pointer;
import net.maidsafe.api.model.AppInfo;
import net.maidsafe.api.model.ContainerPermission;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Auth {

	public CompletableFuture<String> getURI(AppInfo appInfo,
			List<ContainerPermission> permissions) {
		return this.getURI(appInfo, permissions, false);
	}

	public CompletableFuture<String> getURI(AppInfo appInfo,
                                            List<ContainerPermission> permissions, boolean createAppContainer) {
		final CompletableFuture<String> future;
		FfiCallback.Auth callback;
		AppExchangeInfo appExchangeInfo;
		List<ContainerPermissions> containerPermissions;
		AuthReq request;

		future = new CompletableFuture<String>();
		callback = new FfiCallback.Auth() {

			@Override
			public void onResponse(Pointer userData, FfiResult result,
					int reqId, String uri) {
				if (result.isError()) {
					future.completeExceptionally(new Exception(result
							.toString()));
					return;
				}
				future.complete(uri);
			}
		};

		try {
			appExchangeInfo = new AppExchangeInfo(appInfo);
			containerPermissions = new ArrayList<ContainerPermissions>();
			if (permissions != null) {
				for (ContainerPermission permission : permissions) {
					containerPermissions.add(new ContainerPermissions(
							permission));
				}
			}
			request = new AuthReq(appExchangeInfo, containerPermissions,
					createAppContainer);

			BindingFactory.getInstance().getAuth()
					.encode_auth_req(request, null, callback);
		} catch (Exception e) {
			future.completeExceptionally(e);
		}
		return future;
	}

	public SafeClient getUnregisteredClient() {
		return null;
	}

	public SafeClient getClient(String uri) {
		return null;
	}

}
