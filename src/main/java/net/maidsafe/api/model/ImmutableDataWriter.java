package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.ImmutableDataBinding;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Pointer;

public class ImmutableDataWriter {

	private final Pointer appHandle;
	private final long handle;
	private final ImmutableDataBinding lib = BindingFactory.getInstance()
			.getImmutableData();
	private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

	public ImmutableDataWriter(Pointer appHandle, long handle) {
		this.appHandle = appHandle;
		this.handle = handle;
	}

	public CompletableFuture<Void> write(byte[] data) {
		final CompletableFuture<Void> future = new CompletableFuture<Void>();
		lib.idata_write_to_self_encryptor(appHandle, handle, data, data.length,
				Pointer.NULL, callbackHelper.getResultCallBack(future));
		return future;
	}

	public CompletableFuture<XorName> save(CipherOpt cipherOpt) {
		return close(cipherOpt);
	}

	public CompletableFuture<XorName> close(CipherOpt cipherOpt) {
		final CompletableFuture<XorName> future = new CompletableFuture<>();
		final CompletableFuture<Pointer> cbFuture = new CompletableFuture<>();
		lib.idata_close_self_encryptor(appHandle, handle,
				cipherOpt.getHandle(), Pointer.NULL, callbackHelper.getPointerCallback(cbFuture));
		cbFuture.thenAccept(pointer -> {
			future.complete(new XorName(pointer.getByteArray(0,
					FfiConstant.XOR_NAME_LEN)));
		}).exceptionally(e -> {
			future.completeExceptionally(e);
			return null;
		});
		return future;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		lib.idata_self_encryptor_writer_free(appHandle, handle, Pointer.NULL,
				callbackHelper.getResultCallBack(null));
	}

}
