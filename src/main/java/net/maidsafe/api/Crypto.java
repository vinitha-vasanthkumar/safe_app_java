package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.PublicEncryptKey;
import net.maidsafe.api.model.PublicSignKey;
import net.maidsafe.api.model.SecretEncryptKey;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.binding.model.FfiCallback;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.binding.model.FfiCallback.HandleCallback;
import net.maidsafe.utils.FfiConstant;

public class Crypto {

	private App app;
	private CryptoBinding lib = BindingFactory.getInstance().getCrypto();

	public Crypto(App app) {
		this.app = app;
	}

	public CompletableFuture<PublicSignKey> getAppPublicSignKey() {
		final CompletableFuture<PublicSignKey> future;
		future = new CompletableFuture<>();
		lib.app_pub_sign_key(app.getAppHandle(), Pointer.NULL,
				new HandleCallback() {
					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new PublicSignKey(app.getAppHandle(),
								handle));
					}
				});
		return future;
	}

	public CompletableFuture<PublicSignKey> getPublicSignKey(byte[] raw) {
		final CompletableFuture<PublicSignKey> future;
		future = new CompletableFuture<>();
		if (raw == null || raw.length != FfiConstant.SIGN_PUBLICKEYBYTES) {
			future.completeExceptionally(new Exception(
					"Invalid argument - Invalid size or null"));
			return future;
		}
		lib.sign_key_new(app.getAppHandle(), raw, Pointer.NULL,
				new HandleCallback() {
					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new PublicSignKey(app.getAppHandle(),
								handle));
					}
				});
		return future;
	}

	public CompletableFuture<PublicEncryptKey> getAppPublicEncryptKey() {
		final CompletableFuture<PublicEncryptKey> future;
		future = new CompletableFuture<>();
		lib.app_pub_enc_key(app.getAppHandle(), Pointer.NULL,
				new HandleCallback() {

					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new PublicEncryptKey(
								app.getAppHandle(), handle));
					}
				});
		return future;
	}

	public CompletableFuture<PublicEncryptKey> getPublicEncryptKey(byte[] raw) {
		final CompletableFuture<PublicEncryptKey> future;
		future = new CompletableFuture<>();
		if (raw == null || raw.length != FfiConstant.BOX_PUBLICKEYBYTES) {
			future.completeExceptionally(new Exception(
					"Invalid size or null argument"));
			return future;
		}
		lib.enc_pub_key_new(app.getAppHandle(), raw, Pointer.NULL,
				new HandleCallback() {
					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new PublicEncryptKey(
								app.getAppHandle(), handle));
					}
				});
		return future;
	}

	public CompletableFuture<SecretEncryptKey> getSecretEncryptKey(byte[] raw) {
		final CompletableFuture<SecretEncryptKey> future;
		future = new CompletableFuture<>();
		lib.enc_secret_key_new(app.getAppHandle(), raw, Pointer.NULL,
				new HandleCallback() {
					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new SecretEncryptKey(
								app.getAppHandle(), handle));
					}
				});
		return future;
	}

	// generate keys
	public CompletableFuture<EncryptKeyPair> generateEncryptKeyPair() {
		final CompletableFuture<EncryptKeyPair> future;
		future = new CompletableFuture<EncryptKeyPair>();
		lib.enc_generate_key_pair(app.getAppHandle(), Pointer.NULL,
				new FfiCallback.TwoHandleCallback() {

					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long publicKey, long secretKey) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new EncryptKeyPair(
								new SecretEncryptKey(app.getAppHandle(),
										secretKey), new PublicEncryptKey(app
										.getAppHandle(), publicKey)));
					}
				});
		return future;
	}

}