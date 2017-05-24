package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.binding.model.FfiCallback;
import net.maidsafe.binding.model.FfiCallback.CallbackForData;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Pointer;

public class PublicEncryptKey {

	private final Pointer appHandle;
	private final long handle;
	private final CryptoBinding lib = BindingFactory.getInstance().getCrypto();

	public PublicEncryptKey(Pointer appHandle, long handle) {
		this.appHandle = appHandle;
		this.handle = handle;
	}

	public long getHandle() {
		return handle;
	}

	public CompletableFuture<byte[]> getRaw() {
		final CompletableFuture<byte[]> future = new CompletableFuture<byte[]>();
		lib.enc_pub_key_get(appHandle, handle, Pointer.NULL,
				new FfiCallback.PointerCallback() {

					@Override
					public void onResponse(Pointer userData,
							FfiResult.ByVal result, Pointer pointer) {
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

	public CompletableFuture<byte[]> encrypt(byte[] data,
			SecretEncryptKey senderSecretKey) {
		final CompletableFuture<byte[]> future;
		future = new CompletableFuture<byte[]>();

		lib.encrypt(appHandle, data, data.length, handle,
				senderSecretKey.getHandle(), Pointer.NULL,
				getCallbackForData(future));

		return future;
	}

	public CompletableFuture<byte[]> encryptSealed(byte[] data) {
		final CompletableFuture<byte[]> future;
		future = new CompletableFuture<byte[]>();

		lib.encrypt_sealed_box(appHandle, data, data.length, handle,
				Pointer.NULL, getCallbackForData(future));

		return future;
	}

	private CallbackForData getCallbackForData(
			final CompletableFuture<byte[]> future) {
		return new CallbackForData() {

			@Override
			public void onResponse(Pointer userData, FfiResult.ByVal result,
					Pointer data, long dataLen) {
				if (result.isError()) {
					future.completeExceptionally(new Exception(result
							.errorMessage()));
					return;
				}
				future.complete(data.getByteArray(0, (int) dataLen));
			}
		};
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		lib.enc_pub_key_free(appHandle, handle, Pointer.NULL,
				new FfiCallback.ResultCallback() {

					@Override
					public void onResponse(Pointer userData,
							FfiResult.ByVal result) {
					}
				});
	}

}
