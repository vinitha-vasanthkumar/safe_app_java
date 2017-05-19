package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.CipherOpt;
import net.maidsafe.api.model.PublicEncryptKey;
//import net.maidsafe.api.model.CipherOpt;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CipherOptBinding;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.binding.model.FfiCallback.HandleCallback;

public class CipherOption {
	private final App app;
	private CipherOptBinding cipherOpt = BindingFactory.getInstance()
			.getCipherOpt();

	public CipherOption(App app) {
		this.app = app;
	}

	public CompletableFuture<CipherOpt> getPlain() {
		final CompletableFuture<CipherOpt> future;
		future = new CompletableFuture<CipherOpt>();

		cipherOpt.cipher_opt_new_plaintext(app.getAppHandle(), Pointer.NULL,
				getHandleCallback(future));

		return future;
	}

	public CompletableFuture<CipherOpt> getSymmetric() {
		final CompletableFuture<CipherOpt> future;
		future = new CompletableFuture<CipherOpt>();

		cipherOpt.cipher_opt_new_symmetric(app.getAppHandle(), Pointer.NULL,
				getHandleCallback(future));

		return future;
	}

	public CompletableFuture<CipherOpt> getAsymmetric(
			PublicEncryptKey publicEncryptKey) {
		final CompletableFuture<CipherOpt> future;
		future = new CompletableFuture<CipherOpt>();

		cipherOpt.cipher_opt_new_asymmetric(app.getAppHandle(),
				publicEncryptKey.getHandle(), Pointer.NULL,
				getHandleCallback(future));

		return future;
	}

	private HandleCallback getHandleCallback(
			final CompletableFuture<CipherOpt> future) {
		return new HandleCallback() {

			@Override
			public void onResponse(Pointer userData, FfiResult result,
					long handle) {
				if (result.isError()) {
					future.completeExceptionally(new Exception(result
							.errorMessage()));
					return;
				}
				future.complete(new CipherOpt(app.getAppHandle(), handle));
			}
		};
	}

}
