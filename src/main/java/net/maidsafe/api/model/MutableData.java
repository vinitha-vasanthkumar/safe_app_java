package net.maidsafe.api.model;

import com.sun.jna.Pointer;

public class MutableData {

	private final Pointer appHandle;
	private final long mdataInfoHandle;

	public MutableData(Pointer appHandle, long mdataInfoHandle) {
		this.appHandle = appHandle;
		this.mdataInfoHandle = mdataInfoHandle;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO implement free for mdataInfoHandle
		super.finalize();
	}

}
