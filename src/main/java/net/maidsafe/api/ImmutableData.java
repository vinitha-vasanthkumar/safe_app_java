package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.ImmutableDataReader;
import net.maidsafe.api.model.ImmutableDataWriter;
import net.maidsafe.api.model.XorName;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.ImmutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

public class ImmutableData {
	private final App app;
	private final ImmutableDataBinding idBinding;
	private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

	public ImmutableData(App app) {
		this.app = app;
		this.idBinding = BindingFactory.getInstance().getImmutableData();
	}

	public CompletableFuture<ImmutableDataWriter> getWriter() {
		final CompletableFuture<ImmutableDataWriter> future;
		final CompletableFuture<Long> cbFuture;
		future = new CompletableFuture<>();
		cbFuture = new CompletableFuture<>();

		idBinding.idata_new_self_encryptor(app.getAppHandle(), Pointer.NULL,
				callbackHelper.getHandleCallBack(cbFuture));

		cbFuture.thenAccept(handle -> future.complete(new ImmutableDataWriter(app.getAppHandle(), handle))).exceptionally(e -> {
			future.completeExceptionally(e);
			return null;
		});
		return future;
	}

	public CompletableFuture<ImmutableDataReader> getReader(XorName name) {
		final CompletableFuture<ImmutableDataReader> future;
		final CompletableFuture<Long> cbFuture;
		future = new CompletableFuture<>();
		cbFuture = new CompletableFuture<>();

		idBinding.idata_fetch_self_encryptor(app.getAppHandle(), name.getRaw(),
				Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

		cbFuture.thenAccept(handle -> future.complete(new ImmutableDataReader(app.getAppHandle(), handle))).exceptionally(e -> {
			future.completeExceptionally(e);
			return null;
		});

		return future;
	}

}
