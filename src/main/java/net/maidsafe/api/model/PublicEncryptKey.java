package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.binding.model.FfiCallback;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Pointer;

public class PublicEncryptKey {

	final Pointer appHandle;
	final long handle;
	final CryptoBinding lib = BindingFactory.getInstance().getCrypto();

	public PublicEncryptKey(Pointer appHandle, long handle) {
		this.appHandle = appHandle;
		this.handle = handle;
	}

	public long getHanldle() {
		return handle;
	}

	public CompletableFuture<byte[]> getRaw() {
		final CompletableFuture<byte[]> future = new CompletableFuture<byte[]>();
		lib.enc_pub_key_get(appHandle, handle, Pointer.NULL,
				new FfiCallback.PointerCallback() {

					@Override
					public void onResponse(Pointer userData, FfiResult result,
							Pointer pointer) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(pointer.getByteArray(0,
								FfiConstant.BOX_PUBLICKEYBYTES));
					}
				});
		return future;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		lib.enc_pub_key_free(appHandle, handle, Pointer.NULL,
				new FfiCallback.ResultCallback() {

					@Override
					public void onResponse(Pointer userData, FfiResult result) {
					}
				});
	}

}
