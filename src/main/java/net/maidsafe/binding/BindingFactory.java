package net.maidsafe.binding;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class BindingFactory implements Cloneable {

	private static BindingFactory factory;
	private final String LIB_NAME = "safe_app";
	private final String WIN_DEP_LIB = "libwinpthread-1";
	private static final String OS = System.getProperty("os.name")
			.toLowerCase();
	private AuthBinding auth;
	private CryptoBinding crypto;

	private BindingFactory() {
		if (OS.contains("win")) {
			NativeLibrary.getInstance(WIN_DEP_LIB);
		}

		NativeLibrary.getInstance(LIB_NAME);
		auth = Native.loadLibrary(AuthBinding.class);
		crypto = Native.loadLibrary(CryptoBinding.class);
	}

	public static synchronized BindingFactory getInstance() {
		if (factory == null) {
			factory = new BindingFactory();
		}
		return factory;
	}

	public AuthBinding getAuth() {
		return auth;
	}

	public CryptoBinding getCrypto() {
		return crypto;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
