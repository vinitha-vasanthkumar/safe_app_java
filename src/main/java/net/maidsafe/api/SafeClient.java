package net.maidsafe.api;

import net.maidsafe.api.model.App;

public class SafeClient {
	private final App app;
	private final Crypto crypto;
	private final CipherOption cipherOption;

	public SafeClient(App app) {
		this.app = app;
		crypto = new Crypto(app);
		cipherOption = new CipherOption(app);
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

}
