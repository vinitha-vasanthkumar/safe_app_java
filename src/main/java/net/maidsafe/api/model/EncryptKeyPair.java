package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;

import com.sun.jna.Pointer;
import net.maidsafe.utils.CallbackHelper;

public class EncryptKeyPair {

	private final SecretEncryptKey secretKey;
	private final PublicEncryptKey publicKey;
	private final Pointer appHandle;
	private final CryptoBinding lib = BindingFactory.getInstance().getCrypto();
	private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

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
		future = new CompletableFuture<>();

		lib.decrypt_sealed_box(appHandle, data, data.length,
				publicKey.getHandle(), secretKey.getHandle(), Pointer.NULL,
				callbackHelper.getDataCallback(future));

		return future;
	}

}
