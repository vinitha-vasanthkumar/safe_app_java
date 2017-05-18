package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.binding.model.FfiResult;
import net.maidsafe.binding.model.FfiCallback.ResultCallback;
import net.maidsafe.binding.model.FfiCallback.PointerCallback;
import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Pointer;

public class PublicSignKey {

	private long handle;
	private CryptoBinding lib = BindingFactory.getInstance().getCrypto();
	private Pointer appHandle;
	
	public PublicSignKey(Pointer appHandle, long handle) {
		this.appHandle = appHandle;
		this.handle = handle;
	}
	
	public long getHandle() {
		return handle;
	}
	
	public CompletableFuture<byte[]> getRaw() {
		final CompletableFuture<byte[]> future = new CompletableFuture<byte[]>();
		lib.sign_key_get(appHandle, handle, Pointer.NULL, new PointerCallback() {
			
			@Override
			public void onResponse(Pointer userData, FfiResult result, Pointer pointer) {
				if (result.isError()) {
					future.completeExceptionally(new Exception(result.errorMessage()));
					return;
				}
				future.complete(pointer.getByteArray(0, FfiConstant.SIGN_PUBLICKEYBYTES));
			}
		});
		return future;
	}
	
	@Override
	protected void finalize() throws Throwable {	
		super.finalize();
		lib.sign_key_free(appHandle, handle, Pointer.NULL, new ResultCallback() {			
			@Override
			public void onResponse(Pointer userData, FfiResult result) {				
			}
		});
	}
	
}
