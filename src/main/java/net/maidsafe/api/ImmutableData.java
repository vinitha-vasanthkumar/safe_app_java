package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.ImmutableDataReader;
import net.maidsafe.api.model.ImmutableDataWriter;
import net.maidsafe.api.model.XorName;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.ImmutableDataBinding;
import net.maidsafe.binding.model.FfiCallback.HandleCallback;
import net.maidsafe.binding.model.FfiResult;

public class ImmutableData {
	private final App app;
	private final ImmutableDataBinding idBinding;

	public ImmutableData(App app) {
		this.app = app;
		idBinding = BindingFactory.getInstance().getImmutableData();
	}

	public CompletableFuture<ImmutableDataWriter> getWriter() {
		final CompletableFuture<ImmutableDataWriter> future;
		future = new CompletableFuture<ImmutableDataWriter>();

		idBinding.idata_new_self_encryptor(app.getAppHandle(), Pointer.NULL,
				new HandleCallback() {

					@Override
					public void onResponse(Pointer userData,
							FfiResult.ByVal result, long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new ImmutableDataWriter(app
								.getAppHandle(), handle));
					}
				});

		return future;
	}

	public CompletableFuture<ImmutableDataReader> getReader(XorName name) {
		final CompletableFuture<ImmutableDataReader> future;
		future = new CompletableFuture<ImmutableDataReader>();

		idBinding.idata_fetch_self_encryptor(app.getAppHandle(), name.getRaw(),
				Pointer.NULL, new HandleCallback() {

					@Override
					public void onResponse(Pointer userData,
							FfiResult.ByVal result, long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new ImmutableDataReader(app
								.getAppHandle(), handle));
					}
				});

		return future;
	}

}
