package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.ImmutableDataBinding;
import net.maidsafe.binding.model.FfiCallback.PointerCallback;
import net.maidsafe.binding.model.FfiCallback.ResultCallback;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Pointer;

public class ImmutableDataWriter {

	private final Pointer appHandle;
	private final long handle;
	private final ImmutableDataBinding lib = BindingFactory.getInstance()
			.getImmutableData();

	public ImmutableDataWriter(Pointer appHandle, long handle) {
		this.appHandle = appHandle;
		this.handle = handle;
	}

	public CompletableFuture<Void> write(byte[] data) {
		final CompletableFuture<Void> future = new CompletableFuture<Void>();
		lib.idata_write_to_self_encryptor(appHandle, handle, data, data.length,
				Pointer.NULL, new ResultCallback() {

					@Override
					public void onResponse(Pointer userData,
							FfiResult.ByVal result) {
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

	public CompletableFuture<XorName> save(CipherOpt cipherOpt) {
		return close(cipherOpt);
	}

	public CompletableFuture<XorName> close(CipherOpt cipherOpt) {
		final CompletableFuture<XorName> future = new CompletableFuture<>();
		lib.idata_close_self_encryptor(appHandle, handle,
				cipherOpt.getHandle(), Pointer.NULL, new PointerCallback() {

					@Override
					public void onResponse(Pointer userData,
							FfiResult.ByVal result, Pointer pointer) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new XorName(pointer.getByteArray(0,
								FfiConstant.XOR_NAME_LEN)));
					}
				});
		return future;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		lib.idata_self_encryptor_writer_free(appHandle, handle, Pointer.NULL,
				new ResultCallback() {

					@Override
					public void onResponse(Pointer userData,
							FfiResult.ByVal result) {
					}
				});
	}

}
