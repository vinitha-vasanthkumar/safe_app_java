package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.SignKey;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.binding.model.FfiCallback.HandleCallback;
import net.maidsafe.utils.FfiConstant;

public class Crypto {

	private App app;
	private CryptoBinding lib = BindingFactory.getInstance().getCrypto();

	public Crypto(App app) {
		this.app = app;
	}

	public CompletableFuture<SignKey> getAppPublicSignKey() {
		final CompletableFuture<SignKey> future;
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
						future.complete(new SignKey(app.getAppHandle(), handle));
					}
				});
		return future;
	}
	
	public CompletableFuture<SignKey> getSignKey(byte[] raw) {
		final CompletableFuture<SignKey> future;
		future = new CompletableFuture<>();	
		Pointer rawPointer = new Memory(FfiConstant.SIGN_PUBLICKEYBYTES);
		rawPointer.write(0, raw, 0, FfiConstant.SIGN_PUBLICKEYBYTES);
		lib.sign_key_new(app.getAppHandle(), rawPointer, Pointer.NULL,
				new HandleCallback() {
					@Override
					public void onResponse(Pointer userData, FfiResult result,
							long handle) {
						if (result.isError()) {
							future.completeExceptionally(new Exception(result
									.errorMessage()));
							return;
						}
						future.complete(new SignKey(app.getAppHandle(), handle));
					}
				});
		return future;
	}

}