package net.maidsafe.api;

import net.maidsafe.api.model.App;

public class SafeClient {
	private final App app;
	private final Crypto crypto;
	private final CipherOption cipherOption;
	private final ImmutableData immutableData;

	public SafeClient(App app) {
		this.app = app;
		crypto = new Crypto(app);
		cipherOption = new CipherOption(app);
		immutableData = new ImmutableData(app);
	}

	public boolean isAnonymous() {
		return app.isAnonymous();
	}

	public Crypto crypto() {
		return crypto;
	}

	public CipherOption cipherOpt() {
		return cipherOption;
	}

	public ImmutableData immutableData() {
		return immutableData;
	}

}
