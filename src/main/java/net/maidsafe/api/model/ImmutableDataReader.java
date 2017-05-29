package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.ImmutableDataBinding;

import com.sun.jna.Pointer;
import net.maidsafe.utils.CallbackHelper;

public class ImmutableDataReader {

	private final Pointer appHandle;
	private final long handle;
	private final ImmutableDataBinding lib;
	private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

	public ImmutableDataReader(Pointer appHandle, long handle) {
		this.appHandle = appHandle;
		this.handle = handle;
		lib = BindingFactory.getInstance().getImmutableData();
	}

	public CompletableFuture<Long> getSize() {
		final CompletableFuture<Long> future = new CompletableFuture<>();
		lib.idata_size(appHandle, handle, Pointer.NULL, callbackHelper.getHandleCallBack(future));
		return future;
	}

	public CompletableFuture<byte[]> read(long offset, long lengthToRead) {
		final CompletableFuture<byte[]> future = new CompletableFuture<>();
		lib.idata_read_from_self_encryptor(appHandle, handle, offset,
				lengthToRead, Pointer.NULL, callbackHelper.getDataCallback(future));
		return future;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		lib.idata_self_encryptor_reader_free(appHandle, handle, Pointer.NULL,
				callbackHelper.getResultCallBack(null));
	}

}
