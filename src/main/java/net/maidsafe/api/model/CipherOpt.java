package net.maidsafe.api.model;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CipherOptBinding;

import com.sun.jna.Pointer;
import net.maidsafe.utils.CallbackHelper;

public class CipherOpt {

	private final Pointer appHandle;
	private final long handle;
	private final CipherOptBinding lib = BindingFactory.getInstance()
			.getCipherOpt();

	public CipherOpt(Pointer appHandle, long handle) {
		this.appHandle = appHandle;
		this.handle = handle;
	}

	public long getHandle() {
		return handle;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		lib.cipher_opt_free(appHandle, handle, Pointer.NULL,
				CallbackHelper.getInstance().getResultCallBack(null));
	}

}
