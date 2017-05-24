package net.maidsafe.api;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.AccessContainerMeta;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.AppInfo;
import net.maidsafe.api.model.ContainerPermission;
import net.maidsafe.api.model.Keys;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.model.AppExchangeInfo;
import net.maidsafe.binding.model.AuthGrantedResponse;
import net.maidsafe.binding.model.AuthReq;
import net.maidsafe.binding.model.FfiContainerPermission;
import net.maidsafe.binding.model.ContainerRequest;
import net.maidsafe.binding.model.FfiCallback;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.utils.Helper;

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
		AuthReq request;

		if (permissions == null) {
			permissions = new ArrayList<>();
		}

		future = new CompletableFuture<String>();
		callback = new FfiCallback.Auth() {

			@Override
			public void onResponse(Pointer userData, FfiResult.ByVal result,
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
			request = new AuthReq(appExchangeInfo, permissions,
					createAppContainer);

			BindingFactory.getInstance().getAuth()
					.encode_auth_req(request, null, callback);
		} catch (Exception e) {
			future.completeExceptionally(e);
		}
		return future;
	}

	public CompletableFuture<String> getContainerRequestURI(AppInfo appInfo,
			List<ContainerPermission> permissions) {
		AppExchangeInfo appExchInfo;
		List<FfiContainerPermission> contPermissions;
		FfiCallback.Auth callback;
		final CompletableFuture<String> future;

		future = new CompletableFuture<String>();
		
		if (permissions == null || permissions.isEmpty()) {
			future.completeExceptionally(new Exception(
					"Containers can not be empty"));
			return future;
		}			

		appExchInfo = new AppExchangeInfo(appInfo);
		contPermissions = new ArrayList<FfiContainerPermission>();

		for (ContainerPermission permission : permissions) {
			contPermissions.add(new FfiContainerPermission(permission));
		}
		callback = new FfiCallback.Auth() {

			@Override
			public void onResponse(Pointer userData, FfiResult.ByVal result,
					int reqId, String uri) {
				if (result.isError()) {
					future.completeExceptionally(new Exception(result
							.errorMessage()));
					return;
				}
				future.complete(uri);
			}
		};

		BindingFactory
				.getInstance()
				.getAuth()
				.encode_containers_req(
						new ContainerRequest(appExchInfo, contPermissions),
						null, callback);
		return future;
	}

	public CompletableFuture<SafeClient> connectAsAnnonymous(
			final NetworkObserver observer) {
		final CompletableFuture<SafeClient> future;
		future = observer.getFuture();
		observer.setApp(new App(null, null, null));
		BindingFactory
				.getInstance()
				.getAuth()
				.app_unregistered(null, observer.getObserver(),
						observer.getAppRef());
		if (Helper.isMockEnvironment()) {
			observer.getObserver().onResponse(null, 0, 0);
		}
		return future;
	}

	public CompletableFuture<SafeClient> connectWithURI(final AppInfo appInfo,
			final String uri, final NetworkObserver observer) {
		final CompletableFuture<SafeClient> future;
		future = observer.getFuture();

		FfiCallback.AuthGranted authCb = new FfiCallback.AuthGranted() {

			@Override
			public void onResponse(Pointer userData, int reqId,
					AuthGrantedResponse authGranted) {
				observer.setApp(new App(appInfo,
						new Keys(authGranted.app_keys),
						new AccessContainerMeta(authGranted.access_container)));
				try {
					BindingFactory
							.getInstance()
							.getAuth()
							.app_registered(appInfo.getId(), authGranted, null,
									observer.getObserver(),
									observer.getAppRef());
					if (Helper.isMockEnvironment()) {
						observer.getObserver().onResponse(null, 0, 0);
					}
				} catch (Exception e) {
					future.completeExceptionally(e);
				}
			}
		};

		FfiCallback.ReqIdCallback containerCb = new FfiCallback.ReqIdCallback() {

			@Override
			public void onResponse(Pointer userData, int reqId) {
				future.completeExceptionally(new Exception(
						"Not a valid URI for creating a client"));
			}
		};

		FfiCallback.NoArgCallback revokedCb = new FfiCallback.NoArgCallback() {

			@Override
			public void onResponse(Pointer userData) {
				future.completeExceptionally(new Exception(
						"Not a valid URI for creating a client"));
			}
		};

		FfiCallback.ErrorCallback errCb = new FfiCallback.ErrorCallback() {

			@Override
			public void onResponse(Pointer userData, FfiResult.ByVal result, int reqId) {
				future.completeExceptionally(new Exception(result
						.errorMessage()));

			}
		};

		BindingFactory
				.getInstance()
				.getAuth()
				.decode_ipc_msg(uri, null, authCb, containerCb, revokedCb,
						errCb);

		return future;
	}

}
