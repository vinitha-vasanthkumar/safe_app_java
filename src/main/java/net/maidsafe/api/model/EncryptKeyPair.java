package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.binding.model.FfiCallback.CallbackForData;

import com.sun.jna.Pointer;

public class EncryptKeyPair {

	private final SecretEncryptKey secretKey;
	private final PublicEncryptKey publicKey;
	private final Pointer appHandle;
	private final CryptoBinding lib = BindingFactory.getInstance().getCrypto();

	public EncryptKeyPair(Pointer appHandle, SecretEncryptKey secretKey,
			PublicEncryptKey publicKey) {
		this.appHandle = appHandle;
		this.secretKey = secretKey;
		this.publicKey = publicKey;
	}

	public SecretEncryptKey getSecretKey() {
		return secretKey;
	}

	public PublicEncryptKey getPublicKey() {
		return publicKey;
	}

	public CompletableFuture<byte[]> decryptSealed(byte[] data) {
		final CompletableFuture<byte[]> future;
		future = new CompletableFuture<byte[]>();

		lib.decrypt_sealed_box(appHandle, data, data.length,
				publicKey.getHandle(), secretKey.getHandle(), Pointer.NULL,
				getCallbackForData(future));

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

}
