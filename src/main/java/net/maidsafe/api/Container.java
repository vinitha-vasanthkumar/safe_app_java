package net.maidsafe.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.api.model.Permission;
import net.maidsafe.binding.AccessContainerBinding;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.model.FfiCallback.BooleanCallback;
import net.maidsafe.binding.model.FfiCallback.HandleCallback;
import net.maidsafe.binding.model.FfiCallback.CallbackForData;
import net.maidsafe.binding.model.FfiCallback.ResultCallback;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.utils.Helper;

public class Container {

	private final App app;
	private final AccessContainerBinding accessBinding = BindingFactory
			.getInstance().getAccessContainer();

	public Container(App app) {
		this.app = app;
	}

	public CompletableFuture<List<String>> getContainers() {
		final CompletableFuture<List<String>> future;
		future = new CompletableFuture<>();

		accessBinding.access_container_get_names(app.getAppHandle(),
				Pointer.NULL, new CallbackForData() {

					@Override
					public void onResponse(Pointer userData, FfiResult result,
							Pointer data, long dataLen) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(Arrays.asList(data.getStringArray(0,
								(int) dataLen)));
					}
				});

		return future;
	}

	public CompletableFuture<Void> refresh() {
		final CompletableFuture<Void> future = new CompletableFuture<>();

		accessBinding.access_container_refresh_access_info(app.getAppHandle(),
				Pointer.NULL, new ResultCallback() {

					@Override
					public void onResponse(Pointer userData, FfiResult result) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(null);
					}
				});

		return future;
	}

	public CompletableFuture<MutableData> getContainer(String containerName) {
		final CompletableFuture<MutableData> future;
		future = new CompletableFuture<MutableData>();

		accessBinding.access_container_get_container_mdata_info(
				app.getAppHandle(), containerName, Pointer.NULL,
				new HandleCallback() {

					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new MutableData(app.getAppHandle(),
								handle));
					}
				});

		return future;
	}

	public CompletableFuture<MutableData> getAppContainer() {
		final CompletableFuture<MutableData> future;
		future = new CompletableFuture<MutableData>();
		String appName = "apps/" + app.getAppInfo().getId();
		String scope = app.getAppInfo().getScope();
		if (scope != null && !scope.isEmpty()) {
			appName = "/" + scope;
		}

		accessBinding.access_container_get_container_mdata_info(
				app.getAppHandle(), appName, Pointer.NULL,
				new HandleCallback() {

					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new MutableData(app.getAppHandle(),
								handle));
					}
				});

		return future;
	}

	public CompletableFuture<Boolean> hasAccess(final String containerName,
			List<Permission> permissions) {
		final CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
		final List<CompletableFuture<Boolean>> permResults = new ArrayList<>();
		permissions.forEach(new Consumer<Permission>() {

			@Override
			public void accept(Permission permission) {
				permResults.add(hasAccess(containerName, permission));
			}

		});
		CompletableFuture<?>[] temp = new CompletableFuture<?>[permResults
				.size()];
		CompletableFuture.allOf(permResults.toArray(temp))
				.thenAccept(new Consumer<Void>() {

					@Override
					public void accept(Void t) {
						for (CompletableFuture<Boolean> permResult : permResults) {
							try {
								if (!permResult.get()) {
									future.complete(false);
									return;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						future.complete(true);
					}

				}).exceptionally(new Function<Throwable, Void>() {

					@Override
					public Void apply(Throwable t) {
						future.completeExceptionally(t);
						return null;
					}

				});
		return future;
	}

	private CompletableFuture<Boolean> hasAccess(String containerName,
			Permission permission) {
		final CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
		accessBinding.access_container_is_permitted(app.getAppHandle(),
				containerName, permission.ordinal(), Pointer.NULL,
				new BooleanCallback() {

					@Override
					public void onResponse(Pointer userData, FfiResult result,
							boolean flag) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(flag);
					}
				});

		return future;
	}

}
